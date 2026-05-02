package org.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmailRecuperacion(String destinatario, String token) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Recupera tu contraseña - SaberShare");

            String link = "http://localhost:5173/reset-password?token=" + token;
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

            helper.setText(cuerpo, true);
            mailSender.send(mensaje);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email de recuperación", e);
        }
    }
}
