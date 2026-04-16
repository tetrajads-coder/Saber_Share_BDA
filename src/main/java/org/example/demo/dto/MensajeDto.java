package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeDto {

    private Integer idMensaje;
    private String contenido;
    private LocalDateTime fechaEnvio;

    // Datos del emisor
    private Integer emisorId;
    private String emisorNombre;

    // Datos del receptor
    private Integer receptorId;
    private String receptorNombre;

    private Boolean leido;
}
