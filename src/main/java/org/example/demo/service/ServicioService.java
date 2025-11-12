package org.example.demo.service;

import org.example.demo.model.Servicio;

import java.util.List;

public interface ServicioService {
    List<Servicio> getAll( );
    Servicio getById(Integer id);
    Servicio save(Servicio estado);
    void delete(Integer id);
    Servicio update(Integer id, Servicio servicio);
}
