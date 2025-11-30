package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.mapper;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.dto.VipWaitlistDTO;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Client;
import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.VipWaitlist;

public class VipWaitlistMapper {

    public static VipWaitlistDTO toDTO(VipWaitlist v) {
        return new VipWaitlistDTO(
                v.getId(),
                v.getClient().getId(),
                v.getDateTime(),
                v.getNumPeople(),
                v.getListStatus()
        );
    }

    public static VipWaitlist toEntity(VipWaitlistDTO dto) {
        VipWaitlist v = new VipWaitlist();
        Client c = new Client();
        c.setId(dto.getClientId());

        v.setId(dto.getId());
        v.setClient(c);
        v.setDateTime(dto.getDateTime());
        v.setNumPeople(dto.getNumPeople());
        v.setListStatus(dto.getListStatus());

        return v;
    }
}
