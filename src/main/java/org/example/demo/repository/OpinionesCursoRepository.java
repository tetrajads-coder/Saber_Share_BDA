package org.example.demo.repository;

import org.example.demo.model.OpinionesCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OpinionesCursoRepository extends JpaRepository<OpinionesCurso, Integer> {

    /** Todas las opiniones de un curso, ordenadas por más recientes */
    List<OpinionesCurso> findByCurso_IdCursoOrderByFechaCreacionDesc(Integer cursoId);

    /** Todas las opiniones de un usuario */
    List<OpinionesCurso> findByUsuario_IdUsuario(Integer usuarioId);

    /** Promedio de calificación de un curso */
    @Query("SELECT AVG(o.calOps) FROM OpinionesCurso o WHERE o.curso.idCurso = :cursoId")
    Optional<Double> calcularPromedioPorCurso(@Param("cursoId") Integer cursoId);

    /** Total de reseñas de un curso */
    @Query("SELECT COUNT(o) FROM OpinionesCurso o WHERE o.curso.idCurso = :cursoId")
    Integer contarResenasPorCurso(@Param("cursoId") Integer cursoId);

    /** Verifica si un usuario ya calificó un curso (evitar duplicados) */
    boolean existsByCurso_IdCursoAndUsuario_IdUsuario(Integer cursoId, Integer usuarioId);

    /** Distribución de estrellas (cuántos dieron 1, 2, 3, 4, 5 estrellas) */
    @Query("SELECT o.calOps, COUNT(o) FROM OpinionesCurso o WHERE o.curso.idCurso = :cursoId GROUP BY o.calOps ORDER BY o.calOps")
    List<Object[]> distribucionEstrellasPorCurso(@Param("cursoId") Integer cursoId);
}
