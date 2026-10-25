package com.akatellme.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatellme.service.model.TipoSesion;
import com.akatellme.service.repository.TipoSesionRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/sesion")
@CrossOrigin(origins = "*") 
public class TiposesionController 
{
    @Autowired
    private TipoSesionRepository tipoSesionRepository;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarSesion(
            @Valid @RequestBody TipoSesion tipoSesion)
    {
        try {

            TipoSesion nuevaSesion =
                    tipoSesionRepository.save(tipoSesion);

            return new ResponseEntity<>(
                    nuevaSesion,
                    HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TipoSesion>> listarsesiones()
    {
        List<TipoSesion> sesiones =
                tipoSesionRepository.findAll();

        return ResponseEntity.ok(sesiones);
    }
}