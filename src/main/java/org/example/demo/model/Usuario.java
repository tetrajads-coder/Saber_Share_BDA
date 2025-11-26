package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Column(name = "Usu_usu", length = 45)
    private String usuUsu;

    @Column(name = "Nom_usu", length = 45)
    private String nomUsu;

    @Column(name = "Ape_usu", length = 45)
    private String apeUsu;

    @Column(name = "Correo_usu", length = 45)
    private String correoUsu;

    @Column(name = "Contra_usu", length = 45)
    private String contraUsu;

    @Lob
    @Column(name = "Fot_usu")
    private byte[] fotUsu;

    @Column(name = "Inte_usu", length = 400)
    private String inteUsu;

    @Column(name = "Tel_usu", length = 45)
    private String telUsu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Rol_idRol", nullable = false)
    private Rol rol;
}
