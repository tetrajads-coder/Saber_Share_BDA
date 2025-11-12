package org.example.demo.service.impl;

import lombok.AllArgsConstructor;
import org.example.demo.model.Agenda;
import org.example.demo.repository.AgendaRepository;
import org.example.demo.service.AgendaService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AgendaServiceImpl implements AgendaService {
    private final AgendaRepository repo;

    @Override public List<Agenda> getAll(){ return repo.findAll(); }
    @Override public Agenda getById(Integer id){ return repo.findById(id).orElse(null); }
    @Override public Agenda save(Agenda a){ return repo.save(a); }
    @Override public void delete(Integer id){ repo.deleteById(id); }
    @Override public Agenda update(Integer id, Agenda a){
        Agenda x = repo.getById(id);
        x.setFechaserv(a.getFechaserv());
        x.setPago(a.getPago());
        x.setUsuario(a.getUsuario());
        return repo.save(x);
    }
}
