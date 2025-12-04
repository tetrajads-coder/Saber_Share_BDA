package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MetodoDePagoDto {
    private Integer id;
    private String compania;
    private String numeroTarjeta;
    private String cvv;
    private String vencimiento;
    private String titular;
    private Integer usuarioId;
}