package com.akatellme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.akatellme.service.model.TipoSesion;


public interface TipoSesionRepository  extends JpaRepository<TipoSesion, Long>
{   
   TipoSesion findByNombreIgnoreCase(String nombre);
    

}
