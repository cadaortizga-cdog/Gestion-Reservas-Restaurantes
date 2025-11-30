package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
