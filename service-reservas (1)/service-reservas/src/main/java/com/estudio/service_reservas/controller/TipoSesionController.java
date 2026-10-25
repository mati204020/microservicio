package com.estudio.service_reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudio.service_reservas.model.TipoSesion;
import com.estudio.service_reservas.repository.TipoSesionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tipos-sesion")
@Tag(name = "Tipos de Sesion", description = "Operaciones relacionadas con la gestion de tipos de sesion")
public class TipoSesionController 
{
    @Autowired
    private TipoSesionRepository tipoSesionRepository;

    @Operation(summary = "Registrar un nuevo tipo de sesion")
    @PostMapping("/guardar")
    public TipoSesion guardar(@RequestBody TipoSesion tipoSesion){
        return tipoSesionRepository.save(tipoSesion);
    }
    @Operation(summary = "Listar todos los tipos de sesion")
    @GetMapping("/listar")
    public List<TipoSesion> listar(){
        return tipoSesionRepository.findAll();

    }

}
