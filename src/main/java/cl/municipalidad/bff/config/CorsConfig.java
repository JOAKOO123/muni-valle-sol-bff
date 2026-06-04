package cl.municipalidad.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuracion de CORS del BFF.
 * Permite solicitudes cross-origin desde los origenes configurados.
 *
 * Patrones aplicados:

 * 

 *   - Configuration Pattern: centralizacion de configuracion de seguridad

 * 
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    /**
     * Registra las reglas de CORS permitiendo metodos estandar y credenciales
     * desde los origenes configurados en application.properties.
     *
     * @param registry registro de mapeos CORS de Spring MVC
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
