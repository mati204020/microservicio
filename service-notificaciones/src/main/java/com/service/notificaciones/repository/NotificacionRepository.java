package com.service.notificaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.notificaciones.model.Notificacion;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> 
{
    List<Notificacion> findByUsuarioId(Long usuarioId);
}