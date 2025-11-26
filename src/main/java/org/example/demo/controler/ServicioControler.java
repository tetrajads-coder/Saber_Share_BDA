package org.example.demo.controler;

import org.example.demo.dto.ServicioDto;
import org.example.demo.model.Servicio;
import org.example.demo.model.Usuario;
import org.example.demo.service.ServicioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/Saber_Share/api")
@RestController
@AllArgsConstructor
public class ServicioControler
{
    private final ServicioService servicioService;

    @RequestMapping("/servicio")
    public ResponseEntity<List<ServicioDto>> lista()
    {
        List<Servicio> servicios = servicioService.getAll();
        if (servicios == null || servicios.size() == 0)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok(
                        servicios
                                .stream()
                                .map(s -> ServicioDto.builder()
                                        .servicioId(s.getIdServicios())
                                        .titulo(s.getTitSer())
                                        .descripcion(s.getDescripcion())
                                        .precio(s.getPrecioSer())
                                        .requisitos(s.getReqSer())
                                        .fecha(s.getFechaSer() != null ? s.getFechaSer().toString() : null)
                                        .hora(s.getHora() != null ? s.getHora().toString() : null)
                                        .usuarioId(s.getUsuario() != null ? s.getUsuario().getIdUsuario() : null)
                                        .build())
                                .collect(Collectors.toList()));
    }

    @RequestMapping("/servicio/{id}")
    public ResponseEntity<ServicioDto> getById(@PathVariable Integer id)
    {
        Servicio s = servicioService.getById(id);
        if (s == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ServicioDto.builder()
                .servicioId(s.getIdServicios())
                .titulo(s.getTitSer())
                .descripcion(s.getDescripcion())
                .precio(s.getPrecioSer())
                .requisitos(s.getReqSer())
                .fecha(s.getFechaSer() != null ? s.getFechaSer().toString() : null)
                .hora(s.getHora() != null ? s.getHora().toString() : null)
                .usuarioId(s.getUsuario() != null ? s.getUsuario().getIdUsuario() : null)
                .build());
    }

    @PostMapping("/servicio")
    public ResponseEntity<ServicioDto> save(@RequestBody ServicioDto servicioDto)
    {
        Servicio s = Servicio.builder()
                .titSer(servicioDto.getTitulo())
                .descripcion(servicioDto.getDescripcion())
                .precioSer(servicioDto.getPrecio())
                .reqSer(servicioDto.getRequisitos())
                .fechaSer(servicioDto.getFecha() != null && !servicioDto.getFecha().isEmpty() ? LocalDate.parse(servicioDto.getFecha()) : null)
                .hora(servicioDto.getHora() != null && !servicioDto.getHora().isEmpty() ? LocalTime.parse(servicioDto.getHora()) : null)
                .usuario(Usuario.builder().idUsuario(servicioDto.getUsuarioId()).build())
                .build();
        servicioService.save(s);
        return ResponseEntity.ok(ServicioDto.builder()
                .servicioId(s.getIdServicios())
                .titulo(s.getTitSer())
                .descripcion(s.getDescripcion())
                .precio(s.getPrecioSer())
                .requisitos(s.getReqSer())
                .fecha(s.getFechaSer() != null ? s.getFechaSer().toString() : null)
                .hora(s.getHora() != null ? s.getHora().toString() : null)
                .usuarioId(s.getUsuario() != null ? s.getUsuario().getIdUsuario() : null)
                .build());
    }

    @DeleteMapping("/servicio/{id}")
    public ResponseEntity<ServicioDto> delete(@PathVariable Integer id)
    {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/servicio/{id}")
    public ResponseEntity<ServicioDto> update(@PathVariable Integer id, @RequestBody ServicioDto servicioDto)
    {
        Servicio s = Servicio.builder()
                .titSer(servicioDto.getTitulo())
                .descripcion(servicioDto.getDescripcion())
                .precioSer(servicioDto.getPrecio())
                .reqSer(servicioDto.getRequisitos())
                .fechaSer(servicioDto.getFecha() != null && !servicioDto.getFecha().isEmpty() ? LocalDate.parse(servicioDto.getFecha()) : null)
                .hora(servicioDto.getHora() != null && !servicioDto.getHora().isEmpty() ? LocalTime.parse(servicioDto.getHora()) : null)
                .usuario(Usuario.builder().idUsuario(servicioDto.getUsuarioId()).build())
                .build();
        servicioService.update(id, s);
        return ResponseEntity.ok(ServicioDto.builder()
                .servicioId(s.getIdServicios())
                .titulo(s.getTitSer())
                .descripcion(s.getDescripcion())
                .precio(s.getPrecioSer())
                .requisitos(s.getReqSer())
                .fecha(s.getFechaSer() != null ? s.getFechaSer().toString() : null)
                .hora(s.getHora() != null ? s.getHora().toString() : null)
                .usuarioId(s.getUsuario() != null ? s.getUsuario().getIdUsuario() : null)
                .build());
    }
}
