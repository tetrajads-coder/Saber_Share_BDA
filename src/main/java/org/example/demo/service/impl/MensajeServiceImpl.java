package org.example.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.ConversacionDto;
import org.example.demo.dto.MensajeCreateDto;
import org.example.demo.dto.MensajeDto;
import org.example.demo.model.Mensaje;
import org.example.demo.model.Usuario;
import org.example.demo.repository.MensajeRepository;
import org.example.demo.repository.UsuarioRepository;
import org.example.demo.service.MensajeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository mensajeRepo;
    private final UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public MensajeDto enviar(MensajeCreateDto dto) {
        Usuario emisor = usuarioRepo.findById(dto.getEmisorId())
                .orElseThrow(() -> new RuntimeException("Emisor no encontrado: " + dto.getEmisorId()));
        Usuario receptor = usuarioRepo.findById(dto.getReceptorId())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado: " + dto.getReceptorId()));

        if (dto.getEmisorId().equals(dto.getReceptorId())) {
            throw new IllegalArgumentException("No puedes enviarte mensajes a ti mismo");
        }

        Mensaje mensaje = Mensaje.builder()
                .contenido(dto.getContenido())
                .emisor(emisor)
                .receptor(receptor)
                .leido(false)
                .build();

        return toDto(mensajeRepo.save(mensaje));
    }

    @Override
    @Transactional
    public List<MensajeDto> conversacion(int user1, int user2) {
        // Al abrir la conversación, marcar como leídos los mensajes de user2 hacia user1
        mensajeRepo.marcarComoLeidos(user1, user2);
        return mensajeRepo.findConversacion(user1, user2)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConversacionDto> inbox(int userId) {
        return mensajeRepo.findUltimosMensajesPorUsuario(userId)
                .stream()
                .map(m -> {
                    // El "otro" usuario es el que no soy yo
                    boolean soyEmisor = m.getEmisor().getIdUsuario().equals(userId);
                    Usuario otro = soyEmisor ? m.getReceptor() : m.getEmisor();

                    int noLeidos = mensajeRepo.countNoLeidos(userId, otro.getIdUsuario());

                    return new ConversacionDto(
                            otro.getIdUsuario(),
                            otro.getNomUsu() + " " + otro.getApeUsu(),
                            m.getContenido(),
                            m.getFechaEnvio(),
                            noLeidos
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void marcarLeidos(int receptorId, int emisorId) {
        mensajeRepo.marcarComoLeidos(receptorId, emisorId);
    }

    // ─── Mapper ───────────────────────────────────────────────────────────────
    private MensajeDto toDto(Mensaje m) {
        return new MensajeDto(
                m.getIdMensaje(),
                m.getContenido(),
                m.getFechaEnvio(),
                m.getEmisor().getIdUsuario(),
                m.getEmisor().getNomUsu() + " " + m.getEmisor().getApeUsu(),
                m.getReceptor().getIdUsuario(),
                m.getReceptor().getNomUsu() + " " + m.getReceptor().getApeUsu(),
                m.getLeido()
        );
    }
}
