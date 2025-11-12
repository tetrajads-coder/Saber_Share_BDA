package org.example.demo.controler;

import lombok.AllArgsConstructor;
import org.example.demo.dto.HistorialDto;
import org.example.demo.service.HistorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/Amaury/api")
@RestController
@AllArgsConstructor
public class HistorialControler {

    private final HistorialService historialService;

    @GetMapping("/historial")
    public ResponseEntity<List<HistorialDto>> list() {
        List<HistorialDto> list = historialService.getAllDto();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/historial/{id}")
    public ResponseEntity<HistorialDto> getById(@PathVariable Integer id) {
        HistorialDto dto = historialService.getDtoById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/historial/usuario/{usuarioId}")
    public ResponseEntity<List<HistorialDto>> getByUsuario(@PathVariable Integer usuarioId) {
        List<HistorialDto> list = historialService.getAllDtoByUsuario(usuarioId);
        return ResponseEntity.ok(list);
    }
}
