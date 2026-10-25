package com.akatellme.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatellme.service.model.TipoDeusuario;
import com.akatellme.service.repository.TipoUsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario/tipo")
@CrossOrigin(origins = "*") 
public class TipoUsuarioController 
{
    @Autowired
    private TipoUsuarioRepository repository;

    @GetMapping
    public ResponseEntity<List<TipoDeusuario>> listar()
    {
        List<TipoDeusuario> tipos = repository.findAll();

        return ResponseEntity.ok(tipos);
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @Valid @RequestBody TipoDeusuario tipoDeusuario)
    {
        try {

            TipoDeusuario nuevoTipo = repository.save(tipoDeusuario);

            return new ResponseEntity<>(
                    nuevoTipo,
                    HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }
}