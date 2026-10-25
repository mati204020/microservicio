package com.galeria.service_galeria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.galeria.service_galeria.model.Galeria;
import com.galeria.service_galeria.service.GaleriaService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/galerias")
@Tag(name = "Galerias", description = "Operaciones relacionadas con la gestion de galerias")
public class GaleriaController {

    @Autowired
    private GaleriaService galeriaService;

    @Operation(summary = "Listar todas las galerias")
    @GetMapping
    public List<Galeria> listar(){
        return galeriaService.listarTodos();
    }

    @Operation(summary = "Buscar una galeria por id")
    @GetMapping("/{id}")
    public ResponseEntity<Galeria> obtener(@PathVariable Long id){
        return galeriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una nueva galeria")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Galeria galeria){
        try{
            return ResponseEntity.ok(galeriaService.guardar(galeria));
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una galeria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        galeriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }



}
