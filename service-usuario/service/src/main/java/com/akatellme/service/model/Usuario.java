package com.akatellme.service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.Email;
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
@Schema(description = "Entidad que representa un usuario del sistema")
public class Usuario 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El run no puede estar vacío")
    @Size(min = 11, max = 12, message = "El run debe tener entre 11 y 12 caracteres")
    private String run;

    @NotBlank(message = "Los nombres no pueden estar vacíos")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String apellidos;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ingresar un correo válido")
    private String correo;

    @NotNull(message = "El tipo de usuario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_usuario_id")
    private TipoDeusuario tipoDeusuario;

    @NotNull(message = "El tipo de sesión es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_sesion_id")
    private TipoSesion tipoSesion;
}