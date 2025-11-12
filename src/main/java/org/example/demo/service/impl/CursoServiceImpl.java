package org.example.demo.service.impl;

import lombok.AllArgsConstructor;
import org.example.demo.model.Curso;
import org.example.demo.repository.CursoRepository;
import org.example.demo.service.CursoService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository repo;

    @Override
    public List<Curso> getAll() {

        return repo.findAllWithUsuario();
    }

    @Override
    public Curso getById(Integer id) {

        return repo.findById(id).orElse(null);
    }

    @Override
    public Curso save(Curso c) {
        return repo.save(c);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Curso update(Integer id, Curso c) {
        Curso db = repo.findById(id).orElse(null);
        if (db == null) return null;
        db.setTitCur(c.getTitCur());
        db.setDescCur(c.getDescCur());
        db.setPreCur(c.getPreCur());
        db.setCalfCur(c.getCalfCur());
        db.setUsuario(c.getUsuario());
        db.setFoto(c.getFoto());
        return repo.save(db);
    }
}
