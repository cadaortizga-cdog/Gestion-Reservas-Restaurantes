package com.gestion_reservas_restaurantes.backend.controller;

import com.gestion_reservas_restaurantes.backend.dto.ClientDTO;
import com.gestion_reservas_restaurantes.backend.mapper.ClientMapper;
import com.gestion_reservas_restaurantes.backend.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ClientDTO save(@RequestBody ClientDTO dto) {
        return ClientMapper.toDTO(
                service.save(ClientMapper.toEntity(dto))
        );
    }

    @PutMapping("/{id}")
    public ClientDTO update(@PathVariable Long id, @RequestBody ClientDTO dto) {
        return ClientMapper.toDTO(
                service.update(id, ClientMapper.toEntity(dto))
        );
    }

    @GetMapping
    public List<ClientDTO> getAll() {
        return service.getAll()
                .stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClientDTO getById(@PathVariable Long id) {
        return ClientMapper.toDTO(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
