package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Metodo_de_Pago")
public class MetodoDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMetodo_de_Pago")
    private Integer idMetodoDePago;

    @Column(name = "compañia", nullable = false, length = 45)
    private String compania;

    @Column(name = "numtar", nullable = false, length = 25)
    private String numtar;

    @Column(name = "CVV", nullable = false, length = 15)
    private String cvv;

    @Column(name = "venci", nullable = false)
    private LocalDate venci;

    @Column(name = "titular", nullable = false, length = 100) // <-- NUEVO
    private String titular;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
}

