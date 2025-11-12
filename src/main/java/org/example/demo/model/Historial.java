package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Historial")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorial;

    @Column(name = "servicio")
    private String servicio;

    @Column(name = "curso")
    private String curso;

    @Column(name = "fechapago")
    private LocalDate fechapago;

    @Column(name = "pago")
    private Double pago;

    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
}
