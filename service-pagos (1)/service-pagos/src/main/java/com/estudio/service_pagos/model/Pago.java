package com.estudio.service_pagos.model;

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
@Schema(description = "Modelo que representa el pago en el sistema")

public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "identeificador del pago", example = "1")
    private Long id;

    @NotNull(message = "El monto no puede estar vacio")
    @Positive(message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto del pago", example = "100000")
    private Double monto;

    @NotBlank(message = "El metodo de pago no puede estar vacio")
    @Schema(description = "Metodo de pago", example = "debito")
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacio")
    @Schema(description = "Estado del pago", example = "PAGADO")
    private String estado;
    
    @NotNull(message = "La fecha de pago no puede estar vacia")
    @Schema(description = "Fecha del pago", example = "2025-10-01")
    private LocalDate fechaPago;

    @NotNull(message = "La reserva es obligatoria")
    @Positive(message = "El id de reserva debe ser mayor a 0")
    @Schema(description = "ID de reserva", example = "1")
    private Long reservaId;



}
