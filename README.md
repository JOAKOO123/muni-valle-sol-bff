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
| JaCoCo | 0.8.12 | Reporte y validación de cobertura de código |
| Maven | Wrapper | Gestión de dependencias y build |

---

## Arquitectura del Proyecto

El BFF actúa como fachada entre el frontend y los microservicios, siguiendo una **arquitectura en capas** con responsabilidades bien delimitadas:

```
┌─────────────────────────────────────────────┐
│              Frontend (Next.js)              │
└────────────────────┬────────────────────────┘
                     │ HTTP + Cookie HttpOnly
┌────────────────────▼────────────────────────┐
│                   BFF                        │
│  ┌────────────┐  ┌──────────┐  ┌─────────┐  │
│  │ Controller │→ │ Service  │→ │ Client  │  │
│  └────────────┘  └──────────┘  └────┬────┘  │
└───────────────────────────────────── │ ──────┘
                     ┌─────────────────┘
          WebClient  │
     ┌───────────────┴──────────────┐
     │                              │
┌────▼────────┐           ┌─────────▼────┐
│ ms-usuarios │           │ ms-reportes  │
│  :8081      │           │   :8082      │
└─────────────┘           └─────────────┘
```

**Flujo de datos:**
1. El frontend hace una petición HTTP al BFF con la cookie `access_token`.
2. El Controller recibe la petición y delega al Service correspondiente.
3. El Service aplica la lógica de negocio (filtros, transformaciones, mapeos).
4. El Client encapsula la comunicación con el microservicio usando WebClient.
5. La respuesta se transforma al DTO que el frontend espera y se retorna.

---

## Patrones de Diseño

| Patrón | Dónde se aplica | Descripción |
|---|---|---|
| **BFF (Backend for Frontend)** | Proyecto completo | Una capa dedicada que adapta la API al contrato que necesita el frontend |
| **Facade** | `AuthService`, `ReportService`, `AlertService` | Los services ocultan la complejidad de comunicarse con múltiples microservicios |
| **DTO (Data Transfer Object)** | `dto/` | Objetos de transferencia que desacoplan el modelo interno del contrato externo |
| **Dependency Injection** | Toda la aplicación | Inyección de dependencias vía constructor con `@RequiredArgsConstructor` |
| **Global Exception Handler** | `GlobalExceptionHandler` | Centraliza el manejo de errores con `@RestControllerAdvice` |
| **Adapter** | `ReportService#toDTO()` | Convierte el modelo del microservicio (`ReportMsDTO`) al modelo del BFF (`ReportDTO`) |

---

## Estructura de Carpetas

```
src/
├── main/
│   ├── java/cl/municipalidad/bff/
│   │   ├── BffApplication.java          # Punto de entrada Spring Boot
│   │   ├── client/
│   │   │   ├── UserClient.java          # Comunicación con ms-usuarios
│   │   │   └── ReportClient.java        # Comunicación con ms-reportes
│   │   ├── config/
│   │   │   ├── CorsConfig.java          # Configuración de CORS
│   │   │   └── WebClientConfig.java     # Beans de WebClient por microservicio
│   │   ├── controller/
│   │   │   ├── AuthController.java      # Endpoints /api/auth
│   │   │   ├── AlertController.java     # Endpoints /api/alertas
│   │   │   └── ReportController.java    # Endpoints /api/reportes
│   │   ├── dto/
│   │   │   ├── AlertDTO.java
│   │   │   ├── LocationDTO.java
│   │   │   ├── LoginRequestDTO.java
│   │   │   ├── LoginResponseDTO.java
│   │   │   ├── RegisterRequestDTO.java
│   │   │   ├── ReportDTO.java
│   │   │   ├── ReportMsDTO.java         # DTO interno del microservicio de reportes
│   │   │   ├── TokenResponseDTO.java
│   │   │   └── UserDTO.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java  # Manejo centralizado de errores
│   │   │   └── MsException.java             # Excepción con HttpStatus del microservicio
│   │   └── service/
│   │       ├── AuthService.java         # Lógica de autenticación y registro
│   │       ├── AlertService.java        # Lógica de alertas (deriva de reportes activos)
│   │       └── ReportService.java       # Lógica de reportes + transformación de DTOs
│   └── resources/
│       ├── application.properties       # Configuración activa
│       └── application-example.properties
└── test/
    └── java/cl/municipalidad/bff/
        ├── BffApplicationTests.java
        ├── controller/
        │   ├── AlertControllerTest.java
        │   └── ReportControllerTest.java
        ├── exception/
        │   └── GlobalExceptionHandlerTest.java
        └── service/
            ├── AlertServiceTest.java
            ├── AuthServiceTest.java
            └── ReportServiceTest.java
```

---

## Endpoints REST

### Autenticación — `/api/auth`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `POST` | `/api/auth/login` | Autentica al usuario y setea la cookie `access_token` | `200 OK` con datos del usuario |
| `POST` | `/api/auth/register` | Registra un nuevo usuario | `201 Created` con `UserDTO` |
| `POST` | `/api/auth/logout` | Invalida la cookie HttpOnly | `204 No Content` |
| `GET` | `/api/auth/me` | Retorna el usuario autenticado según la cookie | `200 OK` con `UserDTO` / `401` si no hay token |

**Body login:**
```json
{
  "email": "usuario@ejemplo.cl",
  "password": "Segura123!"
}
```

**Body register:**
```json
{
  "nombre": "Juan Pérez",
  "email": "usuario@ejemplo.cl",
  "password": "Segura123!",
  "rol": "CIUDADANO"
}
```

---

### Reportes — `/api/reportes`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/api/reportes` | Lista todos los reportes | `200 OK` con array de `ReportDTO` |
| `GET` | `/api/reportes/activos` | Lista solo reportes con estado `ACTIVO` | `200 OK` con array de `ReportDTO` |
| `GET` | `/api/reportes/{id}` | Obtiene un reporte por ID | `200 OK` con `ReportDTO` |
| `POST` | `/api/reportes` | Crea un nuevo reporte | `201 Created` con `ReportDTO` |
| `PUT` | `/api/reportes/{id}/estado` | Actualiza el estado de un reporte | `200 OK` con `ReportDTO` |
| `PUT` | `/api/reportes/{id}` | Actualiza el título de un reporte | `200 OK` con `ReportDTO` |
| `DELETE` | `/api/reportes/{id}` | Elimina un reporte | `204 No Content` |

**ReportDTO:**
```json
{
  "id": 1,
  "titulo": "Incendio cerro",
  "descripcion": "Fuego activo en sector norte",
  "tipo": "INCENDIO",
  "estado": "ACTIVO",
  "emailUsuario": "juan@gmail.com",
  "ubicacion": { "lat": -33.4569, "lng": -70.6483 },
  "fechaCreacion": "2025-01-15T10:30:00"
}
```

---

### Alertas — `/api/alertas`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/api/alertas` | Lista alertas derivadas de reportes con estado `ACTIVO` | `200 OK` con array de `AlertDTO` |
| `POST` | `/api/alertas` | Crea una alerta manual | `201 Created` con `AlertDTO` |

**Mapeo de severidad por tipo de reporte:**

| Tipo | Severidad |
|---|---|
| `INCENDIO` | `ALTA` |
| `HUMO` | `MEDIA` |
| `SOSPECHOSO` | `BAJA` |
| Otro | `MEDIA` (por defecto) |

---

### Manejo de Errores

Todos los errores retornan la siguiente estructura:

```json
{
  "error": "Mensaje descriptivo del error",
  "status": 500,
  "timestamp": "2025-01-15T10:30:00"
}
```

| Excepción | Código HTTP |
|---|---|
| `MsException` | El que define el microservicio (`4xx`, `5xx`) |
| `RuntimeException` | `500 Internal Server Error` |

---

## Git Flow

Este proyecto sigue **Git Flow** adaptado:

```
main          ← producción estable
  └── develop ← integración continua
        ├── feature/HU-XXX-descripcion   ← nuevas funcionalidades
        ├── fix/descripcion-del-bug      ← correcciones
        ├── chore/descripcion            ← tareas técnicas (dependencias, config)
        └── docs/descripcion             ← documentación
```

**Reglas:**
- Nunca hacer push directo a `main` o `develop`.
- Toda rama parte desde `develop` y se integra via Pull Request.
- El PR requiere que el build de `mvnw verify` pase (tests + cobertura).

---

## Convenciones de Commits

Se sigue la especificación **Conventional Commits**:

```
<tipo>: <descripción en imperativo, minúsculas>
```

| Tipo | Cuándo usarlo |
|---|---|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de un bug |
| `chore` | Tareas técnicas sin cambio de lógica (dependencias, config, build) |
| `docs` | Cambios en documentación |
| `test` | Agregar o modificar pruebas |
| `refactor` | Cambio de código sin fix ni feature |

**Ejemplos:**
```bash
git commit -m "feat: implementar endpoint /api/auth/me con validación JWT"
git commit -m "fix: corregir llamada a endpoint email en UserClient"
git commit -m "chore: agregar jacoco para reporte y validación de cobertura"
git commit -m "test: agregar pruebas unitarias para AlertService"
```

---

## Pruebas Unitarias

El proyecto cuenta con **49 pruebas unitarias** distribuidas en 6 clases de test.

### Ejecutar las pruebas

```bash
# Solo tests
.\mvnw.cmd test

# Tests + reporte de cobertura JaCoCo
.\mvnw.cmd verify
```

El reporte HTML de cobertura queda disponible en:
```
target/site/jacoco/index.html
```

### Cobertura mínima exigida

JaCoCo valida automáticamente que la cobertura de líneas sea **≥ 80%** sobre las clases de lógica de negocio. Si la cobertura cae por debajo, el build falla en fase `verify`.

**Clases excluidas del check** (sin lógica de negocio):

| Clase / Paquete | Razón |
|---|---|
| `BffApplication` | Punto de entrada de Spring Boot |
| `config/**` | Solo configuración de beans |
| `dto/**` | Records Java, solo datos |
| `exception/MsException` | Excepción simple generada por Lombok |
| `client/**` | Wrappers de WebClient, requieren tests de integración |
| `controller/AuthController` | Pendiente de prueba unitaria |

### Suite de pruebas

| Clase de Test | Pruebas | Qué cubre |
|---|---|---|
| `AuthServiceTest` | 7 | `login()`, `register()`, `getUser()` y sus interacciones con `UserClient` |
| `AlertServiceTest` | 11 | `listAlerts()` con filtros, mapeo de severidad y `create()` |
| `ReportServiceTest` | 11 | CRUD completo delegado a `ReportClient` y transformación de DTOs |
| `AlertControllerTest` | — | Endpoints `GET /api/alertas` y `POST /api/alertas` vía MockMvc |
| `ReportControllerTest` | 9 | Todos los endpoints de `/api/reportes` vía MockMvc |
| `GlobalExceptionHandlerTest` | 5 | Manejo de `RuntimeException` y `MsException` con distintos status HTTP |

---

## Seguridad

### Cookie HttpOnly

El token JWT **nunca se expone en el cuerpo de la respuesta** ni es accesible por JavaScript. Se almacena exclusivamente en una cookie con las siguientes flags:

| Flag | Valor | Propósito |
|---|---|---|
| `HttpOnly` | `true` | Inaccesible desde JavaScript (mitiga XSS) |
| `Secure` | `true` | Solo se envía por HTTPS |
| `SameSite` | `Strict` | Bloquea envío en peticiones cross-site (mitiga CSRF) |
| `Path` | `/` | Válida para toda la aplicación |
| `MaxAge` | `86400` | Expira en 24 horas (configurable via `JWT_COOKIE_MAXAGE`) |

### Validación JWT en `/api/auth/me`

El endpoint `me` extrae el email del claim `sub` del token, verifica la firma con la clave secreta configurada y consulta al microservicio de usuarios. Si el token es inválido o está ausente, retorna `401 Unauthorized`.

### CORS

Solo el origen configurado en `CORS_ORIGINS` puede hacer peticiones con credenciales (`allowCredentials: true`). Por defecto: `http://localhost:3000`.

---

## Instalación y Uso

### Prerequisitos

- Java 17+
- Maven (o usar el wrapper incluido `mvnw` / `mvnw.cmd`)
- Los microservicios `ms-usuarios` y `ms-reportes` levantados

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/JOAKOO123/muni-valle-sol-bff.git
cd muni-valle-sol-bff

# 2. Ir a la rama de desarrollo
git checkout develop

# 3. Copiar el archivo de ejemplo y configurar variables
cp src/main/resources/application-example.properties src/main/resources/application.properties
# Editar application.properties con tus valores

# 4. Compilar y ejecutar tests
.\mvnw.cmd verify        # Windows
./mvnw verify            # Linux / macOS

# 5. Levantar la aplicación
.\mvnw.cmd spring-boot:run    # Windows
./mvnw spring-boot:run        # Linux / macOS
```

El BFF quedará disponible en `http://localhost:8080`.

---

## Variables de Entorno

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `MS_USUARIOS_URL` | `http://localhost:8081` | URL base del microservicio de usuarios |
| `MS_REPORTES_URL` | `http://localhost:8082` | URL base del microservicio de reportes |
| `CORS_ORIGINS` | `http://localhost:3000` | Origen permitido para peticiones CORS |
| `JWT_SECRET` | `mi-clave-secreta-super-segura-de-al-menos-32-chars` | Clave para firmar y validar tokens JWT — **cambiar en producción** |
| `JWT_COOKIE_MAXAGE` | `86400` | Duración de la cookie en segundos (86400 = 24 horas) |

> **Nota:** En producción, nunca dejar el valor por defecto de `JWT_SECRET`. Usar una clave de al menos 32 caracteres aleatorios.