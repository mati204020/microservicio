package com.estudio.service_reservas.controller;

import com.estudio.service_reservas.model.Reserva;
import com.estudio.service_reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Operaciones relacionadas con la gestion de reservas")
public class ReservaController
{
    private final ReservaService reservaService;

    ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(summary = "Listar reservas: ADMIN/FOTOGRAFO ven todas, CLIENTE solo las suyas")
    @GetMapping
    public List<Reserva> listar(
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (esSoloCliente(roles)) {
            return reservaService.listarPorUsuario(userId);
        }
        return reservaService.listarTodos();
    }

    @Operation(summary = "Buscar una reserva por id (un CLIENTE solo puede ver la suya)")
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtener(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        return reservaService.buscarPorId(id)
                .filter(reserva -> tieneAcceso(reserva, roles, userId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una nueva reserva")
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Reserva reserva,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        try {
            if (esSoloCliente(roles)) {
                reserva.setUsuarioId(userId);
            }
            return ResponseEntity.ok(reservaService.guardar(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una reserva (un CLIENTE solo puede eliminar la suya)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        return reservaService.buscarPorId(id)
                .map(reserva -> {
                    if (!tieneAcceso(reserva, roles, userId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    reservaService.eliminar(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private boolean esSoloCliente(String roles) {
        return roles != null && roles.contains("CLIENTE")
                && !roles.contains("ADMINISTRADOR") && !roles.contains("FOTOGRAFO");
    }

    private boolean tieneAcceso(Reserva reserva, String roles, Long userId) {
    // Sin headers de rol = llamada interna entre microservicios (ej. service-pagos
    // verificando que la reserva existe). El Gateway siempre inyecta estos headers
    // para peticiones externas, así que su ausencia indica tráfico de confianza.
    if (roles == null) return true;
    if (roles.contains("ADMINISTRADOR") || roles.contains("FOTOGRAFO")) return true;
    return reserva.getUsuarioId() != null && reserva.getUsuarioId().equals(userId);
    }
}