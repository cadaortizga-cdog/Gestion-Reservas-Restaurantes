package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.ClientDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;

public class ClientMapper {

    public static Client toEntity(ClientDTO dto) {
        Client c = new Client();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setPhone(dto.getPhone());
        c.setVip(dto.getVip());
        c.setPoints(dto.getPoints());
        c.setLoyaltyLevel(dto.getLoyaltyLevel());
        return c;
    }

    public static ClientDTO toDTO(Client c) {
        return new ClientDTO(
                c.getId(),
                c.getName(),
                c.getPhone(),
                c.getVip(),
                c.getPoints(),
                c.getLoyaltyLevel()
        );
    }
}
