package com.estudio.service_reservas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa un fotografo en el sistema")

public class Fotografo 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del fotografo", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Schema(description = "Nombre del fotografo", example = "Juan Perez")
    private String nombre;

    @NotBlank(message = "La especialidad no puede estar vacia")
    @Schema(description = "Especialidad del fotografo", example = "Fotografia de bodas")
    private String especialidad;


}
