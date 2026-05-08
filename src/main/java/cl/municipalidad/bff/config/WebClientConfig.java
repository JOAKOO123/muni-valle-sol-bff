package cl.municipalidad.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    @Value("${ms.reportes.url}")
    private String msReportesUrl;

    @Bean
    public WebClient msUsuariosClient() {
        return WebClient.builder()
                .baseUrl(msUsuariosUrl)
                .build();
    }

    @Bean
    public WebClient msReportesClient() {
        return WebClient.builder()
                .baseUrl(msReportesUrl)
                .build();
    }
}
