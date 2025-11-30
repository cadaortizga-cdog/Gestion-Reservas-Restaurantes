package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.serviceImpl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Reservation;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.ReservationRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.RestaurantTableRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.VipWaitlistRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.ClientRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceImplTest {

    private ReservationRepository reservationRepository;
    private RestaurantTableRepository restaurantTableRepository;
    private VipWaitlistRepository vipWaitlistRepository;
    private ClientRepository clientRepository;
    private ReservationServiceImpl service;

    @BeforeEach
    void setup() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        restaurantTableRepository = Mockito.mock(RestaurantTableRepository.class);
        vipWaitlistRepository = Mockito.mock(VipWaitlistRepository.class);
        clientRepository = Mockito.mock(ClientRepository.class);

        service = new ReservationServiceImpl(
                reservationRepository,
                restaurantTableRepository,
                vipWaitlistRepository,
                clientRepository
        );

        Mockito.when(reservationRepository.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    void createReservation_futureDurationOk_saves() {
        RestaurantTable table = new RestaurantTable();
        table.setId(1L);
        table.setCapacity(4);
        table.setTableStatus("disponible");
        table.setVipExclusive(false);

        Client client = new Client();
        client.setId(1L);
        client.setName("Juan Pérez");
        client.setVip(false);
        client.setPoints(0);
        client.setLoyaltyLevel("Bronce");

        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(reservationRepository.findConflicts(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(Collections.emptyList());

        Mockito.when(reservationRepository.save(ArgumentMatchers.any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation r = new Reservation();
        r.setTable(table);
        r.setClient(client);
        r.setDateTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0));
        r.setEndTime(r.getDateTime().plusHours(1));
        r.setNumPeople(3);

        Reservation saved = service.createReservation(r);

        assertEquals("pendiente", saved.getStatus());
        assertNotNull(saved.getTable());
        assertNotNull(saved.getClient());
        Mockito.verify(reservationRepository).save(ArgumentMatchers.any(Reservation.class));
    }

    @Test
    void createReservation_conflict_throws() {
        RestaurantTable table = new RestaurantTable();
        table.setId(2L);
        table.setCapacity(4);
        table.setTableStatus("disponible");
        table.setVipExclusive(false);

        Client client = new Client();
        client.setId(2L);
        client.setName("Ana García");
        client.setVip(false);
        client.setPoints(0);

        Mockito.when(restaurantTableRepository.findById(2L)).thenReturn(Optional.of(table));
        Mockito.when(clientRepository.findById(2L)).thenReturn(Optional.of(client));

        Mockito.when(reservationRepository.findConflicts(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(List.of(new Reservation()));

        Reservation r = new Reservation();
        r.setTable(table);
        r.setClient(client);
        r.setDateTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0));
        r.setEndTime(r.getDateTime().plusHours(1));
        r.setNumPeople(2);


        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createReservation(r));
        assertTrue(ex.getMessage().contains("La mesa ya está reservada"));
    }

    @Test
    void createReservation_vipClient_noAvailability_goesToWaitlist() {
        RestaurantTable table = new RestaurantTable();
        table.setId(5L);
        table.setCapacity(4);
        table.setTableStatus("disponible");
        table.setVipExclusive(false);

        Client vipClient = new Client();
        vipClient.setId(10L);
        vipClient.setName("Carlos VIP");
        vipClient.setVip(true);
        vipClient.setPoints(150);
        vipClient.setLoyaltyLevel("Oro");

        Mockito.when(restaurantTableRepository.findById(5L)).thenReturn(Optional.of(table));
        Mockito.when(clientRepository.findById(10L)).thenReturn(Optional.of(vipClient));
        Mockito.when(restaurantTableRepository.count()).thenReturn(10L);
        Mockito.when(vipWaitlistRepository.countByDateTime(ArgumentMatchers.any())).thenReturn(0L);

        Mockito.when(reservationRepository.findConflicts(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(List.of(new Reservation()));

        Reservation r = new Reservation();
        r.setTable(table);
        r.setClient(vipClient);
        r.setDateTime(LocalDateTime.now().plusHours(2));
        r.setEndTime(r.getDateTime().plusHours(1));
        r.setNumPeople(2);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createReservation(r));
        assertTrue(ex.getMessage().contains("agregado a la lista de espera"));

        Mockito.verify(vipWaitlistRepository).save(ArgumentMatchers.any());
    }

    @Test
    void refreshStatuses_pastReservations_areCancelled() {
        Reservation past = new Reservation();
        past.setId(1L);
        past.setDateTime(LocalDateTime.now().minusHours(2));
        past.setEndTime(LocalDateTime.now().minusHours(1));
        past.setStatus("confirmada");

        Mockito.when(reservationRepository.findAll()).thenReturn(List.of(past));
        Mockito.when(reservationRepository.save(ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.refreshStatuses();
        assertEquals("cancelada", past.getStatus());
    }

    @Test
    void finishReservation_addPointsToClient() {
        Client c = new Client();
        c.setId(1L);
        c.setPoints(0);
        c.setLoyaltyLevel("Bronce");

        Reservation r = new Reservation();
        r.setId(1L);
        r.setClient(c);
        r.setStatus("pendiente");

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(c));
        Mockito.when(clientRepository.save(ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(reservationRepository.save(ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.finishReservation(1L);

        assertEquals("finalizada", r.getStatus());
        assertEquals(5, c.getPoints());
    }

    @Test
    void scheduler_calls_refreshStatuses() {
        ReservationServiceImpl spy = Mockito.spy(service);
        spy.refreshStatuses();
        Mockito.verify(spy).refreshStatuses();
    }

    @Test
    void createReservation_nonVipClient_vipExclusiveTable_throws() {
        RestaurantTable vipTable = new RestaurantTable();
        vipTable.setId(99L);
        vipTable.setCapacity(6);
        vipTable.setTableStatus("disponible");
        vipTable.setVipExclusive(true);
        Client regularClient = new Client();
        regularClient.setId(50L);
        regularClient.setName("Pedro Regular");
        regularClient.setVip(false);
        regularClient.setPoints(10);

        Mockito.when(restaurantTableRepository.findById(99L)).thenReturn(Optional.of(vipTable));
        Mockito.when(clientRepository.findById(50L)).thenReturn(Optional.of(regularClient));
        Mockito.when(reservationRepository.findConflicts(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(Collections.emptyList());

        Reservation r = new Reservation();
        r.setTable(vipTable);
        r.setClient(regularClient);
        r.setDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0));
        r.setEndTime(r.getDateTime().plusHours(2));
        r.setNumPeople(4);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createReservation(r));
        assertTrue(ex.getMessage().contains("exclusiva para clientes VIP"));
    }
}