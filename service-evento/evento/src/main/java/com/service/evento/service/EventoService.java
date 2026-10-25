package com.service.evento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.evento.model.Eventoi;
import com.service.evento.repository.EventoRepository;

import jakarta.transaction.Transactional;

@Service
public class EventoService 
{
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Eventoi> listarTodo()
    {
        return eventoRepository.findAll();
    }

    public Optional<Eventoi> buscarPorId(Long id)
    {
        return eventoRepository.findById(id);
    }

    public Eventoi obtenerEventoCompleto(Long id)
    {
        Eventoi evento = eventoRepository.findById(id).orElse(null);

        if (evento != null) {
            return enriquecerConUsuario(evento);
        }

        return null;
    }

    @Transactional
    public Eventoi guardar(Eventoi evento)
    {
        Eventoi eventoGuardado = eventoRepository.save(evento);

        crearNotificacion(eventoGuardado);

        return eventoGuardado;
    }

    public void eliminar(Long id)
    {
        eventoRepository.deleteById(id);
    }

    private Eventoi enriquecerConUsuario(Eventoi evento)
    {
        if (evento.getUsuarioId() != null) {

            try {

                Object usuario = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8082/usuario/" + evento.getUsuarioId())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                evento.setDatosUsuario(usuario);

            } catch (Exception e) {

                evento.setDatosUsuario(e.getMessage());
            }
        }

        return evento;
    }

    private void crearNotificacion(Eventoi evento)
    {
        try {

            String notificacionJson = """
                    {
                        "mensaje": "Se ha creado o actualizado el evento: %s",
                        "tipo": "Evento",
                        "usuarioId": %d,
                        "fecha": "%s"
                    }
                    """.formatted(
                            evento.getNombre(),
                            evento.getUsuarioId(),
                            evento.getFecha()
                    );

            webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8086/notificaciones")
                    .header("Content-Type", "application/json")
                    .bodyValue(notificacionJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            System.out.println("No se pudo crear la notificación: " + e.getMessage());
        }
    }
}