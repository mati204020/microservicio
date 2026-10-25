package com.service.contacto.testcontacto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.service.contacto.model.ContactoModelo;
import com.service.contacto.repository.ContactoRepository;
import com.service.contacto.service.ContactoService;

@ExtendWith(MockitoExtension.class)
public class ContactoTest 
{
    @Mock
    private ContactoRepository contactoRepository;

    @InjectMocks
    private ContactoService contactoService;

    @Test
    void listarContactosTest() {
        ContactoModelo contacto = new ContactoModelo();
        contacto.setId(1L);
        contacto.setNombre("Juan Perez");

        when(contactoRepository.findAll()).thenReturn(List.of(contacto));

        List<ContactoModelo> resultado = contactoService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombre());

        verify(contactoRepository, times(1)).findAll();
    }
    @Test
    void guardarContactoTest() {
        ContactoModelo contacto = new ContactoModelo();
        contacto.setId(1L);
        contacto.setNombre("Juan Perez");
        contacto.setCorreo("juan@gmail.com");
        contacto.setTelefono("912345678");
        contacto.setAsunto("Consulta");
        contacto.setMensaje("Necesito cotizar");

        when(contactoRepository.save(contacto)).thenReturn(contacto);

        ContactoModelo resultado = contactoService.guardar(contacto);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());

        verify(contactoRepository, times(1)).save(contacto);
    }
    @Test
    void buscarPorIdTest() {
        ContactoModelo contacto = new ContactoModelo();
        contacto.setId(1L);
        contacto.setNombre("Camila Soto");

        when(contactoRepository.findById(1L)).thenReturn(Optional.of(contacto));

        ContactoModelo resultado = contactoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Camila Soto", resultado.getNombre());

        verify(contactoRepository, times(1)).findById(1L);
    }
    @Test
    void eliminarContactoTest() {
        Long id = 1L;

        contactoService.eliminar(id);

        verify(contactoRepository, times(1)).deleteById(id);
    }
    @Test
    void actualizarContactoTest() {
        ContactoModelo existente = new ContactoModelo();
        existente.setId(1L);
        existente.setNombre("Nombre antiguo");

        ContactoModelo nuevo = new ContactoModelo();
        nuevo.setNombre("Nombre nuevo");
        nuevo.setCorreo("nuevo@gmail.com");
        nuevo.setTelefono("999999999");
        nuevo.setAsunto("Nuevo asunto");
        nuevo.setMensaje("Nuevo mensaje");

        when(contactoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(contactoRepository.save(existente)).thenReturn(existente);

        ContactoModelo resultado = contactoService.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Nombre nuevo", resultado.getNombre());
        assertEquals("nuevo@gmail.com", resultado.getCorreo());

        verify(contactoRepository, times(1)).findById(1L);
        verify(contactoRepository, times(1)).save(existente);
    }

}
