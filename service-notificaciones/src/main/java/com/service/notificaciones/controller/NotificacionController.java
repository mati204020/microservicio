package com.service.notificaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.service.notificaciones.model.Notificacion;
import com.service.notificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/notificaciones")
@CrossOrigin(origins = "*")
@Tag(name = "Gestión de Notificaciones", description = "Permite administrar las notificaciones del sistema")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    @Operation(summary = "Listar notificaciones: ADMIN/FOTOGRAFO ven todas, CLIENTE solo las suyas")
    public ResponseEntity<List<Notificacion>> listar(
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        List<Notificacion> notificaciones = esSoloCliente(roles)
                ? notificacionService.listarPorUsuario(userId)
                : notificacionService.listarTodo();

        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar una notificación por ID (un CLIENTE solo puede ver la suya)")
    public ResponseEntity<Notificacion> buscar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        return notificacionService.buscarPorId(id)
                .filter(n -> tieneAcceso(n, roles, userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva notificación")
    public ResponseEntity<?> guardar(@Valid @RequestBody Notificacion notificacion) {
        try {
            Notificacion nuevaNotificacion = notificacionService.guardar(notificacion);
            return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una notificación por ID")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Notificacion notificacion) {
        try {
            return notificacionService.buscarPorId(id)
                    .map(n -> {
                        notificacion.setId(id);
                        return new ResponseEntity<>(notificacionService.guardar(notificacion), HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una notificación por ID")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            notificacionService.eliminar(id);
            return ResponseEntity.ok("Notificación eliminada correctamente");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private boolean esSoloCliente(String roles) {
        return roles != null && roles.contains("CLIENTE")
                && !roles.contains("ADMINISTRADOR") && !roles.contains("FOTOGRAFO");
    }

    private boolean tieneAcceso(Notificacion n, String roles, Long userId) {
        if (roles == null) return false;
        if (roles.contains("ADMINISTRADOR") || roles.contains("FOTOGRAFO")) return true;
        return n.getUsuarioId() != null && n.getUsuarioId().equals(userId);
    }
}