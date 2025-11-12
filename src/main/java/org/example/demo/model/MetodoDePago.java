package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Metodo de Pago") // tu tabla tiene espacio, si puedes cámbiala a MetodoDePago
public class MetodoDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMetodo de Pago")
    private Integer idMetodoDePago;

    @Column(name = "compañia", length = 45)
    private String compania;

    @Column(name = "numtar", length = 25)
    private String numtar;

    @Column(name = "CVV", length = 15)
    private String cvv;

    @Column(name = "venci")
    private LocalDate venci;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
}

