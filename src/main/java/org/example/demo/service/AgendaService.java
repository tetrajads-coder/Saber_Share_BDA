package org.example.demo.service;

import org.example.demo.model.Agenda;
import java.util.List;

public interface AgendaService {
    List<Agenda> getAll();
    Agenda getById(Integer id);
    Agenda save(Agenda agenda);
    void delete(Integer id);
    Agenda update(Integer id, Agenda agenda);
}
