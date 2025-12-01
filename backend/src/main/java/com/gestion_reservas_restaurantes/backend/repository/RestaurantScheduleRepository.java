package com.gestion_reservas_restaurantes.backend.repository;

import com.gestion_reservas_restaurantes.backend.model.RestaurantSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantScheduleRepository extends JpaRepository<RestaurantSchedule, Long> {
}