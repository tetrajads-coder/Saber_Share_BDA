package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta tras crear una orden PayPal.
 * El frontend Android abre la approvalUrl en un WebView o navegador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaypalResponseDto {

    /** ID de la orden PayPal (para confirmar luego) */
    private String paymentId;

    /** URL que el usuario debe abrir para aprobar el pago */
    private String approvalUrl;

    /** Estado: "created", "approved", "failed" */
    private String status;

    /** Mensaje adicional (errores o confirmación) */
    private String message;
}
