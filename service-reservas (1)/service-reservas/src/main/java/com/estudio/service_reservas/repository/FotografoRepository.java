package com.estudio.service_reservas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudio.service_reservas.model.Fotografo;

@Repository

public interface FotografoRepository extends JpaRepository<Fotografo, Long>
{
    Fotografo findByNombre(String nombre);

    List<Fotografo> findByEspecialidad(String especialidad);

}
