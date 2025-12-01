package com.gestion_reservas_restaurantes.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantScheduleDTO {
    private Long id;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer dayOfWeek;
}
