package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.RestaurantTableDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.RestaurantTable;

public class RestaurantTableMapper {

    public static RestaurantTable toEntity(RestaurantTableDTO dto) {
        RestaurantTable t = new RestaurantTable();
        t.setId(dto.getId());
        t.setNumTable(dto.getNumTable());
        t.setCapacity(dto.getCapacity());
        t.setTableStatus(dto.getTableStatus());
        t.setVipExclusive(dto.getVipExclusive());
        return t;
    }

    public static RestaurantTableDTO toDTO(RestaurantTable t) {
        return new RestaurantTableDTO(
                t.getId(),
                t.getNumTable(),
                t.getCapacity(),
                t.getTableStatus(),
                t.getVipExclusive()
        );
    }
}