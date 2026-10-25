package com.galeria.service_galeria.service;

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

import com.galeria.service_galeria.model.Galeria;
import com.galeria.service_galeria.repository.GaleriaRepository;

import java.util.Optional;
@ExtendWith(MockitoExtension.class)


public class GaleriaServiceTest {

    @Mock
    private GaleriaRepository galeriaRepository;

    @InjectMocks
    private GaleriaService galeriaService;

    @Test
    @DisplayName("Debe listar todas las galerias")
    void listarTodosTest(){

        Galeria galeria = new Galeria();
        galeria.setId(1L);
        galeria.setNombre("Sesion Familiar");

        when(galeriaRepository.findAll())
                .thenReturn(Arrays.asList(galeria));

        assertEquals(1, galeriaService.listarTodos().size());
    }
    @Test
    @DisplayName("Debe buscar una galeria por ID")
    void buscarPorIdTest(){

        Galeria galeria = new Galeria();
        galeria.setId(1L);

        when(galeriaRepository.findById(1L))
                .thenReturn(Optional.of(galeria));

        Optional<Galeria> resultado = galeriaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    @DisplayName("Debe eliminar una galeria")
    void eliminarTest(){

        galeriaService.eliminar(1L);

        verify(galeriaRepository).deleteById(1L);
    }








}
