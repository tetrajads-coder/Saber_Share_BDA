package org.example.demo.service.impl;

import lombok.AllArgsConstructor;
import org.example.demo.model.OpinionesCurso;
import org.example.demo.repository.OpinionesCursoRepository;
import org.example.demo.service.OpinionesCursoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OpinionesCursoServiceImpl implements OpinionesCursoService {

    private final OpinionesCursoRepository repo;

    @Override
    public List<OpinionesCurso> getAll() {
        return repo.findAll();
    }

    @Override
    public OpinionesCurso getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public OpinionesCurso save(OpinionesCurso opinion) {
        return repo.save(opinion);
    }

    @Override
    public OpinionesCurso update(Integer id, OpinionesCurso opinion) {
        OpinionesCurso db = repo.findById(id).orElse(null);
        if (db == null) return null;
        db.setComentOps(opinion.getComentOps());
        db.setCalOps(opinion.getCalOps());
        return repo.save(db);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<OpinionesCurso> findByCurso(Integer cursoId) {
        return repo.findByCurso_IdCurso(cursoId);
    }

}