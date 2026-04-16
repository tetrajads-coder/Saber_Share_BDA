package org.example.demo.repository;

import org.example.demo.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    /**
     * Obtiene todos los mensajes entre dos usuarios ordenados por fecha.
     * Funciona en ambas direcciones (A→B y B→A).
     */
    @Query("""
        SELECT m FROM Mensaje m
        WHERE (m.emisor.idUsuario = :user1 AND m.receptor.idUsuario = :user2)
           OR (m.emisor.idUsuario = :user2 AND m.receptor.idUsuario = :user1)
        ORDER BY m.fechaEnvio ASC
        """)
    List<Mensaje> findConversacion(@Param("user1") int user1, @Param("user2") int user2);

    /**
     * Obtiene el último mensaje de cada conversación del usuario.
     * Usado para construir el inbox (lista de chats).
     */
    @Query("""
        SELECT m FROM Mensaje m
        WHERE m.idMensaje IN (
            SELECT MAX(m2.idMensaje) FROM Mensaje m2
            WHERE m2.emisor.idUsuario = :userId OR m2.receptor.idUsuario = :userId
            GROUP BY CASE
                WHEN m2.emisor.idUsuario = :userId THEN m2.receptor.idUsuario
                ELSE m2.emisor.idUsuario
            END
        )
        ORDER BY m.fechaEnvio DESC
        """)
    List<Mensaje> findUltimosMensajesPorUsuario(@Param("userId") int userId);

    /**
     * Cuenta los mensajes no leídos que tiene un usuario de parte de otro.
     */
    @Query("""
        SELECT COUNT(m) FROM Mensaje m
        WHERE m.receptor.idUsuario = :receptorId
          AND m.emisor.idUsuario = :emisorId
          AND m.leido = false
        """)
    int countNoLeidos(@Param("receptorId") int receptorId, @Param("emisorId") int emisorId);

    /**
     * Marca como leídos todos los mensajes de una conversación.
     */
    @Modifying
    @Query("""
        UPDATE Mensaje m SET m.leido = true
        WHERE m.receptor.idUsuario = :receptorId
          AND m.emisor.idUsuario = :emisorId
          AND m.leido = false
        """)
    void marcarComoLeidos(@Param("receptorId") int receptorId, @Param("emisorId") int emisorId);
}
