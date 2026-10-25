package com.serice.resenas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.serice.resenas.model.Modelo;
import com.serice.resenas.service.ResenasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenasService service;

    @GetMapping
    public List<Modelo> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Modelo> guardar(@Valid @RequestBody Modelo resena) {
        return ResponseEntity.ok(service.guardar(resena));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modelo> buscar(@PathVariable Long id) {
        Modelo resena = service.buscarPorId(id);

        if (resena == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resena);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}