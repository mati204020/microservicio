package com.serice.resenas.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Entidad que representa una reseña realizada por un cliente a un evento")
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la reseña", example = "1")
    private Long id;

   
    @Schema(description = "ID del cliente que realiza la reseña", example = "2")
    private Long clienteid;

    @NotNull(message = "El ID del evento es obligatorio")
    @Schema(description = "ID del evento reseñado", example = "3")
    private Long eventoid;

    @NotBlank(message = "El comentario es obligatorio")
    @Schema(description = "Comentario escrito por el cliente", example = "Muy buen evento, excelente organización")
    private String comentario;

    @NotNull(message = "La clasificación debe ser obligatoria")
    @Schema(description = "Clasificación o nota entregada al evento", example = "5")
    private Integer clasificacion;

    @Schema(description = "Fecha en que se realizó la reseña", example = "2026-06-16")
    private LocalDate fecha;
}