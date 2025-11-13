package org.example.demo.service.impl.init;

import org.example.demo.repository.UsuarioRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Bean
    public ApplicationRunner initializer(
            final UsuarioRepository usuarioRepository,
            final JdbcTemplate jdbcTemplate) {

        return args -> {
            // Revisa si ya existe algún dato en la tabla principal (Usuario)
            if (usuarioRepository.count() > 0) {
                logger.info("La base de datos ya contiene datos. No se reiniciarán los contadores.");
            } else {
                // Si no hay datos, significa que es una BD nueva o vacía.
                logger.warn("¡Base de datos vacía detectada! Reiniciando contadores de todas las tablas.");

                String[] tablesToTruncate = {
                        "Agenda",
                        "Curso",
                        "Historial",
                        "Metodo_de_Pago",
                        "OpinionServicio",
                        "OpinionesCurso",
                        "Servicio",
                        "Usuario"
                };

                // Desactiva llaves foráneas, trunca tablas y reactiva llaves
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

                for (String table : tablesToTruncate) {
                    logger.info("Truncando tabla: " + table);
                    jdbcTemplate.execute("TRUNCATE TABLE " + table);
                }

                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
                logger.info("Reinicio de base de datos completado.");
            }
        };
    }
}