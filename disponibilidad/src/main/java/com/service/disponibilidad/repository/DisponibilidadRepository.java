package com.service.disponibilidad.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.disponibilidad.model.DisponibilidadModelo;

@Repository
public interface DisponibilidadRepository extends JpaRepository<DisponibilidadModelo, Long>
{
    Optional<DisponibilidadModelo> findByFechaAndHoraAndFotografoId(
        LocalDate fecha, LocalTime hora, Long fotografoId
    );
}