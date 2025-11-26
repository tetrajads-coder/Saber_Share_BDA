package org.example.demo.controler;

import lombok.AllArgsConstructor;
import org.example.demo.dto.AgendaDto;
import org.example.demo.model.Agenda;
import org.example.demo.model.Usuario;
import org.example.demo.service.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/Saber_Share/api")
@RestController
@AllArgsConstructor
public class AgendaControler {

    private final AgendaService service;

    @RequestMapping("/agenda")
    public ResponseEntity<List<AgendaDto>> lista() {
        List<Agenda> list = service.getAll();
        if (list == null || list.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(
                list.stream().map(a -> AgendaDto.builder()
                        .idAgenda(a.getIdAgenda())
                        .fechaserv(a.getFechaserv()!=null? a.getFechaserv().toString():null)
                        .pago(a.getPago())
                        .usuarioId(a.getUsuario()!=null? a.getUsuario().getIdUsuario():null)
                        .build()
                ).collect(Collectors.toList())
        );
    }

    @RequestMapping("/agenda/{id}")
    public ResponseEntity<AgendaDto> getById(@PathVariable Integer id) {
        Agenda a = service.getById(id);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(AgendaDto.builder()
                .idAgenda(a.getIdAgenda())
                .fechaserv(a.getFechaserv()!=null? a.getFechaserv().toString():null)
                .pago(a.getPago())
                .usuarioId(a.getUsuario()!=null? a.getUsuario().getIdUsuario():null)
                .build());
    }

    @PostMapping("/agenda")
    public ResponseEntity<AgendaDto> save(@RequestBody AgendaDto dto) {
        Agenda a = Agenda.builder()
                .fechaserv(dto.getFechaserv()!=null && !dto.getFechaserv().isEmpty()? LocalDate.parse(dto.getFechaserv()):null)
                .pago(dto.getPago())
                .usuario(dto.getUsuarioId()!=null? Usuario.builder().idUsuario(dto.getUsuarioId()).build():null)
                .build();
        service.save(a);
        return ResponseEntity.ok(AgendaDto.builder()
                .idAgenda(a.getIdAgenda())
                .fechaserv(a.getFechaserv()!=null? a.getFechaserv().toString():null)
                .pago(a.getPago())
                .usuarioId(a.getUsuario()!=null? a.getUsuario().getIdUsuario():null)
                .build());
    }

    @PutMapping("/agenda/{id}")
    public ResponseEntity<AgendaDto> update(@PathVariable Integer id, @RequestBody AgendaDto dto) {
        Agenda upd = service.update(id, Agenda.builder()
                .fechaserv(dto.getFechaserv()!=null && !dto.getFechaserv().isEmpty()? LocalDate.parse(dto.getFechaserv()):null)
                .pago(dto.getPago())
                .usuario(dto.getUsuarioId()!=null? Usuario.builder().idUsuario(dto.getUsuarioId()).build():null)
                .build());
        if (upd==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(AgendaDto.builder()
                .idAgenda(upd.getIdAgenda())
                .fechaserv(upd.getFechaserv()!=null? upd.getFechaserv().toString():null)
                .pago(upd.getPago())
                .usuarioId(upd.getUsuario()!=null? upd.getUsuario().getIdUsuario():null)
                .build());
    }

    @DeleteMapping("/agenda/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
