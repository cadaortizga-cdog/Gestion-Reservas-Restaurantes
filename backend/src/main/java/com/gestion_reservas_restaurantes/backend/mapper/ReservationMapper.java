package com.gestion_reservas_restaurantes.backend.mapper;

import com.gestion_reservas_restaurantes.backend.dto.ReservationDTO;
import com.gestion_reservas_restaurantes.backend.model.Client;
import com.gestion_reservas_restaurantes.backend.model.Reservation;
import com.gestion_reservas_restaurantes.backend.model.RestaurantTable;

public class ReservationMapper {

    public static Reservation toEntity(ReservationDTO dto) {
        Reservation r = new Reservation();
        r.setId(dto.getId());

        Client c = new Client();
        c.setId(dto.getClientId());
        r.setClient(c);

        RestaurantTable t = new RestaurantTable();
        t.setId(dto.getTableId());
        r.setTable(t);

        r.setDateTime(dto.getDateTime());
        r.setEndTime(dto.getEndTime());
        r.setNumPeople(dto.getNumPeople());
        r.setStatus(dto.getStatus());
        r.setConfirmedAt(dto.getConfirmedAt());

        return r;
    }

    public static ReservationDTO toDTO(Reservation r) {
        return new ReservationDTO(
                r.getId(),
                r.getClient().getId(),
                r.getTable().getId(),
                r.getDateTime(),
                r.getEndTime(),
                r.getNumPeople(),
                r.getStatus(),
                r.getConfirmedAt()
        );
    }
}
