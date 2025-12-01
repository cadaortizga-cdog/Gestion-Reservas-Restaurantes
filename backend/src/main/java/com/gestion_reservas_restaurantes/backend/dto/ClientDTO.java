package com.gestion_reservas_restaurantes.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String name;
    private String phone;
    private Boolean vip;
    private Integer points;
    private String loyaltyLevel;
}
