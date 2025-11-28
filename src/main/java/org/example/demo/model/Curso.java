package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCurso")
    private Integer idCurso;

    @Column(name = "tit_cur", length = 45)
    private String titCur;

    @Column(name = "desc_cur", length = 350)
    private String descCur;

    @Column(name = "pre_cur")
    private Double preCur;

    @Column(name = "calf_cur", length = 45)
    private String calfCur;

    @Column(name = "Foto", length = 500)
    private String foto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
}

