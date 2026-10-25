package com.service.evento.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.evento.model.Eventoi;
import com.service.evento.repository.EventoRepository;
import com.service.evento.service.EventoService;

@ExtendWith(MockitoExtension.class)
public class EventoTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private EventoService eventoService;

    @Test
    void listarEventosTest() {
        Eventoi evento = new Eventoi();
        evento.setId(1L);
        evento.setNombre("Cumpleaños");
        evento.setLugar("Santiago");
        evento.setFecha("2026-06-16");
        evento.setUsuarioId(1L);

        when(eventoRepository.findAll()).thenReturn(List.of(evento));

        List<Eventoi> resultado = eventoService.listarTodo();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cumpleaños", resultado.get(0).getNombre());

        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorIdTest() {
        Eventoi evento = new Eventoi();
        evento.setId(1L);
        evento.setNombre("Matrimonio");

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        Optional<Eventoi> resultado = eventoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Matrimonio", resultado.get().getNombre());

        verify(eventoRepository, times(1)).findById(1L);
    }

    @Test
    void eliminarEventoTest() {
        Long id = 1L;

        eventoService.eliminar(id);

        verify(eventoRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerEventoCompletoNoExisteTest() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        Eventoi resultado = eventoService.obtenerEventoCompleto(1L);

        assertNull(resultado);

        verify(eventoRepository, times(1)).findById(1L);
    }

    @Test
    void guardarEventoTest() {
        Eventoi evento = new Eventoi();
        evento.setId(1L);
        evento.setNombre("Sesion Familiar");
        evento.setLugar("Providencia");
        evento.setFecha("2026-06-20");
        evento.setUsuarioId(1L);

        when(eventoRepository.save(evento)).thenReturn(evento);
        when(webClientBuilder.build()).thenThrow(new RuntimeException("Servicio notificaciones no disponible"));

        Eventoi resultado = eventoService.guardar(evento);

        assertNotNull(resultado);
        assertEquals("Sesion Familiar", resultado.getNombre());
        assertEquals("Providencia", resultado.getLugar());

        verify(eventoRepository, times(1)).save(evento);
        verify(webClientBuilder, times(1)).build();
    }
}