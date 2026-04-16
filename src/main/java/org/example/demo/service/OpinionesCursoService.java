package org.example.demo.service;

import org.example.demo.dto.OpinionesCursoDto;

import java.util.List;
import java.util.Map;

public interface OpinionesCursoService {

    /** Crear una nueva calificación (valida que no haya duplicado) */
    OpinionesCursoDto crearOpinion(OpinionesCursoDto dto);

    /** Obtener todas las calificaciones de un curso con promedio */
    List<OpinionesCursoDto> obtenerPorCurso(Integer cursoId);

    /** Obtener todas las calificaciones de un usuario */
    List<OpinionesCursoDto> obtenerPorUsuario(Integer usuarioId);

    /** Obtener promedio y distribución de estrellas de un curso */
    Map<String, Object> obtenerEstadisticasCurso(Integer cursoId);

    /** Actualizar una opinión existente */
    OpinionesCursoDto actualizarOpinion(Integer idOpinion, OpinionesCursoDto dto);

    /** Eliminar una opinión */
    void eliminarOpinion(Integer idOpinion);
}
