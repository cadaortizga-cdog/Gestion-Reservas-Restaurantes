package com.gestion_reservas_restaurantes.backend.service;

import com.gestion_reservas_restaurantes.backend.model.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);

    Reservation updateReservation(Long id, Reservation reservation);

    void cancelReservation(Long id);

    Reservation getReservationById(Long id);

    List<Reservation> getAllReservations();
}