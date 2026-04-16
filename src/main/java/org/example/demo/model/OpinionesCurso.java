package org.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "OpinionesCurso")
public class OpinionesCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOpiniones")
    private Integer idOpiniones;

    @Column(name = "coment_ops", nullable = false, length = 500)
    private String comentOps;

    @Column(name = "cal_ops", nullable = false)
    private Integer calOps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Curso_idCurso", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuario;

    /** Fecha de creación automática — agrega esta columna con el script SQL */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}
