package com.gestion_reservas_restaurantes.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vip_waitlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VipWaitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDateTime dateTime;
    private Integer numPeople;
    private String listStatus;
}
