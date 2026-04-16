package org.example.demo.controler;

import lombok.RequiredArgsConstructor;
import org.example.demo.model.Usuario;
import org.example.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioSaldoController {

    private final UsuarioRepository usuarioRepository;

    /** GET /api/usuario/{id}/saldo → devuelve saldo pendiente y correo PayPal */
    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> getSaldo(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(u -> ResponseEntity.ok(Map.of(
                        "saldoPendiente", u.getSaldoPendiente() != null ? u.getSaldoPendiente() : 0.0,
                        "correoPaypal",   u.getCorreoPaypal()   != null ? u.getCorreoPaypal()   : ""
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /api/usuario/{id}/correo-paypal → registra o actualiza correo PayPal */
    @PutMapping("/{id}/correo-paypal")
    public ResponseEntity<?> actualizarCorreoPaypal(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String correo = body.get("correoPaypal");
        if (correo == null || correo.isBlank())
            return ResponseEntity.badRequest().body("correoPaypal es requerido");

        return usuarioRepository.findById(id).map(u -> {
            u.setCorreoPaypal(correo);
            usuarioRepository.save(u);
            return ResponseEntity.ok(Map.of("mensaje", "Correo PayPal actualizado"));
        }).orElse(ResponseEntity.notFound().build());
    }
}