package org.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursoDto {
    private Integer idCurso;

    @JsonProperty("tit_cur")
    private String tit_cur;

    @JsonProperty("desc_cur")
    private String desc_cur;

    @JsonProperty("pre_cur")
    private Double pre_cur;

    @JsonProperty("calf_cur")
    private String calf_cur;

    @JsonProperty("Foto")
    private String Foto;

    @JsonProperty("usuario_idUsuario")
    private Integer usuario_idUsuario;
}
