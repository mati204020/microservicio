package com.hospital.service_auth.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.service_auth.model.Rol;
import com.hospital.service_auth.model.Usuario;
import com.hospital.service_auth.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth/perfil")
@Tag(name = "Perfil", description = "Perfil del usuario autenticado")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Perfil del usuario autenticado")
    @GetMapping
    public ResponseEntity<?> perfil(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se pudo identificar al usuario autenticado.");
        }

        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombreRol)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PerfilResponse(usuario.getId(), usuario.getNombreUsuario(), usuario.getCorreo(), roles));
    }

    private record PerfilResponse(Long id, String nombreUsuario, String correo, List<String> roles) {}
}