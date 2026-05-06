package org.example.demo.controler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatbotControler {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    private static final String ERROR_RESPONSE = "Lo siento, no puedo responder ahora. Intenta más tarde.";

    private static final String SYSTEM_PROMPT =
            "Eres el asistente virtual de SaberShare, una plataforma de marketplace de tutorías y cursos " +
            "en línea en México. Tu nombre es SaberBot. Los usuarios pueden ser Alumnos o Tutores llamados " +
            "Chalanes. Los Tutores publican Cursos grabados y Clases 1 a 1. Los pagos se procesan con PayPal. " +
            "Hay chat entre alumnos y tutores, sistema de agenda, historial de compras y calificaciones. " +
            "Responde siempre en español, sé amable y conciso, máximo 3 párrafos, usa emojis ocasionalmente.";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        String mensaje = body.get("mensaje");
        if (mensaje == null || mensaje.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("respuesta", ERROR_RESPONSE));
        }

        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", "llama-3.1-8b-instant",
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user",   "content", mensaje)
                    ),
                    "temperature", 0.7,
                    "max_tokens",  500
            ));

            System.out.println("=== GROQ REQUEST ===");
            System.out.println("Mensaje: " + mensaje);

            Request request = new Request.Builder()
                    .url(groqApiUrl)
                    .addHeader("Authorization", "Bearer " + groqApiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(okhttp3.RequestBody.create(requestBody, MediaType.get("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "(vacío)";

                System.out.println("=== GROQ RESPONSE ===");
                System.out.println("Status: " + response.code());
                System.out.println("Body: " + responseBody);

                if (!response.isSuccessful() || responseBody.equals("(vacío)")) {
                    return ResponseEntity.ok(Map.of("respuesta", ERROR_RESPONSE));
                }

                JsonNode root = objectMapper.readTree(responseBody);
                String texto = root
                        .path("choices").get(0)
                        .path("message")
                        .path("content").asText();

                return ResponseEntity.ok(Map.of("respuesta", texto));
            }
        } catch (Exception e) {
            System.out.println("=== GROQ ERROR ===");
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("respuesta", ERROR_RESPONSE));
        }
    }
}
