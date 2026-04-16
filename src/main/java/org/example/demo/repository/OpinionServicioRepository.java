package org.example.demo.repository;

import org.example.demo.model.OpinionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OpinionServicioRepository extends JpaRepository<OpinionServicio, Integer> {

    List<OpinionServicio> findByServicio_IdServiciosOrderByFechaCreacionDesc(Integer servicioId);

    List<OpinionServicio> findByUsuario_IdUsuario(Integer usuarioId);

    @Query("SELECT AVG(o.calOps) FROM OpinionServicio o WHERE o.servicio.idServicios = :servicioId")
    Optional<Double> calcularPromedioPorServicio(@Param("servicioId") Integer servicioId);

    @Query("SELECT COUNT(o) FROM OpinionServicio o WHERE o.servicio.idServicios = :servicioId")
    Integer contarResenasPorServicio(@Param("servicioId") Integer servicioId);

    boolean existsByServicio_IdServiciosAndUsuario_IdUsuario(Integer servicioId, Integer usuarioId);

    @Query("SELECT o.calOps, COUNT(o) FROM OpinionServicio o WHERE o.servicio.idServicios = :servicioId GROUP BY o.calOps ORDER BY o.calOps")
    List<Object[]> distribucionEstrellasPorServicio(@Param("servicioId") Integer servicioId);
}
