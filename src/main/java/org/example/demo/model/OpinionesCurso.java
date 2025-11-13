package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "OpinionesCurso")
public class OpinionesCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOpiniones")
    private Integer idOpiniones;

    @Column(name = "coment_ops", length = 255)
    private String comentOps;

    @Column(name = "cal_ops")
    private Integer calOps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Curso_idCurso")
    private Curso curso;
}
