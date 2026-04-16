package org.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OpinionesCursoDto {
    private Integer idOpiniones;

    // Sin validación — comentario opcional
    private String comentOps;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1 estrella")
    @Max(value = 5, message = "La calificación máxima es 5 estrellas")
    private Integer calOps;

    @NotNull(message = "El curso es obligatorio")
    private Integer cursoId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    private String nombreUsuario;
    private String tituloCurso;
    private LocalDateTime fechaCreacion;
    private Double promedioCalificacion;
    private Integer totalResenas;
}