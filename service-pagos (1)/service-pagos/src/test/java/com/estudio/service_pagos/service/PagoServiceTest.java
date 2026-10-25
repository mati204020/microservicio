package com.estudio.service_pagos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

import com.estudio.service_pagos.model.Pago;
import com.estudio.service_pagos.repository.PagoRepository;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)



public class PagoServiceTest {
    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    @DisplayName("Debe listar todos los pagos")
    void listarTodosTest(){
        Pago pago = new Pago();
        pago.setId(1l);
        pago.setMonto(200000.0);

        when(pagoRepository.findAll())
                .thenReturn(Arrays.asList(pago));
        assertEquals(1, pagoService.listarTodos().size());


    }

    @Test
    @DisplayName("Debe buscar un pago por ID")
    void buscarPorIdTest(){
        Pago pago = new Pago();
        pago.setId(1L);

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));
        Optional<Pago> resultado = pagoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L,resultado.get().getId());
    }
    @Test
    @DisplayName("Debe eliminar un pago")
    void eliminarTest(){
        pagoService.eliminar(1L);

        verify(pagoRepository).deleteById(1L);
    }
}

   
       



        
    

