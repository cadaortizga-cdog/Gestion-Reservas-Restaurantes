package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.scheduling;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.service.service.impl.ReservationServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatusScheduler {

    private final ReservationServiceImpl reservationService;

    public StatusScheduler(ReservationServiceImpl reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void runRefresh() {
        try {
            reservationService.refreshStatuses();
        } catch (Exception ex) {
            System.err.println("Error al ejecutar refreshStatuses: " + ex.getMessage());
        }
    }
}
