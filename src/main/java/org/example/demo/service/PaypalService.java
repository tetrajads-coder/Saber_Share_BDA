package org.example.demo.service;

import org.example.demo.dto.PaypalRequestDto;
import org.example.demo.dto.PaypalResponseDto;

public interface PaypalService {

    /**
     * Crea una orden de pago en PayPal.
     * Devuelve la URL de aprobación para redirigir al usuario.
     */
    PaypalResponseDto crearPago(PaypalRequestDto request);

    /**
     * Confirma y ejecuta el pago después de que el usuario aprueba en PayPal.
     * Llama a este endpoint desde el returnUrl (deep link de Android).
     *
     * @param paymentId  ID del pago devuelto por PayPal
     * @param payerId    ID del pagador, enviado por PayPal en el redirect
     * @param usuarioId  ID del usuario en tu sistema
     * @param itemId     ID del curso o servicio
     * @param tipo       "CURSO" o "SERVICIO"
     */
    PaypalResponseDto ejecutarPago(String paymentId, String payerId,
                                   Integer usuarioId, Integer itemId, String tipo);
}
