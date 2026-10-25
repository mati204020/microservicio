package com.serice.resenas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.serice.resenas.model.Modelo;
import com.serice.resenas.service.ResenasService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resenas")
@CrossOrigin(origins = "*")
public class ResenaController {

    @Autowired
    private ResenasService service;

    @GetMapping
    @Operation(summary = "Listar reseñas: ADMIN/FOTOGRAFO ven todas, CLIENTE solo las suyas")
    public List<Modelo> listar(
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (esSoloCliente(roles)) {
            return service.listarPorCliente(userId);
        }
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<?> guardar(
            @Valid @RequestBody Modelo resena,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (esSoloCliente(roles)) {
            resena.setClienteid(userId);
        }
        try {
            return ResponseEntity.ok(service.guardar(resena, roles, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID (un CLIENTE solo puede ver la suya)")
    public ResponseEntity<?> buscar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        Modelo resena = service.buscarPorId(id);
        if (resena == null) return ResponseEntity.notFound().build();
        if (!tieneAcceso(resena, roles, userId)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(resena);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reseña por ID (un CLIENTE solo puede editar la suya)")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Modelo resena,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        Modelo resenaExistente = service.buscarPorId(id);
        if (resenaExistente == null) return ResponseEntity.notFound().build();
        if (!tieneAcceso(resenaExistente, roles, userId)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        resena.setId(id);
        if (esSoloCliente(roles)) {
            resena.setClienteid(userId);
        }

        try {
            return ResponseEntity.ok(service.guardar(resena, roles, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña por ID (un CLIENTE solo puede borrar la suya)")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        Modelo resena = service.buscarPorId(id);
        if (resena == null) return ResponseEntity.notFound().build();
        if (!tieneAcceso(resena, roles, userId)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private boolean esSoloCliente(String roles) {
        return roles != null && roles.contains("CLIENTE")
                && !roles.contains("ADMINISTRADOR") && !roles.contains("FOTOGRAFO");
    }

    private boolean tieneAcceso(Modelo resena, String roles, Long userId) {
        if (roles == null) return false;
        if (roles.contains("ADMINISTRADOR") || roles.contains("FOTOGRAFO")) return true;
        return resena.getClienteid() != null && resena.getClienteid().equals(userId);
    }
}