package org.example.demo.controler;

import lombok.AllArgsConstructor;
import org.example.demo.dto.AgendaDto;
import org.example.demo.model.Agenda;
import org.example.demo.model.Servicio;
import org.example.demo.model.Usuario;
import org.example.demo.service.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@AllArgsConstructor
public class AgendaControler {

    private final AgendaService service;

    @GetMapping("/servicio/{idServicio}")
    public ResponseEntity<List<AgendaDto>> getSlotsPorServicio(@PathVariable Integer idServicio) {
        return ResponseEntity.ok(service.getDtoByServicio(idServicio));
    }

    @GetMapping("/profesor/{idProfesor}")
    public ResponseEntity<List<AgendaDto>> getSlotsPorProfesor(@PathVariable Integer idProfesor) {
        return ResponseEntity.ok(service.getDtoByProfesor(idProfesor));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AgendaDto>> getSlotsPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(service.getDtoByProfesor(idUsuario));
    }

    @PostMapping
    public ResponseEntity<?> crearSlot(@RequestBody AgendaDto dto) {
        if (dto.getServicioId() == null || dto.getProfesorId() == null ||
                dto.getFecha() == null || dto.getHora() == null) {
            return ResponseEntity.badRequest()
                    .body("servicioId, profesorId, fecha y hora son obligatorios");
        }

        final LocalDate fecha;
        final LocalTime hora;
        try {
            fecha = LocalDate.parse(dto.getFecha());
            String h = dto.getHora();
            hora = (h.length() == 5) ? LocalTime.parse(h + ":00") : LocalTime.parse(h);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Formato inválido. fecha=yyyy-MM-dd, hora=HH:mm:ss");
        }

        Agenda entidad = Agenda.builder()
                .fecha(fecha)
                .hora(hora)
                .estado(dto.getEstado() != null && !dto.getEstado().isBlank()
                        ? dto.getEstado() : "DISPONIBLE")
                .servicio(Servicio.builder().idServicios(dto.getServicioId()).build())
                .profesor(Usuario.builder().idUsuario(dto.getProfesorId()).build())
                .alumno(dto.getAlumnoId() != null
                        ? Usuario.builder().idUsuario(dto.getAlumnoId()).build() : null)
                .build();

        Agenda saved = service.save(entidad);
        return ResponseEntity
                .created(URI.create("/api/agenda/" + saved.getIdAgenda()))
                .body(service.toDto(saved));
    }

    @PutMapping("/reservar/{idAgenda}")
    public ResponseEntity<?> reservar(@PathVariable Integer idAgenda,
                                      @RequestParam("idAlumno") Integer idAlumno) {
        Agenda upd = service.reservar(idAgenda, idAlumno);
        if (upd == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service.toDto(upd));
    }

    @DeleteMapping("/{idAgenda}")
    public ResponseEntity<Void> eliminarSlot(@PathVariable Integer idAgenda) {
        service.delete(idAgenda);
        return ResponseEntity.noContent().build();
    }
}