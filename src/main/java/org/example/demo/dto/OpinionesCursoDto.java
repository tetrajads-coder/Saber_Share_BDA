package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OpinionesCursoDto {
    private Integer id;
    private String comentario;
    private Integer calificacion;
    private Integer usuarioId;
    private Integer cursoId;
}
