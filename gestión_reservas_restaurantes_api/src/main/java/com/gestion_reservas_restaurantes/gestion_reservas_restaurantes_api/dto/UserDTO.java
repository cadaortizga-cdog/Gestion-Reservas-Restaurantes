package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String passwordHash;
    private String role;
    private Boolean active;
    private Long clientId;
}
