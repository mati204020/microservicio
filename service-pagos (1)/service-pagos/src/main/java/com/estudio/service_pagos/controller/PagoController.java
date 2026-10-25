package com.estudio.service_pagos.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estudio.service_pagos.model.Pago;
import com.estudio.service_pagos.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Operaciones relacionadas con la gestion de pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @Operation(summary = "listar todos los pagos")

    @GetMapping
    public List<Pago> listar(){
        return pagoService.listarTodos();
    }


    @Operation(summary = "buscar un pago por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtener(@PathVariable Long id){
        return pagoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Registrar un nuevo pago")
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Pago pago,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestHeader(value = "X-User-Id", required = false) Long userId){
        try{
            return ResponseEntity.ok(pagoService.guardar(pago, roles, userId));
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
            
        }
    }

    @Operation(summary = "Eliminar un pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        pagoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}