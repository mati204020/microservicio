package com.akatellme.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatellme.service.model.Usuario;
import com.akatellme.service.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*") 
public class UsuarioController 
{
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<Usuario>> listar()
    {
        List<Usuario> usuarios = usuarioService.listartodo();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario especifico por id")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id)
    {
        return usuarioService.BuscarporID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario)
    {
        try {

            Usuario nuevoUsuario = usuarioService.guardar(usuario);

            return new ResponseEntity<>(
                    nuevoUsuario,
                    HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario por ID")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario)
    {
        try {

            Usuario usuarioActualizado =
                    usuarioService.actualizar(id, usuario);

            return new ResponseEntity<>(
                    usuarioActualizado,
                    HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario por ID")
    public ResponseEntity<?> eliminar(@PathVariable Long id)
    {
        try {

            usuarioService.eliminar(id);

            return new ResponseEntity<>(
                    "Usuario eliminado correctamente",
                    HttpStatus.NO_CONTENT);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }
}