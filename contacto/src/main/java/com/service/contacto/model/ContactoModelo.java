package com.service.contacto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo que representa un mensaje de contacto enviado por un cliente")
public class ContactoModelo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de la persona que envía el mensaje")
    @NotBlank
    private String nombre;

    @Schema(description = "Correo electrónico de contacto")
    @Email
    @NotBlank
    private String correo;

    @Schema(description = "Teléfono de contacto")
    @NotBlank
    private String telefono;

    @Schema(description = "Asunto del mensaje")
    @NotBlank
    private String asunto;

    @Schema(description = "Mensaje enviado por el cliente")
    @NotBlank
    private String mensaje;



}
