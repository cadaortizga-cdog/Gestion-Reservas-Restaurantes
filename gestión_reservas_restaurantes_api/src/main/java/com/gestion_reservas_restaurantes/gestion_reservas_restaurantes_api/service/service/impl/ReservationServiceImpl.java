package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Reservation;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.ReservationRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.RestaurantTableRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantTableRepository restaurantTableRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {

        if (reservation.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La reserva debe ser en una fecha futura");
        }

        long minutes = Duration.between(
                reservation.getDateTime(),
                reservation.getEndTime()
        ).toMinutes();

        if (minutes < 60) {
            throw new RuntimeException("La reserva debe durar mínimo 1 hora");
        }

        RestaurantTable table = null;
        if (reservation.getTable() != null && reservation.getTable().getId() != null) {
            table = restaurantTableRepository.findById(reservation.getTable().getId())
                    .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        } else {
            throw new RuntimeException("Debe especificar la mesa (id)");
        }

        if (reservation.getNumPeople() > table.getCapacity()) {
            throw new RuntimeException("La mesa no tiene capacidad suficiente");
        }

        reservation.setStatus("pendiente");
        reservation.setTable(table);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Long id, Reservation reservation) {

        Reservation existing = getReservationById(id);

        existing.setDateTime(reservation.getDateTime());
        existing.setEndTime(reservation.getEndTime());
        existing.setNumPeople(reservation.getNumPeople());
        existing.setStatus(reservation.getStatus());

        return reservationRepository.save(existing);
    }

    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);
        reservation.setStatus("cancelada");
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
