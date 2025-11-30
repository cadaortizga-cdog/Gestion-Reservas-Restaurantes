package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository;


import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
