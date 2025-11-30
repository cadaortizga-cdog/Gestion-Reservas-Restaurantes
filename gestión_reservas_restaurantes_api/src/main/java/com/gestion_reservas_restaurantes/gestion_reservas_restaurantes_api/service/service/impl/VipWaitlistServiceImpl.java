package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.VipWaitlist;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository.VipWaitlistRepository;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.VipWaitlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipWaitlistServiceImpl implements VipWaitlistService {

    private final VipWaitlistRepository repository;

    public VipWaitlistServiceImpl(VipWaitlistRepository repository) {
        this.repository = repository;
    }

    @Override
    public VipWaitlist create(VipWaitlist vipWaitlist) {
        vipWaitlist.setListStatus("en_espera");
        return repository.save(vipWaitlist);
    }

    @Override
    public List<VipWaitlist> getAll() {
        return repository.findAll();
    }

    @Override
    public VipWaitlist getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esperalista VIP no encontrada"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public VipWaitlist update(Long id, VipWaitlist vipWaitlist) {
        VipWaitlist existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esperalista VIP no encontrada"));

        existing.setClient(vipWaitlist.getClient());
        existing.setDateTime(vipWaitlist.getDateTime());
        existing.setNumPeople(vipWaitlist.getNumPeople());
        existing.setListStatus(vipWaitlist.getListStatus());

        return repository.save(existing);
    }
}