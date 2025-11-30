package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Reservation;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.VipWaitlist;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.ClientRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.ReservationRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.RestaurantTableRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.VipWaitlistRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final VipWaitlistRepository vipWaitlistRepository;
    private final ClientRepository clientRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantTableRepository restaurantTableRepository,
                                  VipWaitlistRepository vipWaitlistRepository,
                                  ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.vipWaitlistRepository = vipWaitlistRepository;
        this.clientRepository = clientRepository;
    }

    private String levelByPoints(Integer points) {
        if (points == null) return "Bronce";
        if (points >= 100) return "Oro";
        if (points >= 50) return "Plata";
        return "Bronce";
    }

    private int pointsForLevel(String level) {
        return switch (level) {
            case "Oro" -> 15;
            case "Plata" -> 10;
            default -> 5;
        };
    }

    @Transactional
    public void refreshStatuses() {
        List<Reservation> all = reservationRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : all) {
            if ("pendiente".equals(r.getStatus())) {
                LocalDateTime confirmDeadline = r.getDateTime().minusMinutes(30);
                if (now.isAfter(confirmDeadline)) {
                    r.setStatus("cancelada");
                    reservationRepository.save(r);
                }
            }

            if ("confirmada".equals(r.getStatus())) {
                if (r.getEndTime() != null && now.isAfter(r.getEndTime())) {
                    r.setStatus("cancelada");
                    reservationRepository.save(r);
                }
            }
        }
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        refreshStatuses();

        if (reservation.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La reserva debe ser en una fecha futura");
        }

        long minutes = Duration.between(reservation.getDateTime(), reservation.getEndTime()).toMinutes();
        if (minutes < 60) {
            throw new RuntimeException("La reserva debe durar mínimo 1 hora");
        }

        LocalTime startTime = reservation.getDateTime().toLocalTime();
        LocalTime endTime = reservation.getEndTime().toLocalTime();
        LocalTime open = LocalTime.of(10, 0);
        LocalTime close = LocalTime.of(22, 0);

        if (startTime.isBefore(open) || !startTime.isBefore(close) ||
                endTime.isAfter(close) || !endTime.isAfter(open)) {
            throw new RuntimeException("Las reservas solo están permitidas entre 10:00 y 22:00");
        }

        RestaurantTable table;
        if (reservation.getTable() != null && reservation.getTable().getId() != null) {
            table = restaurantTableRepository.findById(reservation.getTable().getId())
                    .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        } else {
            throw new RuntimeException("Debe especificar la mesa (id)");
        }

        if (reservation.getNumPeople() > table.getCapacity()) {
            throw new RuntimeException("La mesa no tiene capacidad suficiente");
        }

        if (!"disponible".equalsIgnoreCase(table.getTableStatus())) {
            throw new RuntimeException("La mesa no está disponible (estado: " + table.getTableStatus() + ")");
        }

        Client cliente;
        if (reservation.getClient() != null && reservation.getClient().getId() != null) {
            cliente = clientRepository.findById(reservation.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else {
            throw new RuntimeException("Debe especificar el cliente");
        }

        if (Boolean.TRUE.equals(table.getVipExclusive())) {
            if (!Boolean.TRUE.equals(cliente.getVip())) {
                throw new RuntimeException("Esta mesa es exclusiva para clientes VIP");
            }
        }

        List<Reservation> conflicts = reservationRepository.findConflicts(
                table.getId(),
                reservation.getDateTime(),
                reservation.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            if (Boolean.TRUE.equals(cliente.getVip())) {
                long totalTables = restaurantTableRepository.count();
                long maxWait = Math.max(1, Math.round(totalTables * 0.20));
                long currentWait = vipWaitlistRepository.countByDateTime(reservation.getDateTime());

                if (currentWait < maxWait) {
                    VipWaitlist w = new VipWaitlist();
                    w.setClient(cliente);
                    w.setDateTime(reservation.getDateTime());
                    w.setNumPeople(reservation.getNumPeople());
                    w.setListStatus("en_espera");
                    vipWaitlistRepository.save(w);

                    throw new RuntimeException("No hay mesas disponibles: cliente VIP agregado a la lista de espera");
                } else {
                    throw new RuntimeException("No hay mesas disponibles y la lista de espera VIP está llena (20%)");
                }
            } else {
                throw new RuntimeException("La mesa ya está reservada en ese horario");
            }
        }

        reservation.setStatus("pendiente");
        reservation.setTable(table);
        reservation.setClient(cliente);

        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long id, Reservation reservation) {

        refreshStatuses();

        Reservation existing = getReservationById(id);

        existing.setDateTime(reservation.getDateTime());
        existing.setEndTime(reservation.getEndTime());
        existing.setNumPeople(reservation.getNumPeople());
        existing.setStatus(reservation.getStatus());

        if (reservation.getTable() != null && reservation.getTable().getId() != null) {
            RestaurantTable newTable = restaurantTableRepository.findById(reservation.getTable().getId())
                    .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
            existing.setTable(newTable);
        }

        List<Reservation> conflicts = reservationRepository.findConflicts(
                existing.getTable().getId(),
                existing.getDateTime(),
                existing.getEndTime()
        ).stream().filter(r -> !r.getId().equals(existing.getId())).collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("La mesa ya está reservada en ese horario");
        }

        return reservationRepository.save(existing);
    }

    @Override
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);

        if (!"pendiente".equals(reservation.getStatus()) &&
                !"confirmada".equals(reservation.getStatus())) {
            throw new RuntimeException("Solo se pueden cancelar reservas en estado pendiente o confirmada");
        }

        reservation.setStatus("cancelada");
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation finishReservation(Long id) {

        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"pendiente".equals(r.getStatus()) &&
                !"confirmada".equals(r.getStatus())) {
            throw new RuntimeException("Solo reservas pendientes o confirmadas pueden finalizarse");
        }

        r.setStatus("finalizada");

        if (r.getClient() != null) {
            Client c = clientRepository.findById(r.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            String level = levelByPoints(c.getPoints());
            int add = pointsForLevel(level);

            c.setPoints(c.getPoints() + add);
            c.setLoyaltyLevel(levelByPoints(c.getPoints()));

            clientRepository.save(c);
        }

        return reservationRepository.save(r);
    }

    @Transactional
    public Reservation confirmReservation(Long id) {

        Reservation r = getReservationById(id);

        if (!"pendiente".equals(r.getStatus())) {
            throw new RuntimeException("Solo reservas pendientes pueden confirmarse");
        }

        if (LocalDateTime.now().isAfter(r.getDateTime().minusMinutes(30))) {
            throw new RuntimeException("Ya pasó el límite para confirmar");
        }

        r.setStatus("confirmada");
        r.setConfirmedAt(LocalDateTime.now());

        if (r.getClient() != null) {
            Client c = clientRepository.findById(r.getClient().getId()).orElseThrow();
            String level = levelByPoints(c.getPoints());
            int add = pointsForLevel(level);

            c.setPoints(c.getPoints() + add);
            c.setLoyaltyLevel(levelByPoints(c.getPoints()));
            clientRepository.save(c);
        }

        return reservationRepository.save(r);
    }

    @Override
    public Reservation getReservationById(Long id) {
        refreshStatuses();
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Override
    public List<Reservation> getAllReservations() {
        refreshStatuses();
        return reservationRepository.findAll();
    }

    public List<RestaurantTable> findAvailableTables(LocalDateTime start, LocalDateTime end, Integer numPeople, Boolean requireVipExclusive) {
        refreshStatuses();

        List<RestaurantTable> allTables = restaurantTableRepository.findAll();

        return allTables.stream()
                .filter(t -> t.getCapacity() >= (numPeople == null ? 1 : numPeople))
                .filter(t -> "disponible".equalsIgnoreCase(t.getTableStatus()))
                .filter(t -> !Boolean.TRUE.equals(requireVipExclusive) || Boolean.TRUE.equals(t.getVipExclusive()))
                .filter(t -> reservationRepository.findConflicts(t.getId(), start, end).isEmpty())
                .collect(Collectors.toList());
    }

    public long totalTables() {
        return restaurantTableRepository.count();
    }
}
