# Electromechanics Backend

Microservicios Spring Boot de Electromechanics App (DSY1105, Duoc UC). Atienden los datos del caso: colaboradores, sucursales, órdenes de trabajo con sus actividades, y marcaciones de jornada con GPS.

- Repositorio de la app Android: https://github.com/joe-munozd01/app-android

## Integrantes

| Integrante | Aportes |
|---|---|
| Nicolás Figueroa | `jornadas-ms`, `colaboradores-ms` y `sucursales-ms`, con sus pruebas y Dockerfile |
| José Muñoz | `servicios-ms` con sus pruebas, `docker-compose` y despliegue en Render |

## Tecnologías

- Java 21 y Spring Boot (Web, Data JPA, Validation)
- PostgreSQL
- Lombok
- JUnit 5 y Mockito
- Docker (un contenedor por microservicio) y Docker Compose
- Render (nube)

## Microservicios

| Microservicio | Puerto local | Responsabilidad | URL en Render |
|---|---|---|---|
| `colaboradores-ms` | 8081 | Técnicos y supervisores | https://colaboradores-ms.onrender.com |
| `sucursales-ms` | 8082 | Sucursales con sus coordenadas | https://sucursales-ms.onrender.com |
| `servicios-ms` | 8083 | Órdenes de trabajo y actividades | https://servicios-ms.onrender.com |
| `jornadas-ms` | 8084 | Marcaciones con GPS y aprobación de horas | https://jornadas-ms.onrender.com |

Comparten una base PostgreSQL, cada uno con sus propias tablas. Al iniciar con la base vacía, `colaboradores-ms`, `sucursales-ms` y `servicios-ms` cargan datos de prueba ficticios.

Render suspende los servicios gratuitos tras 15 minutos sin uso; la primera petición puede tardar cerca de un minuto en responder.

## Endpoints

### colaboradores-ms
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/colaboradores?rol=` | Lista los colaboradores, opcionalmente por rol (`EMPLEADO` o `SUPERVISOR`) |
| GET | `/api/colaboradores/{id}` | Obtiene un colaborador |
| POST | `/api/colaboradores` | Crea un colaborador; valida RUT, nombre, correo y rol |

### sucursales-ms
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/sucursales` | Lista las sucursales con sus coordenadas |
| GET | `/api/sucursales/{id}` | Obtiene una sucursal |
| POST | `/api/sucursales` | Crea una sucursal; valida nombre, dirección y coordenadas |

### servicios-ms
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/servicios?idEmpleado=&estado=` | Lista las órdenes de trabajo, con filtros opcionales |
| GET | `/api/servicios/{id}` | Obtiene una orden |
| POST | `/api/servicios` | Crea una orden en estado `Pendiente` |
| PUT | `/api/servicios/{id}/asignar?idEmpleado=` | Asigna un técnico a la orden |
| PUT | `/api/servicios/{id}/estado?estado=` | Cambia el estado (`Pendiente`, `En Proceso` o `Finalizado`) |
| GET | `/api/servicios/{id}/actividades` | Lista las actividades de una orden |
| POST | `/api/servicios/{id}/actividades` | Registra una actividad ejecutada |

### jornadas-ms
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/jornadas` | Registra una marcación de entrada o salida con sus coordenadas; queda `PENDIENTE` |
| GET | `/api/jornadas?idEmpleado=&estado=` | Lista las marcaciones, con filtros opcionales |
| GET | `/api/jornadas/{id}` | Obtiene una marcación |
| PUT | `/api/jornadas/{id}/aprobar?idSupervisor=` | Aprueba una marcación |

Códigos de respuesta: **201** al crear, **400** con datos inválidos, **404** si el recurso no existe y **409** si hay un duplicado (RUT, correo o dirección).

## Estructura de cada microservicio

```
<servicio>/
├── Dockerfile
└── src/
    ├── main/java/com/electromechanics/<servicio>/
    │   ├── entities/      tablas (JPA)
    │   ├── dto/           datos de entrada con sus validaciones
    │   ├── repository/    consultas con Spring Data JPA
    │   ├── services/      interfaz e implementación con la lógica
    │   ├── controller/    endpoints REST
    │   └── config/        datos de prueba iniciales
    └── test/              pruebas con JUnit y Mockito
```

## Cómo ejecutar en local

Requiere Docker y Docker Compose.

```bash
git clone https://github.com/joe-munozd01/electromechanics-backend.git
cd electromechanics-backend
cp .env.example .env
docker compose up -d --build
docker compose ps
```

La primera vez tarda varios minutos. Deben aparecer cinco contenedores en `running`: la base y los cuatro microservicios. Para comprobar que responden:

```bash
curl -s localhost:8081/api/colaboradores
curl -s localhost:8082/api/sucursales
curl -s localhost:8083/api/servicios
curl -s localhost:8084/api/jornadas
```

Detenerlos: `docker compose down`. Ver errores de un servicio: `docker compose logs <servicio>`.

También se puede ejecutar un microservicio por separado, con PostgreSQL disponible en `localhost:5432`:

```bash
cd jornadas-ms
./mvnw spring-boot:run
```

### Variables de entorno

| Variable | Descripción | Valor local por defecto |
|---|---|---|
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base | `electromechanics` |
| `DB_USER` | Usuario | `postgres` |
| `DB_PASSWORD` | Contraseña | `postgres` |
| `PORT` | Puerto HTTP (en Render lo asigna la plataforma) | el de cada microservicio |

El archivo `.env` con las credenciales no se sube al repositorio; `.env.example` es la plantilla.

## Pruebas unitarias

Cada microservicio tiene pruebas de la capa de servicio (con el repositorio simulado con Mockito) y del controlador (con MockMvc). No necesitan base de datos.

```bash
for s in colaboradores-ms sucursales-ms servicios-ms jornadas-ms; do
  (cd $s && ./mvnw test 2>&1 | grep -E "Tests run:|BUILD")
done
```

| Microservicio | Pruebas |
|---|---|
| `colaboradores-ms` | 10 |
| `sucursales-ms` | 9 |
| `servicios-ms` | 12 |
| `jornadas-ms` | 8 |

Cada uno debe terminar con `Failures: 0, Errors: 0` y `BUILD SUCCESS`.

## Despliegue en Render

- Una base **PostgreSQL** y **cuatro Web Services** de tipo Docker, en la misma región.
- Cada servicio usa su carpeta como *Root Directory*, su `Dockerfile` y la rama `develop`.
- Las variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` y `DB_PASSWORD` se configuran en Render y no se suben a GitHub.

## Git Flow

Ramas `main`, `develop`, `feature/backend/<nombre-apellido>` y `release/`. Todo cambio entra a `develop` por Pull Request y se publica en `main` con un tag de versión.
