package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantSchedule;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.RestaurantScheduleRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.RestaurantScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantScheduleServiceImpl implements RestaurantScheduleService {

    private final RestaurantScheduleRepository repository;

    public RestaurantScheduleServiceImpl(RestaurantScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public RestaurantSchedule create(RestaurantSchedule schedule) {
        return repository.save(schedule);
    }

    @Override
    public List<RestaurantSchedule> getAll() {
        return repository.findAll();
    }

    @Override
    public RestaurantSchedule getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public RestaurantSchedule update(Long id, RestaurantSchedule schedule) {
        RestaurantSchedule existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        existing.setOpenTime(schedule.getOpenTime());
        existing.setCloseTime(schedule.getCloseTime());
        existing.setDayOfWeek(schedule.getDayOfWeek());

        return repository.save(existing);
    }
}
