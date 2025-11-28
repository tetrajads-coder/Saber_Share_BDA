package org.example.demo.controler;

import lombok.AllArgsConstructor;
import org.example.demo.dto.CursoDto;
import org.example.demo.model.Curso;
import org.example.demo.model.Usuario;
import org.example.demo.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/Saber_Share/api")
@RestController
@AllArgsConstructor
public class CursoControler {

    private final CursoService cursoService;

    @GetMapping("/curso")
    public ResponseEntity<List<CursoDto>> lista() {
        List<Curso> cursos = cursoService.getAll();
        if (cursos == null || cursos.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                cursos.stream().map(this::toDto).collect(Collectors.toList())
        );
    }

    @GetMapping("/curso/{id}")
    public ResponseEntity<CursoDto> getById(@PathVariable Integer id) {
        Curso c = cursoService.getById(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDto(c));
    }

    @PostMapping("/curso")
    public ResponseEntity<CursoDto> save(@RequestBody CursoDto dto) {
        // Usaren ti getUsuarioId() imbes a getUsuario_idUsuario()
        if (dto.getUsuarioId() == null)
            return ResponseEntity.badRequest().build();

        Curso entidad = Curso.builder()
                .titCur(dto.getTitulo())       // <--- Nagan manipud Android
                .descCur(dto.getDescripcion())
                .preCur(dto.getPrecio())
                .calfCur(dto.getCalificacion())
                .foto(dto.getFoto())           // <--- Daytoy ti 'path'
                .usuario(Usuario.builder().idUsuario(dto.getUsuarioId()).build())
                .build();

        Curso saved = cursoService.save(entidad);
        return ResponseEntity
                .created(URI.create("/Saber_Share/api/curso/" + saved.getIdCurso()))
                .body(toDto(saved));
    }   

    @PutMapping("/curso/{id}")
    public ResponseEntity<CursoDto> update(@PathVariable Integer id, @RequestBody CursoDto dto) {
        if (dto.getUsuarioId() == null)
            return ResponseEntity.badRequest().build();

        Curso cambios = Curso.builder()
                .titCur(dto.getTitulo())
                .descCur(dto.getDescripcion())
                .preCur(dto.getPrecio())
                .calfCur(dto.getCalificacion())
                .foto(dto.getFoto())
                .usuario(Usuario.builder().idUsuario(dto.getUsuarioId()).build())
                .build();

        Curso up = cursoService.update(id, cambios);
        if (up == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDto(up));
    }

    @DeleteMapping("/curso/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CursoDto toDto(Curso c) {
        String nombreAutor = "Desconocido";
        if (c.getUsuario() != null) {
            nombreAutor = c.getUsuario().getNomUsu() + " " + c.getUsuario().getApeUsu();
        }

        return CursoDto.builder()
                .idCurso(c.getIdCurso())
                .titulo(c.getTitCur())       // <--- Mapeo inverso
                .descripcion(c.getDescCur())
                .precio(c.getPreCur())
                .calificacion(c.getCalfCur())
                .foto(c.getFoto())
                .usuarioId(c.getUsuario() != null ? c.getUsuario().getIdUsuario() : null)
                .nombreUsuario(nombreAutor)
                .build();
    }
}