package org.example.demo.repository;

import org.example.demo.model.OpinionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionServicioRepository extends JpaRepository<OpinionServicio, Integer> {
    List<OpinionServicio> findByServicio_IdServicios(Integer servicioId);
    List<OpinionServicio> findByUsuario_IdUsuario(Integer usuarioId);
}