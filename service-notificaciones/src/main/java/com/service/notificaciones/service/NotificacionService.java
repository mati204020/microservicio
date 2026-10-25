package com.service.notificaciones.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.notificaciones.model.Notificacion;
import com.service.notificaciones.repository.NotificacionRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificacionService 
{
    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarTodo()
    {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> buscarPorId(Long id)
    {
        return notificacionRepository.findById(id);
    }

    @Transactional
    public Notificacion guardar(Notificacion notificacion)
    {
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void eliminar(Long id)
    {
        notificacionRepository.deleteById(id);
    }

    public List<Notificacion> listarPorUsuario(Long usuarioId)
    {
    return notificacionRepository.findByUsuarioId(usuarioId);
    }
}