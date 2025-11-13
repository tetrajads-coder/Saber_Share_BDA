package org.example.demo.service;

import org.example.demo.model.OpinionServicio; // Importa el Modelo
import java.util.List;

public interface OpinionServicioService {
    List<OpinionServicio> getAll();
    OpinionServicio getById(Integer id);
    OpinionServicio save(OpinionServicio opinion);
    OpinionServicio update(Integer id, OpinionServicio opinion);
    void delete(Integer id);
    List<OpinionServicio> findByServicio(Integer servicioId);
}