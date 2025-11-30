package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.controller;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.ReservationDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper.ReservationMapper;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.ReservationService;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl.ReservationServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;
    private final ReservationServiceImpl serviceImpl;

    public ReservationController(ReservationService service, ReservationServiceImpl serviceImpl) {
        this.service = service;
        this.serviceImpl = serviceImpl;
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

    @GetMapping("/active")
    public List<ReservationDTO> getActive() {
        return serviceImpl.getAllReservations().stream()
                .filter(r -> "pendiente".equals(r.getStatus()) || "confirmada".equals(r.getStatus()))
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

    @PutMapping("/{id}/confirm")
    public ReservationDTO confirm(@PathVariable Long id) {
        return ReservationMapper.toDTO(serviceImpl.confirmReservation(id));
    }

    @GetMapping("/available-tables")
    public List<Long> availableTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam Integer numPeople,
            @RequestParam(required = false, defaultValue = "false") Boolean vipOnly
    ) {
        List<RestaurantTable> tables = serviceImpl.findAvailableTables(start, end, numPeople, vipOnly);
        return tables.stream().map(RestaurantTable::getId).collect(Collectors.toList());
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finishReservation(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(serviceImpl.finishReservation(id));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
