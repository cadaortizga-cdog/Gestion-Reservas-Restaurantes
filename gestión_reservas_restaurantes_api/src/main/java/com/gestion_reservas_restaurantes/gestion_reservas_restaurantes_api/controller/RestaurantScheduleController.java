package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.controller;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.RestaurantScheduleDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper.RestaurantScheduleMapper;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.RestaurantScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class RestaurantScheduleController {

    private final RestaurantScheduleService service;

    public RestaurantScheduleController(RestaurantScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public RestaurantScheduleDTO save(@RequestBody RestaurantScheduleDTO dto) {
        return RestaurantScheduleMapper.toDTO(
                service.create(RestaurantScheduleMapper.toEntity(dto))
        );
    }

    @GetMapping
    public List<RestaurantScheduleDTO> getAll() {
        return service.getAll()
                .stream()
                .map(RestaurantScheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RestaurantScheduleDTO getById(@PathVariable Long id) {
        return RestaurantScheduleMapper.toDTO(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public RestaurantScheduleDTO update(@PathVariable Long id, @RequestBody RestaurantScheduleDTO dto) {
        return RestaurantScheduleMapper.toDTO(
                service.update(id, RestaurantScheduleMapper.toEntity(dto))
        );
    }

}

