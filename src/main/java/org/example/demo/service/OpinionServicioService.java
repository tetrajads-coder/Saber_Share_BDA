package org.example.demo.service;

import org.example.demo.dto.OpinionServicioDto;
import org.example.demo.model.OpinionServicio;

import java.util.List;
import java.util.Map;

public interface OpinionServicioService {

    // ─── Métodos originales ───────────────────────────────────────────────────
    List<OpinionServicio> getAll();
    OpinionServicio getById(Integer id);
    OpinionServicio save(OpinionServicio opinion);
    OpinionServicio update(Integer id, OpinionServicio opinion);
    void delete(Integer id);
    List<OpinionServicio> findByServicio(Integer servicioId);

    // ─── Métodos nuevos (calificaciones mejoradas) ────────────────────────────
    OpinionServicioDto crearOpinion(OpinionServicioDto dto);
    List<OpinionServicioDto> obtenerPorServicio(Integer servicioId);
    List<OpinionServicioDto> obtenerPorUsuario(Integer usuarioId);
    Map<String, Object> obtenerEstadisticasServicio(Integer servicioId);
    OpinionServicioDto actualizarOpinion(Integer id, OpinionServicioDto dto);
    void eliminarOpinion(Integer id);
}