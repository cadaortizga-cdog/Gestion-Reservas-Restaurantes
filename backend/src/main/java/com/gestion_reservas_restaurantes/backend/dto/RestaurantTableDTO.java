package com.gestion_reservas_restaurantes.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableDTO {
    private Long id;
    private Integer numTable;
    private Integer capacity;
    private String tableStatus;
    private Boolean vipExclusive;
}