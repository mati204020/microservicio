package com.service.evento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.service.evento.model.Eventoi;
import com.service.evento.service.EventoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*") 
public class EventoController 
{
    @Autowired
    private EventoService eventoService;

    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones")
    public ResponseEntity<List<Eventoi>> listar()
    {
        List<Eventoi> eventos = eventoService.listarTodo();

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un evento especifico")
    public ResponseEntity<Eventoi> buscar(@PathVariable Long id)
    {
        Eventoi evento = eventoService.obtenerEventoCompleto(id);

        if (evento != null) {
            return ResponseEntity.ok(evento);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> guardar(
            @Valid @RequestBody Eventoi evento)
    {
        try {

            Eventoi nuevoEvento =
                    eventoService.guardar(evento);

            return new ResponseEntity<>(
                    nuevoEvento,
                    HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un evento")
    public ResponseEntity<?> actualizarEvento(
            @PathVariable Long id,
            @Valid @RequestBody Eventoi evento)
    {
        try {

            return eventoService.buscarPorId(id)
                    .map(e -> {

                        evento.setId(id);

                        return new ResponseEntity<>(
                                eventoService.guardar(evento),
                                HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(
                            HttpStatus.NOT_FOUND));

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un evento especifico")
    public ResponseEntity<?> eliminar(@PathVariable Long id)
    {
        try {

            eventoService.eliminar(id);

            return new ResponseEntity<>(
                    "Evento eliminado correctamente",
                    HttpStatus.NO_CONTENT);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }
}