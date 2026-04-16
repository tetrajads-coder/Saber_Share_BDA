package org.example.demo.service.impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.dto.PaypalRequestDto;
import org.example.demo.dto.PaypalResponseDto;
import org.example.demo.model.Curso;
import org.example.demo.model.Historial;
import org.example.demo.model.Servicio;
import org.example.demo.model.Usuario;
import org.example.demo.repository.CursoRepository;
import org.example.demo.repository.HistorialRepository;
import org.example.demo.repository.ServicioRepository;
import org.example.demo.repository.UsuarioRepository;
import org.example.demo.service.PaypalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    private static final double COMISION_APP = 0.10; // 10% para Saber Share
    private static final double PARTE_VENDEDOR = 1.0 - COMISION_APP; // 90%

    private final APIContext         apiContext;
    private final CursoRepository    cursoRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository  usuarioRepository;
    private final HistorialRepository historialRepository;

    // ── Crear pago ────────────────────────────────────────────────────────────

    @Override
    public PaypalResponseDto crearPago(PaypalRequestDto request) {
        try {
            double precio = obtenerPrecio(request.getItemId(), request.getTipo());

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format(java.util.Locale.US, "%.2f", precio));

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Pago " + request.getTipo()
                    + " #" + request.getItemId() + " - Saber Share");

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setReturnUrl(request.getReturnUrl());
            redirectUrls.setCancelUrl(request.getCancelUrl());

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            payment.setRedirectUrls(redirectUrls);

            Payment createdPayment = payment.create(apiContext);

            String approvalUrl = createdPayment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .map(Links::getHref)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró URL de aprobación"));

            return new PaypalResponseDto(createdPayment.getId(), approvalUrl,
                    "created", "Pago creado exitosamente");

        } catch (PayPalRESTException e) {
            log.error("Error al crear pago PayPal: {}", e.getMessage());
            return new PaypalResponseDto(null, null, "failed",
                    "Error al crear el pago: " + e.getMessage());
        }
    }

    // ── Ejecutar pago + split ─────────────────────────────────────────────────

    @Override
    @Transactional
    public PaypalResponseDto ejecutarPago(String paymentId, String payerId,
                                          Integer usuarioId, Integer itemId, String tipo) {
        try {
            // 1. Ejecutar en PayPal
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution execution = new PaymentExecution();
            execution.setPayerId(payerId);
            Payment executed = payment.execute(apiContext, execution);

            if (!"approved".equals(executed.getState())) {
                return new PaypalResponseDto(paymentId, null, "failed",
                        "El pago no fue aprobado por PayPal");
            }

            // 2. Obtener precio y vendedor
            double precioTotal  = obtenerPrecio(itemId, tipo);
            double parteVendedor = Math.round(precioTotal * PARTE_VENDEDOR * 100.0) / 100.0;
            double comisionApp   = Math.round(precioTotal * COMISION_APP  * 100.0) / 100.0;

            Usuario vendedor = obtenerVendedor(itemId, tipo);

            // 3. Acumular saldo pendiente del vendedor
            if (vendedor != null) {
                double saldoActual = vendedor.getSaldoPendiente() != null
                        ? vendedor.getSaldoPendiente() : 0.0;
                vendedor.setSaldoPendiente(saldoActual + parteVendedor);
                usuarioRepository.save(vendedor);
                log.info("Saldo vendedor {} actualizado: +{} (total: {})",
                        vendedor.getIdUsuario(), parteVendedor, vendedor.getSaldoPendiente());
            }

            // 4. Registrar en Historial
            registrarHistorial(usuarioId, itemId, tipo, precioTotal, parteVendedor, comisionApp);

            log.info("Pago aprobado — total: {} | vendedor: {} ({}%) | app: {} ({}%)",
                    precioTotal, parteVendedor, (int)(PARTE_VENDEDOR * 100),
                    comisionApp, (int)(COMISION_APP * 100));

            return new PaypalResponseDto(paymentId, null, "approved",
                    String.format("Pago aprobado. Vendedor recibirá $%.2f USD", parteVendedor));

        } catch (PayPalRESTException e) {
            log.error("Error al ejecutar pago PayPal: {}", e.getMessage());
            return new PaypalResponseDto(null, null, "failed",
                    "Error al ejecutar el pago: " + e.getMessage());
        }
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private double obtenerPrecio(Integer itemId, String tipo) {
        if ("CURSO".equalsIgnoreCase(tipo)) {
            Curso c = cursoRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado: " + itemId));
            return c.getPreCur();
        } else if ("SERVICIO".equalsIgnoreCase(tipo) || "CLASE".equalsIgnoreCase(tipo)) {
            Servicio s = servicioRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + itemId));
            return s.getPrecioSer();
        }
        throw new IllegalArgumentException("Tipo inválido: " + tipo);
    }

    /** Obtiene el Usuario dueño de la publicación (para acreditarle el saldo) */
    private Usuario obtenerVendedor(Integer itemId, String tipo) {
        try {
            if ("CURSO".equalsIgnoreCase(tipo)) {
                Curso c = cursoRepository.findById(itemId).orElseThrow();
                return c.getUsuario(); // asume que Curso tiene @ManyToOne Usuario
            } else {
                Servicio s = servicioRepository.findById(itemId).orElseThrow();
                return s.getUsuario(); // asume que Servicio tiene @ManyToOne Usuario
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener vendedor para {} #{}: {}", tipo, itemId, e.getMessage());
            return null;
        }
    }

    private void registrarHistorial(Integer usuarioId, Integer itemId, String tipo,
                                    double precioTotal, double parteVendedor, double comisionApp) {
        Usuario comprador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Historial historial = new Historial();
        historial.setFechapago(LocalDate.now());
        historial.setPago(precioTotal);
        historial.setUsuario(comprador);

        if ("CURSO".equalsIgnoreCase(tipo)) {
            Curso curso = cursoRepository.findById(itemId).orElseThrow();
            historial.setCurso(curso);
        } else {
            Servicio servicio = servicioRepository.findById(itemId).orElseThrow();
            historial.setServicio(servicio);
        }

        historialRepository.save(historial);
    }
}