package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable create(RestaurantTable table);

    RestaurantTable update(Long id, RestaurantTable table);

    List<RestaurantTable> getAll();

    RestaurantTable getById(Long id);

    void delete(Long id);
}
