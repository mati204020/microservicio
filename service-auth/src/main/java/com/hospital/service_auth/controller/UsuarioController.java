package com.hospital.service_auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.service_auth.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth/usuarios")
@Tag(name = "Usuarios (auth)", description = "Consulta interna de existencia de cuentas, usada por otros microservicios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Verifica si existe una cuenta con ese id")
    @GetMapping("/{id}")
    public ResponseEntity<Void> existe(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}