package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "OpinionServicio")
public class OpinionServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOpiniones")
    private Integer idOpiniones;

    @Column(name = "coment_ops", length = 500)
    private String comentOps;

    @Column(name = "cal_ops")
    private Integer calOps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Servicio_Servicios")
    private Servicio servicio;

    // ✅ Campo nuevo — asegúrate de haber ejecutado el SQL de migración
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}