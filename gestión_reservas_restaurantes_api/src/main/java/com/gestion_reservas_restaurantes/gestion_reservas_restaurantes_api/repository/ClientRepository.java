package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}