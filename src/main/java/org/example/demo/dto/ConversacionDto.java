package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversacionDto {

    /** ID del otro usuario en la conversación */
    private int otroId;

    /** Nombre completo del otro usuario */
    private String otroNombre;

    /** Texto del último mensaje enviado */
    private String ultimoMensaje;

    /** Fecha del último mensaje */
    private LocalDateTime fechaUltimo;

    /** Cantidad de mensajes no leídos */
    private int noLeidos;
}
