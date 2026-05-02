package org.example.demo.controler;

import lombok.AllArgsConstructor;
import org.example.demo.model.PasswordResetToken;
import org.example.demo.model.Usuario;
import org.example.demo.repository.PasswordResetTokenRepository;
import org.example.demo.security.JwtUtil;
import org.example.demo.service.EmailService;
import org.example.demo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo   = body.get("correo");
        String password = body.get("password");

        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Correo recibido: " + correo);
        System.out.println("Password recibido: " + password);

        Usuario usuario = usuarioService.findByCorreo(correo);

        System.out.println("Usuario encontrado: " + (usuario != null ? usuario.getCorreoUsu() : "NULL"));
        if (usuario != null) {
            System.out.println("Hash en BD: " + usuario.getContraUsu());
        }

        if (usuario == null || !passwordEncoder.matches(password, usuario.getContraUsu())) {
            return ResponseEntity.status(401)
                    .body(Map.of("mensaje", "Credenciales inválidas"));
        }

        String token    = jwtUtil.generateToken(usuario);
        String nombreRol = usuario.getRol() != null ? usuario.getRol().getNombre() : "SIN_ROL";

        return ResponseEntity.ok(Map.of(
                "token", token,
                "usuario", Map.of(
                        "id",     usuario.getIdUsuario(),
                        "nombre", usuario.getNomUsu() != null ? usuario.getNomUsu() : "",
                        "correo", usuario.getCorreoUsu(),
                        "rol",    nombreRol
                )
        ));
    }

    // POST /api/auth/recuperar
    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperar(@RequestBody Map<String, String> body) {
        String correo   = body.get("correo");
        Usuario usuario = usuarioService.findByCorreo(correo);

        // No revelar si el correo existe o no (seguridad)
        if (usuario != null) {
            String tokenStr = UUID.randomUUID().toString();

            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(tokenStr)
                    .usuario(usuario)
                    .fechaExpiracion(LocalDateTime.now().plusMinutes(30))
                    .usado(false)
                    .build();

            tokenRepository.save(resetToken);
            emailService.enviarEmailRecuperacion(correo, tokenStr);
        }

        return ResponseEntity.ok(Map.of(
                "mensaje", "Si el correo existe, recibirás instrucciones"
        ));
    }

    // POST /api/auth/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String tokenStr       = body.get("token");
        String nuevaPassword  = body.get("nuevaPassword");

        var optToken = tokenRepository.findByToken(tokenStr);

        if (optToken.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Token inválido o expirado"));
        }

        PasswordResetToken resetToken = optToken.get();

        if (resetToken.isUsado() || resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Token inválido o expirado"));
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setContraUsu(passwordEncoder.encode(nuevaPassword));
        usuarioService.save(usuario);

        resetToken.setUsado(true);
        tokenRepository.save(resetToken);

        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }
}
