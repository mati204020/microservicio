package com.akatellme.service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.akatellme.service.model.TipoDeusuario;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoDeusuario,Long>
{
    
    TipoDeusuario findByNombreIgnoreCase(String nombre);




    
} 


