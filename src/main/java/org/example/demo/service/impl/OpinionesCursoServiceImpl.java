package org.example.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.OpinionesCursoDto;
import org.example.demo.model.Curso;
import org.example.demo.model.OpinionesCurso;
import org.example.demo.model.Usuario;
import org.example.demo.repository.CursoRepository;
import org.example.demo.repository.OpinionesCursoRepository;
import org.example.demo.repository.UsuarioRepository;
import org.example.demo.service.OpinionesCursoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinionesCursoServiceImpl implements OpinionesCursoService {

    private final OpinionesCursoRepository opinionRepo;
    private final CursoRepository cursoRepo;
    private final UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public OpinionesCursoDto crearOpinion(OpinionesCursoDto dto) {
        // Validar que no haya duplicado
        if (opinionRepo.existsByCurso_IdCursoAndUsuario_IdUsuario(dto.getCursoId(), dto.getUsuarioId())) {
            throw new IllegalStateException("El usuario ya calificó este curso");
        }

        Curso curso = cursoRepo.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        OpinionesCurso opinion = new OpinionesCurso();
        opinion.setComentOps(dto.getComentOps());
        opinion.setCalOps(dto.getCalOps());
        opinion.setCurso(curso);
        opinion.setUsuario(usuario);

        OpinionesCurso saved = opinionRepo.save(opinion);
        return toDto(saved);
    }

    @Override
    public List<OpinionesCursoDto> obtenerPorCurso(Integer cursoId) {
        Double promedio = opinionRepo.calcularPromedioPorCurso(cursoId).orElse(0.0);
        Integer total = opinionRepo.contarResenasPorCurso(cursoId);

        return opinionRepo.findByCurso_IdCursoOrderByFechaCreacionDesc(cursoId)
                .stream()
                .map(o -> {
                    OpinionesCursoDto d = toDto(o);
                    d.setPromedioCalificacion(promedio != null ? Math.round(promedio * 10.0) / 10.0 : 0.0);
                    d.setTotalResenas(total);
                    return d;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OpinionesCursoDto> obtenerPorUsuario(Integer usuarioId) {
        return opinionRepo.findByUsuario_IdUsuario(usuarioId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> obtenerEstadisticasCurso(Integer cursoId) {
        Double promedio = opinionRepo.calcularPromedioPorCurso(cursoId).orElse(0.0);
        Integer total = opinionRepo.contarResenasPorCurso(cursoId);
        List<Object[]> distribucion = opinionRepo.distribucionEstrellasPorCurso(cursoId);

        // Construir mapa de distribución de estrellas (1-5)
        Map<Integer, Long> estrellas = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) estrellas.put(i, 0L);
        for (Object[] row : distribucion) {
            estrellas.put((Integer) row[0], (Long) row[1]);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("promedio", promedio != null ? Math.round(promedio * 10.0) / 10.0 : 0.0);
        stats.put("totalResenas", total);
        stats.put("distribucionEstrellas", estrellas);
        return stats;
    }

    @Override
    @Transactional
    public OpinionesCursoDto actualizarOpinion(Integer idOpinion, OpinionesCursoDto dto) {
        OpinionesCurso opinion = opinionRepo.findById(idOpinion)
                .orElseThrow(() -> new RuntimeException("Opinión no encontrada"));
        opinion.setComentOps(dto.getComentOps());
        opinion.setCalOps(dto.getCalOps());
        return toDto(opinionRepo.save(opinion));
    }

    @Override
    @Transactional
    public void eliminarOpinion(Integer idOpinion) {
        if (!opinionRepo.existsById(idOpinion)) {
            throw new RuntimeException("Opinión no encontrada");
        }
        opinionRepo.deleteById(idOpinion);
    }

    // ─── Mapper ───────────────────────────────────────────────────────────────
    private OpinionesCursoDto toDto(OpinionesCurso o) {
        OpinionesCursoDto dto = new OpinionesCursoDto();
        dto.setIdOpiniones(o.getIdOpiniones());
        dto.setComentOps(o.getComentOps());
        dto.setCalOps(o.getCalOps());
        dto.setCursoId(o.getCurso().getIdCurso());
        dto.setUsuarioId(o.getUsuario().getIdUsuario());
        dto.setTituloCurso(o.getCurso().getTitCur());
        dto.setNombreUsuario(o.getUsuario().getNomUsu() + " " + o.getUsuario().getApeUsu());
        dto.setFechaCreacion(o.getFechaCreacion());
        return dto;
    }
}
