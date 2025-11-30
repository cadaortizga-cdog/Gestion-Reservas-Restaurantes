package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.UserDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(Long id, UserDTO dto);

    List<User> getAll();

    User getById(Long id);

    void delete(Long id);
}