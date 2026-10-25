package com.estudio.service_reservas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo que representa un tipo de sesion fotografica")

public class TipoSesion 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del tipo de sesion", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Schema(description = "Nombre del tipo de sesion", example = "Sesion Familiar")
    private String nombre;

    @Positive(message = "La duracion debe ser mayor a 0")
    @Schema(description = "Duracion de la sesion en minutos", example = "60")
    private int duracion;

    @Positive(message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio de la sesion", example = "50000")
    private double precio;


}
