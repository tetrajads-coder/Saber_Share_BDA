package org.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarEmailRecuperacion(String destinatario, String token) {
        String link = "https://saber-share-webb.vercel.app/reset-password?token=" + token;

        String cuerpo = """
                <html>
                <body style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px;">
                    <h2 style="color: #4A90D9;">Recuperación de contraseña</h2>
                    <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta en
                       <strong>SaberShare</strong>.</p>
                    <p>Haz clic en el siguiente enlace para crear una nueva contraseña.
                       El enlace expira en <strong>30 minutos</strong>.</p>
                    <a href="%s"
                       style="display:inline-block; padding:12px 24px; background:#4A90D9;
                              color:#fff; text-decoration:none; border-radius:5px; margin:16px 0;">
                        Restablecer contraseña
                    </a>
                    <p style="color:#888; font-size:12px;">
                        Si no solicitaste este cambio, ignora este correo.
                    </p>
                </body>
                </html>
                """.formatted(link);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> body = Map.of(
                "from", "onboarding@resend.dev",
                "to", List.of(destinatario),
                "subject", "Recuperación de contraseña",
                "html", cuerpo
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity("https://api.resend.com/emails", request, String.class);
    }
}
