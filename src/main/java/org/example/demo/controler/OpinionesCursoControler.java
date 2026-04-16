package org.example.demo.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo.dto.OpinionesCursoDto;
import org.example.demo.service.OpinionesCursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador mejorado para calificaciones de cursos.
 *
 * Endpoints:
 *   POST   /api/opiniones/cursos              → Crear calificación
 *   GET    /api/opiniones/cursos/{cursoId}     → Ver todas las del curso
 *   GET    /api/opiniones/cursos/stats/{id}    → Promedio + distribución estrellas
 *   GET    /api/opiniones/cursos/usuario/{id}  → Calificaciones de un usuario
 *   PUT    /api/opiniones/cursos/{id}          → Editar calificación
 *   DELETE /api/opiniones/cursos/{id}          → Eliminar calificación
 */
@RestController
@RequestMapping("/api/opiniones/cursos")
@RequiredArgsConstructor
public class OpinionesCursoControler {

    private final OpinionesCursoService opinionService;

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody OpinionesCursoDto dto) {
        try {
            OpinionesCursoDto creada = opinionService.crearOpinion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{cursoId}")
    public ResponseEntity<List<OpinionesCursoDto>> porCurso(@PathVariable Integer cursoId) {
        return ResponseEntity.ok(opinionService.obtenerPorCurso(cursoId));
    }

    @GetMapping("/stats/{cursoId}")
    public ResponseEntity<Map<String, Object>> estadisticas(@PathVariable Integer cursoId) {
        return ResponseEntity.ok(opinionService.obtenerEstadisticasCurso(cursoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<OpinionesCursoDto>> porUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(opinionService.obtenerPorUsuario(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpinionesCursoDto> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody OpinionesCursoDto dto) {
        return ResponseEntity.ok(opinionService.actualizarOpinion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        opinionService.eliminarOpinion(id);
        return ResponseEntity.noContent().build();
    }
}
