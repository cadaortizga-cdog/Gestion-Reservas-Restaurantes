package com.gestion_reservas_restaurantes.backend.service;

import com.gestion_reservas_restaurantes.backend.model.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable create(RestaurantTable table);

    RestaurantTable update(Long id, RestaurantTable table);

    List<RestaurantTable> getAll();

    RestaurantTable getById(Long id);

    void delete(Long id);
}
