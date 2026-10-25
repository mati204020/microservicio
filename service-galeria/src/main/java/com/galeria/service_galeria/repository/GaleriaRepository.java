package com.galeria.service_galeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galeria.service_galeria.model.Galeria;

@Repository

public interface GaleriaRepository extends JpaRepository<Galeria, Long> {
    List<Galeria> findByReservaId(Long reservaId);
    List<Galeria> findByEstado(String estado);


}
