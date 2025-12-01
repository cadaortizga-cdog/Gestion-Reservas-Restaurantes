package com.gestion_reservas_restaurantes.backend.repository;

import com.gestion_reservas_restaurantes.backend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}