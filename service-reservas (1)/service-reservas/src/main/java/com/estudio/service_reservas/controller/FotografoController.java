package com.estudio.service_reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudio.service_reservas.model.Fotografo;
import com.estudio.service_reservas.repository.FotografoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/fotografos")
@Tag(name = "Fotografos", description = "Operaciones relacionadas con la gestion de fotografos")

public class FotografoController 
{
    @Autowired
    private FotografoRepository fotografoRepository;

    @Operation(summary = "Registrar un nuevo fotografo")
    @PostMapping("/guardar")
    public Fotografo guardar(@RequestBody Fotografo fotografo){
        return fotografoRepository.save(fotografo);
    }
    @Operation(summary = "Listar todos los fotografos")
    @GetMapping("/listar")
    public List<Fotografo> listar(){
        return fotografoRepository.findAll();
    }

}
