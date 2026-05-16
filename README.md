# Municipalidad Valle del Sol — BFF (Backend for Frontend)

Capa intermediaria entre el frontend Next.js y los microservicios internos. Centraliza la autenticación JWT con cookies HttpOnly, transforma los datos de los microservicios y expone una API REST unificada al cliente.

---

## Tabla de Contenidos

- [Stack Tecnológico](#stack-tecnológico)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Patrones de Diseño](#patrones-de-diseño)
- [Estructura de Carpetas](#estructura-de-carpetas)
- [Endpoints REST](#endpoints-rest)
- [Git Flow](#git-flow)
- [Convenciones de Commits](#convenciones-de-commits)
- [Pruebas Unitarias](#pruebas-unitarias)
- [Seguridad](#seguridad)
- [Instalación y Uso](#instalación-y-uso)
- [Variables de Entorno](#variables-de-entorno)

---

## Stack Tecnológico

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 4.0.6 | Framework principal |
| Spring WebMVC | Incluido | API REST síncrona |
| Spring WebFlux / WebClient | Incluido | Comunicación reactiva con microservicios |
| JJWT | 0.11.5 | Parsing y validación de tokens JWT |
| Lombok | Incluido | Reducción de boilerplate |
| JUnit 5 | Incluido | Framework de pruebas unitarias |
| Mockito | Incluido | Mocking para pruebas unitarias |
| MockMvc | Incluido | Pruebas de integración web |
| Maven | Wrapper | Gestión de dependencias y build |

---

## Arquitectura del Proyecto

El BFF actúa como fachada entre el frontend y los microservicios, siguiendo una **arquitectura en capas** con responsabilidades bien delimitadas: