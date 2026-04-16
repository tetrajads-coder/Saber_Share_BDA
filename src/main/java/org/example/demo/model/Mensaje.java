package org.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMensaje")
    private Integer idMensaje;

    @Column(nullable = false, length = 500)
    private String contenido;

    @CreationTimestamp
    @Column(name = "fechaEnvio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    // ✅ Relaciones JPA correctas en lugar de IDs sueltos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisorId", nullable = false)
    private Usuario emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptorId", nullable = false)
    private Usuario receptor;

    @Builder.Default
    @Column(nullable = false)
    private Boolean leido = false;
}
