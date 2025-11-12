package org.example.demo.service;

import org.example.demo.model.Curso;
import java.util.List;

public interface CursoService {
    List<Curso> getAll();
    Curso getById(Integer id);
    Curso save(Curso curso);
    void delete(Integer id);
    Curso update(Integer id, Curso curso);
}
