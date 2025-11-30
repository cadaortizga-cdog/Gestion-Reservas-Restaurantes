package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.UserDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.User;

public class UserMapper {

    public static UserDTO toDTO(User u) {
        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getPasswordHash(),
                u.getRole(),
                u.getActive(),
                u.getClient() != null ? u.getClient().getId() : null
        );
    }

    public static User toEntity(UserDTO dto) {
        User u = new User();

        if (dto.getClientId() != null) {
            Client c = new Client();
            c.setId(dto.getClientId());
            u.setClient(c);
        }

        u.setId(dto.getId());
        u.setUsername(dto.getUsername());
        u.setPasswordHash(dto.getPasswordHash());
        u.setRole(dto.getRole());
        u.setActive(dto.getActive());

        return u;
    }
}