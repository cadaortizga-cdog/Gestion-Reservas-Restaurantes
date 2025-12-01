package com.gestion_reservas_restaurantes.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VipWaitlistDTO {
    private Long id;
    private Long clientId;
    private LocalDateTime dateTime;
    private Integer numPeople;
    private String listStatus;
}
