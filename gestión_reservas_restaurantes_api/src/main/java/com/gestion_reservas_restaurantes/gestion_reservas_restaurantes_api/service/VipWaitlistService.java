package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.VipWaitlist;

import java.util.List;

public interface VipWaitlistService {
    VipWaitlist create(VipWaitlist vipWaitlist);

    VipWaitlist update(Long id, VipWaitlist vipWaitlist);

    List<VipWaitlist> getAll();

    VipWaitlist getById(Long id);

    void delete(Long id);

    Long countByDateTime(java.time.LocalDateTime dateTime);
}