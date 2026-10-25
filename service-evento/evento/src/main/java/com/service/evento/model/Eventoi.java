package com.service.evento.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa un mensaje de contacto enviado por un cliente")
public class Eventoi 
{
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del evento", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 3, max = 100,
           message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre del evento", example = "Urban Fest")
    private String nombre;

    @NotBlank(message = "El lugar es obligatorio")
    @Size(min = 3, max = 100,
           message = "El lugar debe tener entre 3 y 100 caracteres")
    @Schema(description = "Lugar donde se realizará el evento", example = "Santiago")
    private String lugar;

    @NotBlank(message = "La fecha es obligatoria")
    @Schema(description = "Fecha del evento", example = "2026-06-20")
    private String fecha;

    @NotNull(message = "El usuario es obligatorio")
    @Schema(description = "ID del usuario asociado al evento", example = "2")
    private Long usuarioId;

    @Transient
    @Schema(description = "Datos del usuario obtenidos desde el microservicio de usuarios")
    private Object datosUsuario;
}