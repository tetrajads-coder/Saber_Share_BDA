package org.example.demo.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo.dto.OpinionServicioDto;
import org.example.demo.service.OpinionServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints para opiniones de servicios:
 *
 *   GET    /api/opinion_servicio                      → Listar todas
 *   GET    /api/opinion_servicio/{id}                 → Obtener por ID
 *   GET    /api/opinion_servicio/servicio/{servicioId}→ Por servicio (con promedio)
 *   GET    /api/opinion_servicio/stats/{servicioId}   → Estadísticas y distribución
 *   GET    /api/opinion_servicio/usuario/{usuarioId}  → Por usuario
 *   POST   /api/opinion_servicio                      → Crear opinión
 *   PUT    /api/opinion_servicio/{id}                 → Actualizar opinión
 *   DELETE /api/opinion_servicio/{id}                 → Eliminar opinión
 */
@RestController
@RequestMapping("/api/opinion_servicio")
@RequiredArgsConstructor
public class OpinionServicioControler {

    private final OpinionServicioService service;

    @GetMapping
    public ResponseEntity<List<OpinionServicioDto>> lista() {
        // Reutiliza el método de obtener todas usando el service mejorado
        List<OpinionServicioDto> opiniones = service.getAll()
                .stream()
                .map(o -> {
                    OpinionServicioDto dto = new OpinionServicioDto();
                    dto.setIdOpiniones(o.getIdOpiniones());
                    dto.setComentOps(o.getComentOps());
                    dto.setCalOps(o.getCalOps());
                    dto.setServicioId(o.getServicio() != null ? o.getServicio().getIdServicios() : null);
                    dto.setUsuarioId(o.getUsuario() != null ? o.getUsuario().getIdUsuario() : null);
                    dto.setTituloServicio(o.getServicio() != null ? o.getServicio().getTitSer() : null);
                    dto.setNombreUsuario(o.getUsuario() != null ?
                            o.getUsuario().getNomUsu() + " " + o.getUsuario().getApeUsu() : null);
                    dto.setFechaCreacion(o.getFechaCreacion());
                    return dto;
                })
                .toList();

        if (opiniones.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opiniones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpinionServicioDto> getById(@PathVariable Integer id) {
        var opinion = service.getById(id);
        if (opinion == null) return ResponseEntity.notFound().build();

        OpinionServicioDto dto = new OpinionServicioDto();
        dto.setIdOpiniones(opinion.getIdOpiniones());
        dto.setComentOps(opinion.getComentOps());
        dto.setCalOps(opinion.getCalOps());
        dto.setServicioId(opinion.getServicio() != null ? opinion.getServicio().getIdServicios() : null);
        dto.setUsuarioId(opinion.getUsuario() != null ? opinion.getUsuario().getIdUsuario() : null);
        dto.setTituloServicio(opinion.getServicio() != null ? opinion.getServicio().getTitSer() : null);
        dto.setNombreUsuario(opinion.getUsuario() != null ?
                opinion.getUsuario().getNomUsu() + " " + opinion.getUsuario().getApeUsu() : null);
        dto.setFechaCreacion(opinion.getFechaCreacion());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<OpinionServicioDto>> getByServicio(@PathVariable Integer servicioId) {
        List<OpinionServicioDto> opiniones = service.obtenerPorServicio(servicioId);
        if (opiniones.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(opiniones);
    }

    @GetMapping("/stats/{servicioId}")
    public ResponseEntity<Map<String, Object>> estadisticas(@PathVariable Integer servicioId) {
        return ResponseEntity.ok(service.obtenerEstadisticasServicio(servicioId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<OpinionServicioDto>> getByUsuario(@PathVariable Integer usuarioId) {
        List<OpinionServicioDto> opiniones = service.obtenerPorUsuario(usuarioId);
        if (opiniones.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(opiniones);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody OpinionServicioDto dto) {
        try {
            OpinionServicioDto creada = service.crearOpinion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpinionServicioDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody OpinionServicioDto dto) {
        var updated = service.actualizarOpinion(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.getById(id) == null) return ResponseEntity.notFound().build();
        service.eliminarOpinion(id);
        return ResponseEntity.noContent().build();
    }
}