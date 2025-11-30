package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.UserDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.User;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.UserRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        user.setCreationDate(LocalDateTime.now());
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User update(Long id, UserDTO dto) {
        User existing = getById(id);

        existing.setUsername(dto.getUsername());
        existing.setPasswordHash(dto.getPasswordHash());
        existing.setRole(dto.getRole());
        existing.setActive(dto.getActive());

        if (dto.getClientId() != null) {
            Client c = new Client();
            c.setId(dto.getClientId());
            existing.setClient(c);
        } else {
            existing.setClient(null);
        }

        return repository.save(existing);
    }
}