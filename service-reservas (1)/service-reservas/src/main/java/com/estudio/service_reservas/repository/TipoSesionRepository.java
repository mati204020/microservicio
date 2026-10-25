package com.estudio.service_reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudio.service_reservas.model.TipoSesion;

@Repository

public interface TipoSesionRepository extends JpaRepository<TipoSesion, Long>
{
    TipoSesion findByNombre(String nombre);

    TipoSesion findByNombreIgnoreCase(String nombre);

}
