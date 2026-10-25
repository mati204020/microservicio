package com.service.disponibilidad.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa los horarios disponibles para las sesiones fotográficas")

public class DisponibilidadModelo 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la disponibilidad", example = "1")
    private Long id;

    @Schema(description = "Fecha de la disponibilidad", example = "2026-06-20")
    private LocalDate fecha;

    @Schema(description = "Hora de la disponibilidad", example = "10:30")
    private LocalTime hora;

    @Schema(description = "Estado de la disponibilidad", example = "Disponible")
    private String estado;

    @Column(name = "fotografo_id")
    @Schema(description = "ID del fotografo al que pertenece este horario", example = "1")
    private Long fotografoId;
}