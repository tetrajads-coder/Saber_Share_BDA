package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idServicios")
    private Integer idServicios;

    @Column(name = "tit_ser", nullable = false, length = 45)
    private String titSer;

    @Column(name = "descripcion", nullable = false, length = 350)
    private String descripcion;

    @Lob
    @Column(name = "img_ser")
    private byte[] imgSer;

    @Column(name = "precio_ser", nullable = false)
    private Double precioSer;

    @Column(name = "fecha_ser", nullable = false)
    private LocalDate fechaSer;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "req_ser", nullable = false, length = 45)
    private String reqSer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
}
