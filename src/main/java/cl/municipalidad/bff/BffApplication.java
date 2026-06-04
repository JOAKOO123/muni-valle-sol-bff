package cl.municipalidad.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del BFF (Backend For Frontend) de Municipalidad Valle del Sol.
 * Punto de entrada de la aplicacion Spring Boot.
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class BffApplication {

    /**
     * Inicia la aplicacion Spring Boot.
     *
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(BffApplication.class, args);
    }
}