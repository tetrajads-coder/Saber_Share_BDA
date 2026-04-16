package org.example.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.OpinionServicioDto;
import org.example.demo.model.OpinionServicio;
import org.example.demo.model.Servicio;
import org.example.demo.model.Usuario;
import org.example.demo.repository.OpinionServicioRepository;
import org.example.demo.repository.ServicioRepository;
import org.example.demo.repository.UsuarioRepository;
import org.example.demo.service.OpinionServicioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinionServicioServiceImpl implements OpinionServicioService {

    private final OpinionServicioRepository repo;
    private final ServicioRepository servicioRepo;
    private final UsuarioRepository usuarioRepo;

    // ─── Métodos originales (compatibles con tu código anterior) ─────────────

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
        // ✅ Corregido: usa el nombre correcto del nuevo repository
        return repo.findByServicio_IdServiciosOrderByFechaCreacionDesc(servicioId);
    }

    // ─── Métodos nuevos (calificaciones mejoradas) ────────────────────────────

    @Transactional
    public OpinionServicioDto crearOpinion(OpinionServicioDto dto) {
        if (repo.existsByServicio_IdServiciosAndUsuario_IdUsuario(dto.getServicioId(), dto.getUsuarioId())) {
            throw new IllegalStateException("El usuario ya calificó este servicio");
        }

        Servicio servicio = servicioRepo.findById(dto.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        OpinionServicio opinion = new OpinionServicio();
        opinion.setComentOps(dto.getComentOps());
        opinion.setCalOps(dto.getCalOps());
        opinion.setServicio(servicio);
        opinion.setUsuario(usuario);

        return toDto(repo.save(opinion));
    }

    public List<OpinionServicioDto> obtenerPorServicio(Integer servicioId) {
        Double promedio = repo.calcularPromedioPorServicio(servicioId).orElse(0.0);
        Integer total = repo.contarResenasPorServicio(servicioId);

        return repo.findByServicio_IdServiciosOrderByFechaCreacionDesc(servicioId)
                .stream()
                .map(o -> {
                    OpinionServicioDto d = toDto(o);
                    d.setPromedioCalificacion(promedio != null ? Math.round(promedio * 10.0) / 10.0 : 0.0);
                    d.setTotalResenas(total);
                    return d;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OpinionServicioDto> obtenerPorUsuario(Integer usuarioId) {
        return List.of();
    }

    public Map<String, Object> obtenerEstadisticasServicio(Integer servicioId) {
        Double promedio = repo.calcularPromedioPorServicio(servicioId).orElse(0.0);
        Integer total = repo.contarResenasPorServicio(servicioId);
        List<Object[]> distribucion = repo.distribucionEstrellasPorServicio(servicioId);

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
    public OpinionServicioDto actualizarOpinion(Integer id, OpinionServicioDto dto) {
        return null;
    }

    @Override
    public void eliminarOpinion(Integer id) {

    }

    // ─── Mapper ───────────────────────────────────────────────────────────────
    private OpinionServicioDto toDto(OpinionServicio o) {
        OpinionServicioDto dto = new OpinionServicioDto();
        dto.setIdOpiniones(o.getIdOpiniones());
        dto.setComentOps(o.getComentOps());
        dto.setCalOps(o.getCalOps());
        dto.setServicioId(o.getServicio().getIdServicios());
        dto.setUsuarioId(o.getUsuario().getIdUsuario());
        dto.setTituloServicio(o.getServicio().getTitSer());
        dto.setNombreUsuario(o.getUsuario().getNomUsu() + " " + o.getUsuario().getApeUsu());
        dto.setFechaCreacion(o.getFechaCreacion());
        return dto;
    }
}