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

@RequestMapping("/Amaury/api")
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
        if (dto.getUsuario_idUsuario() == null)
            return ResponseEntity.badRequest().build();

        Curso entidad = Curso.builder()
                .titCur(dto.getTit_cur())
                .descCur(dto.getDesc_cur())
                .preCur(dto.getPre_cur())
                .calfCur(dto.getCalf_cur())
                .foto(dto.getFoto())
                .usuario(Usuario.builder().idUsuario(dto.getUsuario_idUsuario()).build())
                .build();

        Curso saved = cursoService.save(entidad);
        return ResponseEntity
                .created(URI.create("/Amaury/api/curso/" + saved.getIdCurso()))
                .body(toDto(saved));
    }


    @PutMapping("/curso/{id}")
    public ResponseEntity<CursoDto> update(@PathVariable Integer id, @RequestBody CursoDto dto) {
        if (dto.getUsuario_idUsuario() == null)
            return ResponseEntity.badRequest().build();

        Curso cambios = Curso.builder()
                .titCur(dto.getTit_cur())
                .descCur(dto.getDesc_cur())
                .preCur(dto.getPre_cur())
                .calfCur(dto.getCalf_cur())
                .foto(dto.getFoto())
                .usuario(Usuario.builder().idUsuario(dto.getUsuario_idUsuario()).build())
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
        return CursoDto.builder()
                .idCurso(c.getIdCurso())
                .tit_cur(c.getTitCur())
                .desc_cur(c.getDescCur())
                .pre_cur(c.getPreCur())
                .calf_cur(c.getCalfCur())
                .Foto(c.getFoto())
                .usuario_idUsuario(c.getUsuario() != null ? c.getUsuario().getIdUsuario() : null)
                .build();
    }
}
