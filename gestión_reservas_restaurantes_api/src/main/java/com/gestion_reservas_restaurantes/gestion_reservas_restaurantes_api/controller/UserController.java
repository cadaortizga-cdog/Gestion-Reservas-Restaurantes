package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.controller;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.UserDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper.UserMapper;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.User;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserDTO save(@RequestBody UserDTO dto) {
        User user = UserMapper.toEntity(dto);
        return UserMapper.toDTO(service.create(user));
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return UserMapper.toDTO(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id,
            @RequestBody UserDTO dto) {

        User updated = service.update(id, dto);
        return ResponseEntity.ok(UserMapper.toDTO(updated));
    }
}
