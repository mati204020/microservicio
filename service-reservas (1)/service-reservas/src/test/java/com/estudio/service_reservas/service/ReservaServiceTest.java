package com.estudio.service_reservas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estudio.service_reservas.model.Reserva;
import com.estudio.service_reservas.repository.ReservaRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {
    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    @DisplayName("Debe listar todas las reservas")
    void listarTodosTest(){

        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(reservaRepository.findAll())
                .thenReturn(Arrays.asList(reserva));

        assertEquals(1, reservaService.listarTodos().size());
    }

    @Test
    @DisplayName("Debe buscar una reserva por ID")
    void buscarPorIdTest(){

        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        Optional<Reserva> resultado = reservaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    @DisplayName("Debe eliminar una reserva")
    void eliminarTest(){

        reservaService.eliminar(1L);

        verify(reservaRepository).deleteById(1L);
    }


    


    

}
