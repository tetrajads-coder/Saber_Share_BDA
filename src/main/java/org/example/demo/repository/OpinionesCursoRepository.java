package org.example.demo.repository;

import org.example.demo.model.OpinionesCurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpinionesCursoRepository extends JpaRepository<OpinionesCurso,Integer> {
    List<OpinionesCurso> findByCurso_IdCurso(Integer cursoId);
    List<OpinionesCurso> findByUsuario_IdUsuario(Integer usuarioId);
}
