package org.example.demo.service;

import org.example.demo.model.MetodoDePago;
import java.util.List;

public interface MetodoDePagoService {
    List<MetodoDePago> getAll();
    MetodoDePago getById(Integer id);
    MetodoDePago save(MetodoDePago metodoDePago);
    void delete(Integer id);
    MetodoDePago update(Integer id, MetodoDePago metodoDePago);
}
