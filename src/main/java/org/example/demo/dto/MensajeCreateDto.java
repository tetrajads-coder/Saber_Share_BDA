package org.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensajeCreateDto {

    @NotNull(message = "El emisor es obligatorio")
    private Integer emisorId;

    @NotNull(message = "El receptor es obligatorio")
    private Integer receptorId;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String contenido;
}
