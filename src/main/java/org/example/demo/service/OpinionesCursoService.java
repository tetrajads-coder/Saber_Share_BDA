package org.example.demo.service;

import org.example.demo.model.OpinionesCurso;
import java.util.List;

public interface OpinionesCursoService {
    List<OpinionesCurso> getAll();
    OpinionesCurso getById(Integer id);
    OpinionesCurso save(OpinionesCurso opinion);
    OpinionesCurso update(Integer id, OpinionesCurso opinion);
    void delete(Integer id);
    List<OpinionesCurso> findByCurso(Integer cursoId);
}