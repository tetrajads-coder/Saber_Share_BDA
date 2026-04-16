package org.example.demo.service;

import org.example.demo.dto.ConversacionDto;
import org.example.demo.dto.MensajeCreateDto;
import org.example.demo.dto.MensajeDto;

import java.util.List;

public interface MensajeService {

    /** Envía un mensaje de un usuario a otro */
    MensajeDto enviar(MensajeCreateDto dto);

    /** Obtiene todos los mensajes entre dos usuarios (conversación completa) */
    List<MensajeDto> conversacion(int user1, int user2);

    /** Obtiene el inbox: lista de conversaciones con el último mensaje de cada una */
    List<ConversacionDto> inbox(int userId);

    /** Marca como leídos todos los mensajes de emisorId hacia receptorId */
    void marcarLeidos(int receptorId, int emisorId);
}
