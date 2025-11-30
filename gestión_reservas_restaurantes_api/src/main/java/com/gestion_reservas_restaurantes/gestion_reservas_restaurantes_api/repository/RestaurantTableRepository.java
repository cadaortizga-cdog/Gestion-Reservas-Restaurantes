package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}