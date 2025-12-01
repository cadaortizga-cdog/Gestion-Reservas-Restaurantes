package com.gestion_reservas_restaurantes.backend.service.service.impl;

import com.gestion_reservas_restaurantes.backend.model.RestaurantTable;
import com.gestion_reservas_restaurantes.backend.repository.RestaurantTableRepository;
import com.gestion_reservas_restaurantes.backend.service.RestaurantTableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository repository;

    public RestaurantTableServiceImpl(RestaurantTableRepository repository) {
        this.repository = repository;
    }

    @Override
    public RestaurantTable create(RestaurantTable table) {
        table.setTableStatus("disponible");
        return repository.save(table);
    }

    @Override
    public RestaurantTable update(Long id, RestaurantTable table) {
        RestaurantTable existing = getById(id);
        existing.setNumTable(table.getNumTable());
        existing.setCapacity(table.getCapacity());
        existing.setTableStatus(table.getTableStatus());
        existing.setVipExclusive(table.getVipExclusive());
        return repository.save(existing);
    }

    @Override
    public List<RestaurantTable> getAll() {
        return repository.findAll();
    }

    @Override
    public RestaurantTable getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
