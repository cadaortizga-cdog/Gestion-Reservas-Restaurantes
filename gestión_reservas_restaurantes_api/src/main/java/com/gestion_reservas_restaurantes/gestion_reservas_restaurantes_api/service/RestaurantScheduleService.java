package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantSchedule;

import java.util.List;

public interface RestaurantScheduleService {
    RestaurantSchedule create(RestaurantSchedule schedule);

    RestaurantSchedule update(Long id, RestaurantSchedule schedule);

    RestaurantSchedule getById(Long id);

    List<RestaurantSchedule> getAll();

    void delete(Long id);
}