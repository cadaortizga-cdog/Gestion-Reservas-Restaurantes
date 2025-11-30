package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.controller;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.ReservationDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper.ReservationMapper;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ReservationDTO save(@RequestBody ReservationDTO dto) {
        return ReservationMapper.toDTO(
                service.createReservation(ReservationMapper.toEntity(dto))
        );
    }

    @PutMapping("/{id}")
    public ReservationDTO update(@PathVariable Long id, @RequestBody ReservationDTO dto) {
        return ReservationMapper.toDTO(
                service.updateReservation(id, ReservationMapper.toEntity(dto))
        );
    }

    @GetMapping
    public List<ReservationDTO> getAll() {
        return service.getAllReservations()
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReservationDTO getById(@PathVariable Long id) {
        return ReservationMapper.toDTO(service.getReservationById(id));
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        service.cancelReservation(id);
    }
}
