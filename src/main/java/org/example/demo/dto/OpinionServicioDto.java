package org.example.demo.dto;

import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OpinionServicioDto {
    private Integer id;
    private String comentario;
    private Integer calificacion;
    private Integer usuarioId;
    private Integer servicioId;
}