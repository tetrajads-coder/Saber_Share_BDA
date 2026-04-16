package org.example.demo.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo.dto.ConversacionDto;
import org.example.demo.dto.MensajeCreateDto;
import org.example.demo.dto.MensajeDto;
import org.example.demo.service.MensajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de mensajería:
 *
 *   POST  /api/mensajes                          → Enviar mensaje
 *   GET   /api/mensajes/conversacion?user1=&user2= → Ver conversación (y marca leídos)
 *   GET   /api/mensajes/inbox?userId=             → Inbox con último mensaje por chat
 *   PUT   /api/mensajes/leidos?receptorId=&emisorId= → Marcar mensajes como leídos
 */
@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
public class MensajeControler {

    private final MensajeService service;

    @PostMapping
    public ResponseEntity<MensajeDto> enviar(@Valid @RequestBody MensajeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.enviar(dto));
    }

    @GetMapping("/conversacion")
    public ResponseEntity<List<MensajeDto>> conversacion(
            @RequestParam int user1,
            @RequestParam int user2) {
        return ResponseEntity.ok(service.conversacion(user1, user2));
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<ConversacionDto>> inbox(@RequestParam int userId) {
        return ResponseEntity.ok(service.inbox(userId));
    }

    @PutMapping("/leidos")
    public ResponseEntity<Void> marcarLeidos(
            @RequestParam int receptorId,
            @RequestParam int emisorId) {
        service.marcarLeidos(receptorId, emisorId);
        return ResponseEntity.noContent().build();
    }
}
