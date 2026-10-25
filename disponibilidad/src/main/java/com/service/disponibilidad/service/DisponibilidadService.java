package com.service.disponibilidad.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.disponibilidad.model.DisponibilidadModelo;
import com.service.disponibilidad.repository.DisponibilidadRepository;

@Service
public class DisponibilidadService 
{
    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    public List<DisponibilidadModelo> obtenerDisponibilidades() {
        return disponibilidadRepository.findAll();
    }

    public DisponibilidadModelo obtenerDisponibilidadPorId(Long id) {
        return disponibilidadRepository.findById(id).get();
    }

    public DisponibilidadModelo guardarDisponibilidad(DisponibilidadModelo disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

    public DisponibilidadModelo actualizarDisponibilidad(Long id, DisponibilidadModelo disponibilidad) {
        DisponibilidadModelo disponibilidadActual = disponibilidadRepository.findById(id).get();

        disponibilidadActual.setFecha(disponibilidad.getFecha());
        disponibilidadActual.setHora(disponibilidad.getHora());
        disponibilidadActual.setEstado(disponibilidad.getEstado());

        return disponibilidadRepository.save(disponibilidadActual);
    }

    public void eliminarDisponibilidad(Long id) {
        disponibilidadRepository.deleteById(id);
    }

    // ===== NUEVO: usado por service-reservas =====

    /**
     * Revisa si existe un horario "Disponible" para ese fotógrafo, fecha y hora.
     */
    public boolean estaDisponible(LocalDate fecha, LocalTime hora, Long fotografoId) {
        Optional<DisponibilidadModelo> registro =
            disponibilidadRepository.findByFechaAndHoraAndFotografoId(fecha, hora, fotografoId);

        return registro.isPresent() && "Disponible".equalsIgnoreCase(registro.get().getEstado());
    }

    /**
     * Marca como "Reservado" el horario correspondiente, una vez que la reserva se creó.
     */
    public void marcarComoReservado(LocalDate fecha, LocalTime hora, Long fotografoId) {
        disponibilidadRepository.findByFechaAndHoraAndFotografoId(fecha, hora, fotografoId)
            .ifPresent(disponibilidad -> {
                disponibilidad.setEstado("Reservado");
                disponibilidadRepository.save(disponibilidad);
            });
    }
}