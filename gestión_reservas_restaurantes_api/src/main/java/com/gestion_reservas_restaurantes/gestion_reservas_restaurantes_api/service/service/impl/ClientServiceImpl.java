package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.ClientRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = getById(id);
        existing.setName(client.getName());
        existing.setPhone(client.getPhone());
        existing.setVip(client.getVip());
        existing.setPoints(client.getPoints());
        existing.setLoyaltyLevel(client.getLoyaltyLevel());
        return clientRepository.save(existing);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
}