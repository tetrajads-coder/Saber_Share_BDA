package org.example.demo.controler;

import lombok.AllArgsConstructor;
import org.example.demo.dto.UsuarioDto;
import org.example.demo.model.Usuario;
import org.example.demo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/Amaury/api")
@RestController
@AllArgsConstructor
public class UsuarioControler {

    private final UsuarioService usuarioService;


    @RequestMapping("/usuario")
    public ResponseEntity<List<UsuarioDto>> lista(
            @RequestParam(name = "user", defaultValue = "", required = false) String user) {

        List<Usuario> usuarios = usuarioService.getAll();
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var stream = usuarios.stream();
        if (user != null && !user.isEmpty()) {
            stream = stream.filter(u -> user.equals(u.getUsuUsu()));
        }

        return ResponseEntity.ok(
                stream.map(this::toDto).collect(Collectors.toList())
        );
    }

    // GET /usuario/{id}
    @RequestMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> getById(@PathVariable Integer id) {
        Usuario u = usuarioService.getById(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDto(u));
    }

    // POST /usuario
    @PostMapping("/usuario")
    public ResponseEntity<UsuarioDto> save(@RequestBody UsuarioDto dto) {
        Usuario u = Usuario.builder()
                .usuUsu(dto.getUser())
                .nomUsu(dto.getNombre())
                .apeUsu(dto.getApellido())
                .correoUsu(dto.getCorreo())
                .contraUsu(dto.getPassword())
                .telUsu(dto.getTelefono())
                .build();
        usuarioService.save(u);
        return ResponseEntity.ok(toDto(u));
    }

    // PUT /usuario/{id}
    @PutMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @RequestBody UsuarioDto dto) {
        Usuario up = usuarioService.update(id, Usuario.builder()
                .usuUsu(dto.getUser())
                .nomUsu(dto.getNombre())
                .apeUsu(dto.getApellido())
                .correoUsu(dto.getCorreo())
                .contraUsu(dto.getPassword())
                .telUsu(dto.getTelefono())
                .build());

        if (up == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDto(up));
    }

    // DELETE /usuario/{id}
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --------- mapper privado ----------
    private UsuarioDto toDto(Usuario u) {
        return UsuarioDto.builder()
                .id(u.getIdUsuario())
                .user(u.getUsuUsu())
                .nombre(u.getNomUsu())
                .apellido(u.getApeUsu())
                .correo(u.getCorreoUsu())
                .password(u.getContraUsu())
                .telefono(u.getTelUsu())
                .build();
    }
}
