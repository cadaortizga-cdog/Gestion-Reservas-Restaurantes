package com.gestion_reservas_restaurantes.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numTable;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, length = 30)
    private String tableStatus;

    @Column(nullable = false)
    private Boolean vipExclusive = false;
}