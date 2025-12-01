package com.gestion_reservas_restaurantes.backend.controller;

import com.gestion_reservas_restaurantes.backend.dto.VipWaitlistDTO;
import com.gestion_reservas_restaurantes.backend.mapper.VipWaitlistMapper;
import com.gestion_reservas_restaurantes.backend.model.VipWaitlist;
import com.gestion_reservas_restaurantes.backend.service.VipWaitlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vip-waitlist")
public class VipWaitlistController {

    private final VipWaitlistService service;

    public VipWaitlistController(VipWaitlistService service) {
        this.service = service;
    }

    @PostMapping
    public VipWaitlistDTO save(@RequestBody VipWaitlistDTO dto) {
        VipWaitlist v = VipWaitlistMapper.toEntity(dto);
        return VipWaitlistMapper.toDTO(service.create(v));
    }

    @GetMapping
    public List<VipWaitlistDTO> getAll() {
        return service.getAll()
                .stream()
                .map(VipWaitlistMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public VipWaitlistDTO getById(@PathVariable Long id) {
        return VipWaitlistMapper.toDTO(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public VipWaitlistDTO update(@PathVariable Long id, @RequestBody VipWaitlistDTO dto) {
        return VipWaitlistMapper.toDTO(service.update(id, VipWaitlistMapper.toEntity(dto)));
    }

}