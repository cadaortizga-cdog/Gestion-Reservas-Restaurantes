package com.gestion_reservas_restaurantes.backend.mapper;

import com.gestion_reservas_restaurantes.backend.dto.RestaurantScheduleDTO;
import com.gestion_reservas_restaurantes.backend.model.RestaurantSchedule;

public class RestaurantScheduleMapper {

    public static RestaurantScheduleDTO toDTO(RestaurantSchedule s) {
        return new RestaurantScheduleDTO(
                s.getId(),
                s.getOpenTime(),
                s.getCloseTime(),
                s.getDayOfWeek()
        );
    }

    public static RestaurantSchedule toEntity(RestaurantScheduleDTO dto) {
        RestaurantSchedule s = new RestaurantSchedule();
        s.setId(dto.getId());
        s.setOpenTime(dto.getOpenTime());
        s.setCloseTime(dto.getCloseTime());
        s.setDayOfWeek(dto.getDayOfWeek());
        return s;
    }
}
