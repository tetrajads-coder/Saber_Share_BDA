package org.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursoDto {
    private Integer idCurso;
    private String titulo;
    private String descripcion;
    private Double precio;
    private String calificacion;
    private String foto;
    private Integer usuarioId;
    private String nombreUsuario;

}