package com.galeria.service_galeria.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que no representa una galeria en el sistema")

public class Galeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de lagaleria", example = "1")
    private Long id;

    

    @NotBlank(message = "El nombre de la galeria no puede estar vacio")
    @Schema(description = "Nombre de la galeria", example = "Sesion Familiar")
    private String nombre;

    @Schema(description = "Estado de la galeria", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Fecha de creacion de la galeria", example = "2026-06-16")
    private LocalDate fechaCreacion;

    @NotNull(message = "La reserva es obligatoria")
    @Positive(message = "El id de reserva debe ser mayor a 0")
    @Schema(description = "ID de la reserva asociada", example = "1")
    private Long reservaId;



}
