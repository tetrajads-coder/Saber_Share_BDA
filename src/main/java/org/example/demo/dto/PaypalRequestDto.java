package org.example.demo.dto;

import lombok.Data;

/**
 * DTO para iniciar un pago con PayPal.
 * El frontend Android envía este objeto al endpoint de pago.
 */
@Data
public class PaypalRequestDto {

    /** ID del curso o servicio a pagar */
    private Integer itemId;

    /** Tipo: "CURSO" o "SERVICIO" */
    private String tipo;

    /** ID del usuario que realiza el pago */
    private Integer usuarioId;

    /** URL de retorno cuando el pago es aprobado (deep link Android) */
    private String returnUrl;

    /** URL de cancelación (deep link Android) */
    private String cancelUrl;
}
