package cl.municipalidad.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuracion de los clientes HTTP WebClient del BFF.
 * Define los beans para comunicarse con MS-Usuarios y MS-Reportes.
 *
 * Patrones aplicados:

 * 

 *   - Factory Pattern: creacion centralizada de clientes HTTP

 *   - Configuration Pattern: externalizacion de URLs en application.properties

 * 
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebClientConfig {

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    @Value("${ms.reportes.url}")
    private String msReportesUrl;

    /**
     * Crea el WebClient para el MS-Usuarios.
     *
     * @return WebClient configurado con la URL base del MS-Usuarios
     */
    @Bean
    public WebClient msUsuariosClient() {
        return WebClient.builder()
                .baseUrl(msUsuariosUrl)
                .build();
    }

    /**
     * Crea el WebClient para el MS-Reportes.
     *
     * @return WebClient configurado con la URL base del MS-Reportes
     */
    @Bean
    public WebClient msReportesClient() {
        return WebClient.builder()
                .baseUrl(msReportesUrl)
                .build();
    }
}
