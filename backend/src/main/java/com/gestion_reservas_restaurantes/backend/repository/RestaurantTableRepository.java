package com.gestion_reservas_restaurantes.backend.repository;

import com.gestion_reservas_restaurantes.backend.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}