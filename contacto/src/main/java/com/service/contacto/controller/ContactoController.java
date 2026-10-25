package com.service.contacto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.contacto.model.ContactoModelo;
import com.service.contacto.service.ContactoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/contacto")
@CrossOrigin(origins = "*") 
public class ContactoController 
{
    @Autowired
    private ContactoService contactoService;

    @GetMapping
    @Operation(summary = "Obtener todos los contactos")
    public List<ContactoModelo> listar() {
        return contactoService.listar();
    }

    @PostMapping
    public ResponseEntity<ContactoModelo> guardar(@Valid @RequestBody ContactoModelo contactoModelo) {
        return ResponseEntity.ok(contactoService.guardar(contactoModelo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "contacto por id")
    public ResponseEntity< ContactoModelo> buscar(@PathVariable Long id) {
        ContactoModelo contactoModelo = contactoService.buscarPorId(id);

        if (contactoModelo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(contactoModelo);
    }
    @PutMapping("/{id}")
    @Operation(summary = "actualizar contacto por id")
    public ResponseEntity<ContactoModelo> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContactoModelo contactoModelo) {

        ContactoModelo actualizado = contactoService.actualizar(id, contactoModelo);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "eliminar contacto por id")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
