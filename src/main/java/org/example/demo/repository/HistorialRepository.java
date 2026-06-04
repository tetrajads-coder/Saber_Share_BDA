package org.example.demo.repository;

import org.example.demo.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HistorialRepository extends JpaRepository<Historial, Integer> {
    List<Historial> findByUsuario_IdUsuario(Integer usuarioId);
    List<Historial> findByCurso_IdCurso(Integer cursoId);

    @Query("SELECT h FROM Historial h WHERE " +
           "(h.curso   IS NOT NULL AND h.curso.usuario.idUsuario   = :vendedorId) OR " +
           "(h.servicio IS NOT NULL AND h.servicio.usuario.idUsuario = :vendedorId)")
    List<Historial> findByVendedorId(@Param("vendedorId") Integer vendedorId);
}
