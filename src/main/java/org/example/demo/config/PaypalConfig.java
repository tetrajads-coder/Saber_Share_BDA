package org.example.demo.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de PayPal SDK.
 * Agrega en application.properties:
 *   paypal.client.id=TU_CLIENT_ID
 *   paypal.client.secret=TU_CLIENT_SECRET
 *   paypal.mode=sandbox   (o "live" en producción)
 */
@Configuration
public class PaypalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}
