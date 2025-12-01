package com.gestion_reservas_restaurantes.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "restaurant_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer dayOfWeek;
}
