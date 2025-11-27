package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicioDto {
    private Integer servicioId;
    private String  titulo;
    private String  descripcion;
    private Double  precio;
    private String  requisitos;
    private String  fecha;
    private String  hora;
    private Integer usuarioId;
    private String nombreUsuario;
}


