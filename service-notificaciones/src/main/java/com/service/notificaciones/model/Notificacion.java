package com.service.notificaciones.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
@Schema(description = "Modelo que representa las notificaciones")
public class Notificacion 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Mensaje que llegara luego de recibir notificacion")
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 5, max = 255,
           message = "El mensaje debe tener entre 5 y 255 caracteres")
    private String mensaje;
    @Schema(description = "Tipo de evento")    
    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String tipo;
    @Schema(description = "Codigo del usuario con quien sera el evento")
    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    private String fecha;
}