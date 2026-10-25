package com.estudio.service_pagos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PagoDTO {
    private Long id;
    private Double monto;
    private String metodoPago;

}
