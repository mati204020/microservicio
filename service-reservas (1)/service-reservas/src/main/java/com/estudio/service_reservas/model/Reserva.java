package com.estudio.service_reservas.model;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Schema(description = "Modelo que representa una reserva en el sistema")
public class Reserva 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la reserva", example = "1")
    private Long id;

    @NotNull(message = "La fecha no puede estar vacia")
    @Schema(description = "Fecha de la reserva", example = "2026-06-17")
    private LocalDate fecha;

    @NotNull(message = "La hora no puede estar vacia")
    @Schema(description = "Hora de la reserva", example = "15:30:00")
    private LocalTime hora;

    @NotBlank(message = "El estado no puede estar vacio")
    @Schema(description = "Estado de la reserva", example = "CONFIRMADA")
    private String estado;

    @NotNull(message = "Debe seleccionar un tipo de sesion")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_sesion_id")
    @Schema(description = "Tipo de sesion asociada a la reserva")
    private TipoSesion tipoSesion;

    
    @NotNull(message = "Debe seleccionar un fotografo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fotografo_id")
    @Schema(description = "Fotografo asignado a la reserva")
    private Fotografo fotografo;

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El id del usuario debe ser mayor a 0")
    @Schema(description = "ID del usuario que realiza la reserva", example = "5")
    private Long usuarioId;

    








}
