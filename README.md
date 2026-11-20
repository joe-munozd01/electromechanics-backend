# Electromechanics App

Aplicación móvil Android para el registro de jornada con GPS y el seguimiento de tareas de mantención en terreno, conectada a microservicios Spring Boot desplegados en la nube. Asignatura DSY1105 (Desarrollo de Aplicaciones Móviles), Duoc UC.

**Versión:** `v2.0` (Experiencia 3)

## Integrantes

| Integrante | Aportes principales |
|---|---|
| Nicolás Figueroa | `jornadas-ms`, `colaboradores-ms` y `sucursales-ms` (con JUnit y Docker), base de Retrofit, marcación e historial conectados al backend, panel del Supervisor (aprobar horas y ver ubicación), diagramas de arquitectura |
| José Muñoz | `servicios-ms`, `docker-compose`, login por correo con rol, Hoja de ruta, Detalle y Formulario conectados al backend, despliegue en Render, APK firmada |

## Enlaces

- Repositorio del backend: https://github.com/joe-munozd01/electromechanics-backend
- Diagramas de arquitectura: rama `documents`, archivo `docs/arquitectura.md`
- Trello: https://trello.com/invite/b/6ad24baede7497f7b1c32d21/ATTI707800078ce35328d03017c47482555eEBE071F9/electromechanics
- APK firmada: sección **Releases** de este repositorio (`app-release.apk`)

## Tecnologías

- Kotlin, Jetpack Compose y Material Design 3
- Arquitectura MVVM
- Room (SQLite) para la autenticación y la copia local de datos
- Retrofit y OkHttp para consumir los microservicios
- Navigation Compose, con navegación según el rol
- Recursos nativos: GPS, vibración, batería y mapa

## Roles

| Rol | Funciones | Recursos nativos |
|---|---|---|
| Técnico (`EMPLEADO`) | Marcar entrada y salida, hoja de ruta, cambiar el estado de un servicio, registrar actividades, historial de marcaciones | GPS, vibración, batería |
| Supervisor (`SUPERVISOR`) | Ver las jornadas pendientes, aprobarlas y ver en el mapa dónde se marcaron | Mapa |

Al iniciar sesión, la app lee el rol del usuario desde Room y abre el menú que le corresponde.

## Usuarios de prueba

Todos los datos son ficticios.

| Rol | Correo | Contraseña |
|---|---|---|
| Técnico | `tecnico1@demo.cl` | `Demo1234` |
| Técnico | `tecnico2@demo.cl` | `Demo1234` |
| Supervisor | `supervisor@demo.cl` | `Demo1234` |

## Microservicios

| Microservicio | URL en Render | Endpoint de prueba |
|---|---|---|
| `colaboradores-ms` | https://colaboradores-ms.onrender.com | `/api/colaboradores` |
| `sucursales-ms` | https://sucursales-ms.onrender.com | `/api/sucursales` |
| `servicios-ms` | https://servicios-ms.onrender.com | `/api/servicios` |
| `jornadas-ms` | https://jornadas-ms.onrender.com | `/api/jornadas` |

Los servicios usan el plan gratuito de Render, que los suspende tras 15 minutos sin uso. La primera petición puede tardar cerca de un minuto en responder; la app tiene tiempos de espera de 90 segundos por ese motivo.

## Cómo ejecutar

**Con el APK firmado**

Con el emulador encendido o el celular conectado por USB, desde la carpeta donde está el APK:

**Linux:**
```bash
~/Android/Sdk/platform-tools/adb install -r app-release.apk
```

**Windows (PowerShell):**
```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" install -r app-release.apk
```

Si `adb` está en el PATH, basta con `adb install -r app-release.apk` en ambos sistemas. También se puede arrastrar el archivo `.apk` a la ventana del emulador.

**Desde el código**
1. Clonar el repositorio y abrir la carpeta en Android Studio.
2. Esperar la sincronización de Gradle.
3. Ejecutar en un emulador o dispositivo con Android 8.0 (API 26) o superior.

Para la marcación en el emulador, fijar una ubicación en **Extended controls → Location → Set location**.

## Cómo probar cada funcionalidad

**Técnico** (`tecnico1@demo.cl`)
1. **Marcar jornada → MARCAR ENTRADA**: pide el permiso de ubicación, guarda la marcación en `jornadas-ms` con hora y coordenadas, y vibra.
2. **Mi hoja de ruta**: lista los servicios asignados desde `servicios-ms`.
3. **Detalle de servicio**: los botones se habilitan según el estado; al cambiarlo se actualiza el backend y el chip cambia de color con animación.
4. **Registrar actividades**: el campo de detalle es obligatorio y exige al menos 10 caracteres; la actividad se guarda en `servicios-ms`.
5. **Historial**: muestra las marcaciones del técnico y el nivel de batería.

**Supervisor** (`supervisor@demo.cl`)
1. **Aprobar horas**: lista las jornadas pendientes con el nombre del técnico (desde `colaboradores-ms`).
2. **VER UBICACIÓN**: abre el mapa en las coordenadas de la marcación.
3. **APROBAR**: la jornada queda aprobada en `jornadas-ms` y desaparece de la lista.

Si no hay conexión, la app guarda la información en Room y sigue funcionando con los datos locales.

## Estructura del proyecto

| Package | Función |
|---|---|
| `data/local` | Entidades y DAO de Room |
| `data/database` | `AppDatabase` con los datos de prueba |
| `data/remote` | Cliente Retrofit, URLs, interfaces de las APIs y DTO |
| `data/repository` | Une Retrofit con Room |
| `data/session` | Usuario con la sesión iniciada y su rol |
| `model` | Estado de cada pantalla |
| `ui/screen` | Pantallas en Compose |
| `ui/viewmodel` | Lógica de cada pantalla |
| `ui/navigation` | Rutas y navegación por rol |
| `ui/components` y `ui/theme` | Componentes reutilizables y tema Material 3 |
| `utils` | Validación de RUT |

## Git Flow

Ramas `main`, `develop`, `feature/<capa>/<nombre-apellido>`, `release/` y `documents`. Todo cambio entra a `develop` por Pull Request y se publica en `main` con un tag de versión.
