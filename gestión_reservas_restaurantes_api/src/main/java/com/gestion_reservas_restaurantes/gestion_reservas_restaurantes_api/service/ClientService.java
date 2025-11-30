package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service;


import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;

import java.util.List;

public interface ClientService {
    Client save(Client client);

    Client update(Long id, Client client);

    Client getById(Long id);

    List<Client> getAll();

    void delete(Long id);

    void addPoints(Long clientId, int pointsToAdd);
}
