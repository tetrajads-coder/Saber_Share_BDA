package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class HistorialDto {
    private Integer idHistorial;
    private String fechapago;
    private Double pago;
    private Integer usuario_idUsuario;
    private Integer servicioId;
    private Integer cursoId;
}
