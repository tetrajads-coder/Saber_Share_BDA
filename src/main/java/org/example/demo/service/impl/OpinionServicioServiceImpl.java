package org.example.demo.service.impl;

import lombok.AllArgsConstructor;
import org.example.demo.model.OpinionServicio;
import org.example.demo.repository.OpinionServicioRepository;
import org.example.demo.service.OpinionServicioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OpinionServicioServiceImpl implements OpinionServicioService {

    private final OpinionServicioRepository repo;

    @Override
    public List<OpinionServicio> getAll() {
        return repo.findAll();
    }

    @Override
    public OpinionServicio getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public OpinionServicio save(OpinionServicio opinion) {
        return repo.save(opinion);
    }

    @Override
    public OpinionServicio update(Integer id, OpinionServicio opinion) {
        OpinionServicio db = repo.findById(id).orElse(null);
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
    public List<OpinionServicio> findByServicio(Integer servicioId) {
        return repo.findByServicio_IdServicios(servicioId);
    }

}