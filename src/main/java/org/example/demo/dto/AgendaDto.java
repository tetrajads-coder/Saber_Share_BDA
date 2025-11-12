package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class AgendaDto {
    private Integer idAgenda;
    private String fechaserv;
    private Double pago;
    private Integer usuarioId;
}
