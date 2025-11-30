package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.controller;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.RestaurantTableDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper.RestaurantTableMapper;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.RestaurantTableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tables")
public class RestaurantTableController {

    private final RestaurantTableService service;

    public RestaurantTableController(RestaurantTableService service) {
        this.service = service;
    }

    @PostMapping
    public RestaurantTableDTO save(@RequestBody RestaurantTableDTO dto) {
        return RestaurantTableMapper.toDTO(
                service.create(RestaurantTableMapper.toEntity(dto))
        );
    }

    @PutMapping("/{id}")
    public RestaurantTableDTO update(@PathVariable Long id, @RequestBody RestaurantTableDTO dto) {
        return RestaurantTableMapper.toDTO(
                service.update(id, RestaurantTableMapper.toEntity(dto))
        );
    }

    @GetMapping
    public List<RestaurantTableDTO> getAll() {
        return service.getAll()
                .stream()
                .map(RestaurantTableMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RestaurantTableDTO getById(@PathVariable Long id) {
        return RestaurantTableMapper.toDTO(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
