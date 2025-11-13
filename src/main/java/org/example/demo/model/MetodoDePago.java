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

