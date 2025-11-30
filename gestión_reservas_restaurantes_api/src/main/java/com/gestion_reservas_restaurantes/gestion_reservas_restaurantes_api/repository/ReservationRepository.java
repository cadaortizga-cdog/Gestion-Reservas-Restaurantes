package com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.repository;

import com.gestion_reservas_restaurantes.gestion_reservas_restaurantes_api.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT * FROM reservation r " +
            "WHERE r.table_id = :tableId " +
            "AND r.status <> 'cancelada' " +
            "AND ( " +
            "  (:start BETWEEN r.date_time AND r.end_time) OR " +
            "  (:end BETWEEN r.date_time AND r.end_time) OR " +
            "  (r.date_time BETWEEN :start AND :end) " +
            ")", nativeQuery = true)
    List<Reservation> findConflicts(
            @Param("tableId") Long tableId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "SELECT COUNT(*) FROM reservation r WHERE r.status = 'pendiente' AND date_trunc('minute', r.date_time) = date_trunc('minute', :dateTime)", nativeQuery = true)
    Long countPendingByDateTime(@Param("dateTime") LocalDateTime dateTime);

    @Query(value = "SELECT * FROM reservation r WHERE r.status IN ('pendiente','confirmada')", nativeQuery = true)
    List<Reservation> findActiveReservations();
}
