package org.example.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.AgendaDto;
import org.example.demo.model.Agenda;
import org.example.demo.model.Usuario;
import org.example.demo.repository.AgendaRepository;
import org.example.demo.service.AgendaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<AgendaDto> getDtoByServicio(Integer servicioId) {
        return repo.findByServicio_IdServicios(servicioId)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaDto> getDtoByProfesor(Integer profesorId) {
        return repo.findByProfesor_IdUsuario(profesorId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public Agenda save(Agenda agenda) {
        if (agenda.getProfesor() == null || agenda.getProfesor().getIdUsuario() == null)
            throw new IllegalArgumentException("profesorId es obligatorio");
        if (agenda.getServicio() == null || agenda.getServicio().getIdServicios() == null)
            throw new IllegalArgumentException("servicioId es obligatorio");
        if (agenda.getFecha() == null)
            throw new IllegalArgumentException("fecha es obligatoria");
        if (agenda.getHora() == null)
            throw new IllegalArgumentException("hora es obligatoria");

        if (agenda.getEstado() == null || agenda.getEstado().isBlank())
            agenda.setEstado("DISPONIBLE");

        if ("DISPONIBLE".equalsIgnoreCase(agenda.getEstado()))
            agenda.setAlumno(null);

        return repo.save(agenda);
    }

    @Override
    public Agenda reservar(Integer idAgenda, Integer idAlumno) {
        Agenda slot = repo.findById(idAgenda).orElse(null);
        if (slot == null) return null;
        if ("RESERVADA".equalsIgnoreCase(slot.getEstado()) || slot.getAlumno() != null)
            return slot;
        slot.setEstado("RESERVADA");
        slot.setAlumno(Usuario.builder().idUsuario(idAlumno).build());
        return repo.save(slot);
    }

    @Override
    public void delete(Integer idAgenda) {
        if (!repo.existsById(idAgenda))
            throw new RuntimeException("Agenda no encontrada: " + idAgenda);
        repo.deleteById(idAgenda);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendaDto toDto(Agenda a) {
        String nombreAlumno = null;
        if (a.getAlumno() != null) {
            String n  = a.getAlumno().getNomUsu() != null ? a.getAlumno().getNomUsu() : "";
            String ap = a.getAlumno().getApeUsu() != null ? a.getAlumno().getApeUsu() : "";
            nombreAlumno = (n + " " + ap).trim();
            if (nombreAlumno.isEmpty()) nombreAlumno = null;
        }

        return AgendaDto.builder()
                .idAgenda(a.getIdAgenda())
                .fecha(a.getFecha() != null ? a.getFecha().toString() : null)
                .hora(a.getHora()   != null ? a.getHora().toString()  : null)
                .estado(a.getEstado())
                .servicioId(a.getServicio() != null ? a.getServicio().getIdServicios() : null)
                .profesorId(a.getProfesor() != null ? a.getProfesor().getIdUsuario()   : null)
                .alumnoId(a.getAlumno()     != null ? a.getAlumno().getIdUsuario()     : null)
                .tituloServicio(a.getServicio() != null ? a.getServicio().getTitSer()  : null)
                .nombreAlumno(nombreAlumno)
                .build();
    }
}