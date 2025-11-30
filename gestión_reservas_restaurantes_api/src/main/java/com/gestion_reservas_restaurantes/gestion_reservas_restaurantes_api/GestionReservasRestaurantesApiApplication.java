package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionReservasRestaurantesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionReservasRestaurantesApiApplication.class, args);
    }
}
