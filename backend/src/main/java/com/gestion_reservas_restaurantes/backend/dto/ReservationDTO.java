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
public class ReservationDTO {
    private Long id;
    private Long clientId;
    private Long tableId;
    private LocalDateTime dateTime;
    private LocalDateTime endTime;
    private Integer numPeople;
    private String status;
    private LocalDateTime confirmedAt;
}