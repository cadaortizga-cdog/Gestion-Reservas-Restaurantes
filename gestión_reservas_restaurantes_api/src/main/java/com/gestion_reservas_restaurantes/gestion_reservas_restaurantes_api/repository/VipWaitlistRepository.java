package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.VipWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VipWaitlistRepository extends JpaRepository<VipWaitlist, Long> {

    @Query(value = "SELECT COUNT(*) FROM vip_waitlist v WHERE date_trunc('minute', v.date_time) = date_trunc('minute', :dateTime)", nativeQuery = true)
    Long countByDateTime(@Param("dateTime") LocalDateTime dateTime);
}
