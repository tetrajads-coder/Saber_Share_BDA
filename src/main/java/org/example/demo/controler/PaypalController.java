package org.example.demo.controler;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.PaypalRequestDto;
import org.example.demo.dto.PaypalResponseDto;
import org.example.demo.service.PaypalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para pagos con PayPal.
 *
 * Flujo desde Android:
 *   1. POST /api/paypal/pagar        → recibe approvalUrl
 *   2. Abrir approvalUrl en WebView/navegador
 *   3. Usuario aprueba → PayPal redirige al returnUrl con paymentId y PayerID
 *   4. GET  /api/paypal/confirmar    → confirma y guarda en Historial
 */
@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;

    /**
     * PASO 1: Crear la orden de pago.
     * Android envía el itemId, tipo (CURSO/SERVICIO) y usuarioId.
     */
    @PostMapping("/pagar")
    public ResponseEntity<PaypalResponseDto> crearPago(@RequestBody PaypalRequestDto request) {
        PaypalResponseDto response = paypalService.crearPago(request);
        if ("failed".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * PASO 2: Confirmar el pago tras la aprobación del usuario.
     * PayPal redirige aquí con paymentId y PayerID como query params.
     * Android intercepta el deep link y llama a este endpoint.
     */
    @GetMapping("/confirmar")
    public ResponseEntity<PaypalResponseDto> confirmarPago(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("itemId") Integer itemId,
            @RequestParam("tipo") String tipo) {

        PaypalResponseDto response = paypalService.ejecutarPago(paymentId, payerId,
                usuarioId, itemId, tipo);

        if ("failed".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de cancelación: cuando el usuario cancela en PayPal.
     */
    @GetMapping("/cancelar")
    public ResponseEntity<PaypalResponseDto> cancelarPago() {
        return ResponseEntity.ok(
                new PaypalResponseDto(null, null, "cancelled", "Pago cancelado por el usuario")
        );
    }
}
