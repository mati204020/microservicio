package com.serice.resenas.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.serice.resenas.model.Modelo;
import com.serice.resenas.repository.ResenaRepository;
import com.serice.resenas.service.ResenasService;

import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import com.serice.resenas.service.EventoInfo;
@ExtendWith(MockitoExtension.class)
public class ResenasTest {

    @Mock
    private ResenaRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ResenasService service;

    @Test
    void listarResenasTest() {
        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setClienteid(1L);
        modelo.setEventoid(1L);
        modelo.setClasificacion(5);
        modelo.setComentario("Excelente servicio");
        modelo.setFecha(LocalDate.now());

        when(repository.findAll()).thenReturn(List.of(modelo));

        List<Modelo> resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Excelente servicio", resultado.get(0).getComentario());

        verify(repository, times(1)).findAll();
    }

    @Test
    void guardarResenaTest() {
        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setClienteid(1L);
        modelo.setEventoid(1L);
        modelo.setClasificacion(5);
        modelo.setComentario("Muy buen trabajo");
        modelo.setFecha(LocalDate.now());

        EventoInfo eventoInfo = new EventoInfo();
        eventoInfo.setId(1L);
        eventoInfo.setUsuarioId(1L);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8085/eventos/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(EventoInfo.class)).thenReturn(Mono.just(eventoInfo));

        when(repository.save(modelo)).thenReturn(modelo);

        Modelo resultado = service.guardar(modelo, "CLIENTE", 1L);

        assertNotNull(resultado);
        assertEquals("Muy buen trabajo", resultado.getComentario());
        assertEquals(5, resultado.getClasificacion());

        verify(repository, times(1)).save(modelo);
    }
    @Test
    void buscarPorIdTest() {
        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setComentario("Fotos excelentes");

        when(repository.findById(1L)).thenReturn(Optional.of(modelo));

        Modelo resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Fotos excelentes", resultado.getComentario());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void eliminarResenaTest() {
        Long id = 1L;

        service.eliminar(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void actualizarResenaTest() {
        Modelo existente = new Modelo();
        existente.setId(1L);
        existente.setClienteid(1L);
        existente.setEventoid(1L);
        existente.setClasificacion(3);
        existente.setComentario("Comentario antiguo");
        existente.setFecha(LocalDate.now());

        Modelo nuevo = new Modelo();
        nuevo.setClienteid(2L);
        nuevo.setEventoid(2L);
        nuevo.setClasificacion(5);
        nuevo.setComentario("Comentario actualizado");
        nuevo.setFecha(LocalDate.now());

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Modelo resultado = service.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Comentario actualizado", resultado.getComentario());
        assertEquals(5, resultado.getClasificacion());
        assertEquals(2L, resultado.getClienteid());
        assertEquals(2L, resultado.getEventoid());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(existente);
    }
}