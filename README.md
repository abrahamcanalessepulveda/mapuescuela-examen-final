# MapuEscuela - MVP Final

Proyecto desarrollado para la asignatura **Integración de Plataformas**.

Esta versión corresponde al **MVP final de MapuEscuela**, una aplicación web que integra una interfaz para clientes y administración, servicios desarrollados con Spring Boot, una base de datos MySQL y un proceso BPMN ejecutado mediante Flowable.

El sistema permite gestionar el proceso principal de venta de productos de MapuEscuela, desde el registro del cliente y generación del pedido hasta la validación del pago, preparación y entrega mediante retiro o despacho.

---

## Integrantes

**Grupo 1**

- Alexis Rosales
- Camilo Quezada
- Abraham Canales

---

## Objetivo del proyecto

El objetivo del proyecto es desarrollar una solución que permita apoyar y automatizar las principales etapas del proceso de venta de MapuEscuela.

El MVP permite:

- Registrar clientes.
- Iniciar sesión en el Portal de Clientes.
- Recuperar la contraseña mediante correo electrónico.
- Consultar productos disponibles.
- Generar pedidos.
- Seleccionar retiro en local o despacho.
- Adjuntar comprobantes de pago.
- Consultar los pedidos asociados al cliente autenticado.
- Revisar pedidos desde el panel administrativo.
- Validar comprobantes.
- Aprobar o rechazar pagos.
- Actualizar el inventario.
- Preparar pedidos.
- Registrar retiros.
- Registrar despachos.
- Registrar números de seguimiento.
- Confirmar entregas.
- Cancelar pedidos cuando corresponde.

El proceso se encuentra integrado con Flowable mediante un modelo BPMN que controla las distintas etapas de la venta.

---

## Arquitectura general

MapuEscuela utiliza distintos componentes que trabajan de forma integrada.

### Interfaz web

La interfaz fue desarrollada utilizando:

- HTML
- CSS
- JavaScript

Se dispone de dos áreas principales:

- **Portal de Clientes**
- **Panel Administrativo**

### Backend

La aplicación utiliza:

- Java 17
- Spring Boot 4.0.8
- Servicios REST
- Spring Data JPA
- Spring Security
- Spring Mail

Spring Boot contiene la lógica principal de la aplicación y permite comunicar la interfaz con MySQL y Flowable.

Spring Mail permite enviar correos electrónicos para la recuperación de contraseña de los clientes mediante un servidor SMTP.

### Base de datos

La información del sistema se almacena utilizando:

- MySQL 8

### Motor de procesos

El proceso BPMN se ejecuta utilizando:

- Flowable 6.8.0
- Docker

De forma simplificada, la arquitectura corresponde a:

```text
Portal de Clientes / Panel Administrativo
                  |
                  v
              Spring Boot
               /       \
              v         v
           MySQL     Flowable REST
                       |
                       v
                   Proceso BPMN
```

Además, Spring Boot incorpora un External Worker que procesa la actividad de confirmación del pago y actualización del stock.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 4.0.8
- Spring Data JPA
- Spring Security
- Spring Mail
- Maven Wrapper
- MySQL 8
- HTML
- CSS
- JavaScript
- REST
- SMTP
- BPMN
- Flowable 6.8.0
- Docker
- Git
- GitHub
- Visual Studio Code

---

# Funcionalidades del MVP

## Portal de Clientes

El Portal de Clientes permite al usuario realizar las principales operaciones relacionadas con una compra.

Entre sus funciones se encuentran:

- Registrar una nueva cuenta.
- Iniciar sesión.
- Recuperar la contraseña mediante correo electrónico.
- Consultar el catálogo de productos.
- Generar un pedido.
- Seleccionar la modalidad de entrega.
- Consultar sus propios pedidos.
- Adjuntar un comprobante de pago.
- Cerrar la sesión.

La información de pedidos se encuentra asociada a la sesión del cliente autenticado.

De esta forma, cada cliente puede consultar únicamente los pedidos correspondientes a su propia cuenta.

### Recuperación de contraseña

El Portal de Clientes incorpora la opción **¿Olvidaste tu contraseña?**.

El cliente puede ingresar el correo electrónico registrado en MapuEscuela para solicitar la recuperación de acceso.

Cuando la solicitud es procesada:

1. La aplicación busca una cuenta asociada al correo ingresado.
2. Se genera automáticamente una contraseña temporal aleatoria.
3. La contraseña temporal se cifra antes de almacenarse en la base de datos.
4. La contraseña temporal se envía al correo registrado mediante SMTP.
5. El cliente puede utilizar la nueva contraseña para volver a iniciar sesión.

La aplicación muestra un mensaje genérico después de procesar la solicitud, evitando informar directamente si una dirección de correo determinada se encuentra registrada.

Para el envío de los correos se utiliza Spring Mail y un servidor SMTP de Gmail.

Las credenciales del correo no se almacenan directamente en el código fuente ni en el repositorio.

---

## Panel Administrativo

El sistema cuenta con un acceso administrativo separado del Portal de Clientes.

Desde el panel administrativo se puede acceder a las principales funciones de gestión.

### Gestión de pedidos

Permite:

- Consultar los pedidos registrados.
- Filtrar pedidos según su estado.
- Revisar el detalle de un pedido.
- Consultar comprobantes.
- Aprobar o rechazar pagos.
- Registrar motivos de rechazo.
- Preparar pedidos.
- Continuar el proceso mediante retiro o despacho.
- Confirmar entregas.

### Inventario

La sección de inventario permite:

- Consultar productos.
- Revisar descripción y categoría.
- Consultar precios.
- Consultar stock.
- Revisar disponibilidad.
- Registrar productos.
- Actualizar productos existentes.

### Clientes

La sección de clientes permite:

- Consultar clientes registrados.
- Registrar clientes.
- Actualizar información de clientes.

---

# Seguridad y sesiones

El Portal de Clientes y el Panel Administrativo utilizan sesiones separadas.

Cuando un cliente inicia sesión, el sistema identifica su cuenta y limita las consultas de pedidos a los registros asociados a ese cliente.

Cuando se inicia una sesión administrativa, la sesión de cliente es eliminada.

Del mismo modo, al volver a iniciar una sesión como cliente se elimina la sesión administrativa.

Esto permite separar las funciones administrativas de las operaciones disponibles para los clientes.

Las contraseñas utilizadas por la aplicación no se almacenan directamente en el repositorio.

Las contraseñas de los clientes se almacenan cifradas mediante el mecanismo de codificación de contraseñas utilizado por Spring Security.

En el proceso de recuperación, la contraseña temporal se envía al correo del cliente y únicamente su versión cifrada queda almacenada en la base de datos.

Las credenciales utilizadas para MySQL, administración y correo electrónico se proporcionan mediante variables de entorno.

---

# Proceso BPMN

El proceso BPMN representa el flujo principal de venta de MapuEscuela.

La versión final utilizada por el proyecto es:

```text
bpmn/Proceso_de_venta_-_Mapuescuela_U3_v9.bpmn20.xml
```

La clave del proceso utilizada por Spring Boot es:

```text
procesoVentaMapuescuelaV3
```

La versión final desplegada corresponde a:

```text
version: 9
```

## Flujo general

El proceso funciona de manera general de la siguiente forma:

1. El cliente inicia una solicitud de compra.
2. Se registran los datos de compra.
3. Se genera el pedido.
4. Se entregan los datos para realizar la transferencia.
5. El cliente adjunta el comprobante.
6. Se revisa el comprobante.
7. El pago puede ser aprobado o rechazado.
8. Si el pago es aprobado, se actualiza el inventario.
9. Se prepara el pedido.
10. El proceso continúa según la modalidad seleccionada.
11. El pedido puede continuar mediante retiro en local o despacho.
12. Se registra la entrega.
13. El proceso finaliza.

---

## Temporizador de 24 horas

El proceso incorpora un temporizador:

```text
PT24H
```

Este representa el plazo disponible para adjuntar el comprobante de pago.

Si el comprobante no es ingresado dentro del tiempo establecido, el proceso puede continuar mediante la ruta de cancelación.

Durante las pruebas del proyecto también se comprobó esta ruta mediante la ejecución controlada del Timer Job de Flowable.

---

# External Worker

El proceso utiliza un External Worker para la actividad:

```text
Confirmar pago y descontar stock
```

El tópico utilizado es:

```text
confirmarPago
```

El componente implementado en Spring Boot es:

```text
ConfirmarPagoWorker
```

Cuando Flowable llega a esta actividad se genera un trabajo externo.

`ConfirmarPagoWorker` consulta periódicamente los trabajos pendientes asociados al tópico `confirmarPago`.

Cuando encuentra uno, utiliza la variable:

```text
idComprobante
```

para identificar el comprobante que debe ser procesado.

Una vez realizada correctamente la operación, el Worker informa a Flowable que el trabajo fue completado y el proceso puede continuar.

El Worker se inicia automáticamente junto con la aplicación Spring Boot y no requiere ejecutarse como un programa separado.

---

# Variables principales de Flowable

Entre las principales variables utilizadas durante el proceso se encuentran:

- `idPedido`
- `idComprobante`
- `modalidadEntrega`
- `resultadoValidacion`
- `motivoRechazo`

### idPedido

Permite relacionar la instancia del proceso con el pedido almacenado en MySQL.

### idComprobante

Permite identificar el comprobante utilizado durante la validación del pago.

### modalidadEntrega

Permite determinar si el pedido continuará mediante:

- RETIRO
- DESPACHO

### resultadoValidacion

Permite controlar el resultado de la revisión del comprobante.

### motivoRechazo

Permite almacenar el motivo ingresado cuando un comprobante es rechazado.

---

# Base de datos

La aplicación utiliza:

```text
MySQL 8
```

La base de datos se denomina:

```text
mapuescuela
```

Las tablas principales son:

- `cliente`
- `producto`
- `pedido`
- `detalle_pedido`
- `comprobante_pago`
- `despacho`

---

## Script de instalación

El repositorio incluye:

```text
database/mapuescuela.sql
```

Este archivo corresponde al script de **instalación limpia** de la base de datos.

El script:

- Crea la base de datos `mapuescuela`.
- Crea las tablas necesarias.
- Configura las relaciones entre las tablas.
- Incorpora seis productos iniciales para realizar pruebas.

El script no incluye los clientes, pedidos, comprobantes o despachos históricos utilizados durante el desarrollo.

Los nuevos clientes pueden registrarse directamente desde el Portal de Clientes.

> **Importante:** el script elimina y vuelve a crear la base de datos `mapuescuela`. No debe ejecutarse sobre una base que contenga información que se quiera conservar.

---

# Manual de instalación

## 1. Requisitos previos

Para ejecutar MapuEscuela se requiere:

- Windows 10 u 11.
- Java 17.
- MySQL 8.
- Docker Desktop.
- Git.
- PowerShell.

Visual Studio Code puede utilizarse como entorno de desarrollo, pero no es obligatorio para ejecutar la aplicación.

El proyecto incluye Maven Wrapper:

```text
mvnw.cmd
```

por lo que no es necesario instalar Maven de forma independiente.

---

## 2. Obtener el proyecto

El repositorio correspondiente a la entrega final es:

```text
https://github.com/abrahamcanalessepulveda/mapuescuela-examen-final
```

Puede clonarse mediante:

```powershell
git clone https://github.com/abrahamcanalessepulveda/mapuescuela-examen-final.git
```

Ingresar posteriormente a la carpeta:

```powershell
cd mapuescuela-examen-final
```

---

## 3. Crear la base de datos

Con MySQL 8 instalado, desde la carpeta raíz del proyecto ejecutar:

```powershell
cmd /c """C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"" -u root -p --default-character-set=utf8mb4 < ""database\mapuescuela.sql"""
```

MySQL solicitará la contraseña del usuario `root`.

La importación se realiza directamente desde el archivo para conservar correctamente la codificación UTF-8 y los caracteres especiales.

La configuración predeterminada de la aplicación utiliza:

```text
Base de datos: mapuescuela
Servidor: localhost
Puerto: 3306
Usuario: root
```

La contraseña no se almacena en el repositorio.

---

# Instalación de Flowable mediante Docker

El proyecto utiliza dos contenedores:

| Componente | Imagen | Puerto |
|---|---|---:|
| Flowable UI | `flowable/flowable-ui:6.8.0` | 8081 |
| Flowable REST | `flowable/flowable-rest:6.8.0` | 8082 |

---

## 4. Crear la red Docker

Ejecutar:

```powershell
docker network create mapuescuela-net
```

Si la red ya existe, no es necesario crearla nuevamente.

---

## 5. Crear Flowable UI

Ejecutar:

```powershell
docker run -d `
  --name flowable-mapuescuela `
  --network mapuescuela-net `
  -p 8081:8080 `
  -e JAVA_OPTS="-Xmx1024M" `
  flowable/flowable-ui:6.8.0
```

Flowable UI quedará disponible en:

```text
http://localhost:8081/flowable-ui/
```

---

## 6. Crear Flowable REST

Ejecutar:

```powershell
docker run -d `
  --name flowable-rest-mapuescuela `
  --network mapuescuela-net `
  -p 8082:8080 `
  -e JAVA_OPTS="-Xmx512M" `
  flowable/flowable-rest:6.8.0
```

Flowable REST quedará disponible en:

```text
http://localhost:8082/flowable-rest/service
```

---

## 7. Comprobar los contenedores

Ejecutar:

```powershell
docker ps
```

Deben aparecer:

```text
flowable-mapuescuela
flowable-rest-mapuescuela
```

---

## 8. Configurar Flowable Admin

Para permitir que Flowable UI se comunique con Flowable REST, ingresar a Flowable Admin.

La conexión al Process Engine debe utilizar:

```text
Server address: http://flowable-rest-mapuescuela
Server port: 8080
Context root: /flowable-rest
REST root: service
Username: rest-admin
```

La contraseña predeterminada utilizada por Flowable REST en el entorno del proyecto es:

```text
test
```

La comunicación utiliza el nombre del contenedor porque ambos servicios se encuentran conectados a:

```text
mapuescuela-net
```

---

## 9. Desplegar el proceso BPMN

Antes de realizar una prueba completa del sistema debe encontrarse desplegado en Flowable el archivo:

```text
bpmn/Proceso_de_venta_-_Mapuescuela_U3_v9.bpmn20.xml
```

La clave esperada por Spring Boot es:

```text
procesoVentaMapuescuelaV3
```

La versión utilizada durante las pruebas finales corresponde a V9.

---

# Configuración de Spring Boot

## 10. Variables de entorno

Antes de iniciar la aplicación deben configurarse las contraseñas locales necesarias.

En PowerShell:

```powershell
$env:DB_PASSWORD="CONTRASENA_MYSQL"
$env:ADMIN_PASSWORD="CONTRASENA_ADMIN"
```

También pueden configurarse opcionalmente:

```powershell
$env:DB_USERNAME="root"
$env:FLOWABLE_URL="http://localhost:8082/flowable-rest/service"
$env:FLOWABLE_USERNAME="rest-admin"
$env:FLOWABLE_PASSWORD="test"
$env:ADMIN_USERNAME="admin"
```

### Configuración del correo para recuperación de contraseña

Para habilitar el envío de contraseñas temporales por correo electrónico deben configurarse adicionalmente:

```powershell
$env:MAIL_USERNAME="CORREO_GMAIL"
$env:MAIL_PASSWORD="CONTRASENA_DE_APLICACION_GMAIL"
```

`MAIL_USERNAME` corresponde a la cuenta Gmail utilizada como remitente.

`MAIL_PASSWORD` debe contener una contraseña de aplicación válida para el acceso SMTP de la cuenta configurada.

La aplicación utiliza la siguiente configuración SMTP:

```text
Servidor: smtp.gmail.com
Puerto: 587
Autenticación: habilitada
STARTTLS: habilitado
```

Las variables de correo son necesarias únicamente para utilizar la función de recuperación de contraseña. Si no se configuran, el resto del MVP puede iniciar y funcionar normalmente, pero no será posible realizar el envío del correo de recuperación.

Las credenciales reales utilizadas durante el desarrollo no se almacenan en el repositorio.

No se deben escribir contraseñas reales directamente en `application.properties` ni incorporarlas a GitHub.

---

## 11. Compilar

Desde la carpeta raíz del proyecto:

```powershell
.\mvnw.cmd clean compile
```

---

## 12. Ejecutar Spring Boot

Ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

Si el inicio es correcto, la aplicación estará disponible en:

```text
http://localhost:8080
```

Al iniciar Spring Boot también comienza automáticamente `ConfirmarPagoWorker`.

---

# Acceso al sistema

## Portal de Clientes

Dirección:

```text
http://localhost:8080/
```

Desde este portal es posible:

- Registrar una cuenta.
- Iniciar sesión.
- Recuperar la contraseña mediante correo electrónico.
- Consultar productos.
- Realizar pedidos.
- Consultar los pedidos asociados a la cuenta.
- Adjuntar comprobantes.
- Cerrar sesión.

### Recuperar contraseña

Desde la pantalla de acceso seleccionar:

```text
¿Olvidaste tu contraseña?
```

Ingresar el correo electrónico registrado y seleccionar:

```text
Enviar contraseña temporal
```

Si la cuenta se encuentra registrada y el correo se puede enviar correctamente, el cliente recibirá una nueva contraseña temporal.

La contraseña temporal recibida puede utilizarse posteriormente para iniciar sesión en el Portal de Clientes.

---

## Panel Administrativo

Dirección:

```text
http://localhost:8080/admin.html
```

Las credenciales administrativas se configuran mediante:

```text
ADMIN_USERNAME
ADMIN_PASSWORD
```

Si no se especifica `ADMIN_USERNAME`, el usuario predeterminado es:

```text
admin
```

La contraseña debe configurarse antes de iniciar la aplicación.

---

# Servicios utilizados

Una instalación completa utiliza:

| Servicio | Dirección |
|---|---|
| MapuEscuela | `http://localhost:8080` |
| Flowable UI | `http://localhost:8081/flowable-ui/` |
| Flowable REST | `http://localhost:8082/flowable-rest/service` |
| MySQL | `localhost:3306` |
| Gmail SMTP | `smtp.gmail.com:587` |

---

# Pruebas realizadas

Durante el desarrollo se probaron diferentes situaciones del proceso.

Entre ellas:

- Registro e inicio de sesión de clientes.
- Recuperación de contraseña desde el Portal de Clientes.
- Generación automática de una contraseña temporal.
- Envío de la contraseña temporal mediante Gmail SMTP.
- Inicio de sesión utilizando la contraseña temporal recibida.
- Separación entre sesión de cliente y administrador.
- Consulta de pedidos correspondientes únicamente al cliente autenticado.
- Generación de pedidos.
- Compra aprobada con retiro.
- Compra aprobada con despacho.
- Carga de comprobantes.
- Visualización de comprobantes.
- Aprobación de pagos.
- Rechazo de pagos.
- Registro del motivo de rechazo.
- Actualización de inventario.
- Conservación del stock cuando el pago no es aprobado.
- Preparación de pedidos.
- Registro de retiro.
- Registro de despacho.
- Registro de número de seguimiento.
- Confirmación de entrega.
- Cancelación mediante temporizador.
- Ejecución del External Worker `confirmarPago`.
- Finalización completa del proceso.

También se realizaron pruebas completas de principio a fin para comprobar la integración entre:

```text
Interfaz
Spring Boot
MySQL
Flowable
```

Adicionalmente, se comprobó el funcionamiento de la recuperación de contraseña desde la interfaz web hasta la recepción del correo y posterior autenticación del cliente mediante la nueva contraseña temporal.

---

# Evolución del proyecto

MapuEscuela fue desarrollado progresivamente durante las distintas unidades de la asignatura.

## Unidad 1

Durante la primera unidad se trabajó principalmente en el modelamiento del proceso.

Se desarrollaron:

- Modelo AS-IS.
- Modelo TO-BE.
- Formularios.
- Flujo del pedido.
- Carga y validación del comprobante.
- Retiro.
- Despacho.
- Temporizador de 24 horas.

La documentación correspondiente se encuentra en:

```text
documentacion/unidad1
```

---

## Unidad 2

Durante la segunda unidad se incorporó el desarrollo de Web Services utilizando Java.

Se trabajó principalmente en operaciones relacionadas con:

- Clientes.
- Pedidos.
- Pagos.
- Estados de los pedidos.

También se realizaron modificaciones al BPMN para relacionar el proceso con los servicios desarrollados.

La documentación correspondiente se encuentra en:

```text
documentacion/unidad2
```

---

## Unidad 3

Durante la tercera unidad se realizó la integración de los principales componentes.

Se incorporaron:

- Spring Boot.
- MySQL.
- Servicios REST.
- Interfaz web.
- Flowable.
- User Tasks.
- Tareas HTTP.
- External Worker.
- Retiro.
- Despacho.
- Inventario.
- Cancelación.
- Validación de comprobantes.
- Rechazo de comprobantes.

La documentación correspondiente se encuentra en:

```text
documentacion/unidad3
```

---

# Versiones del BPMN

Durante el desarrollo se generaron diferentes versiones del proceso para incorporar mejoras y realizar pruebas.

Entre las últimas versiones se encuentran:

```text
Proceso_de_venta_-_Mapuescuela_U3_v6.bpmn20.xml
Proceso_de_venta_-_Mapuescuela_U3_v7.bpmn20.xml
Proceso_de_venta_-_Mapuescuela_U3_v8.bpmn20.xml
Proceso_de_venta_-_Mapuescuela_U3_v9.bpmn20.xml
```

La clave utilizada por el proceso se mantuvo como:

```text
procesoVentaMapuescuelaV3
```

V6 incorporó el External Worker `confirmarPago`.

V7 incorporó ajustes al flujo de validación del comprobante y al manejo del motivo de rechazo.

V8 correspondió a una versión intermedia de integración manteniendo las ramas de retiro y despacho.

V9 mantiene la lógica de V8 y mejora la claridad de algunas actividades del proceso.

La tarea:

```text
Registrar datos y generar pedido
```

pasó a denominarse:

```text
Registrar datos de compra y generar pedido
```

y el evento inicial:

```text
Solicitud de compra iniciada
```

pasó a:

```text
Cliente registrado inicia solicitud de compra
```

V9 corresponde al proceso final utilizado durante las pruebas de integración del MVP.

---

# Uso de inteligencia artificial

Durante el desarrollo se utilizó inteligencia artificial como herramienta de apoyo.

Su utilización estuvo principalmente relacionada con:

- Revisión de errores.
- Interpretación de mensajes de ejecución.
- Apoyo en la revisión de código.
- Orientación durante la integración entre Java, MySQL y Flowable.
- Revisión de configuraciones.
- Apoyo en documentación técnica.
- Organización de pruebas.

Los cambios implementados fueron posteriormente ejecutados y comprobados en el entorno de desarrollo antes de incorporarlos a la versión final.

---

# Documentación

La documentación del proyecto se encuentra en:

```text
documentacion
```

La estructura principal incluye:

```text
documentacion/unidad1
documentacion/unidad2
documentacion/unidad3
```

Estas carpetas contienen documentos, modelos BPMN, actas y otras evidencias generadas durante el desarrollo.

---

# Control de versiones

El proyecto utiliza Git y GitHub para mantener el control de versiones.

El repositorio de la entrega final es:

```text
https://github.com/abrahamcanalessepulveda/mapuescuela-examen-final
```

El historial de commits permite revisar la evolución del proyecto y los principales cambios realizados durante el desarrollo.

El repositorio incluye:

- Código fuente.
- Interfaz web.
- Servicios.
- Proceso BPMN.
- Script de instalación de MySQL.
- Documentación.
- Maven Wrapper.
- Archivos necesarios para reproducir el MVP.

---

# Videos

Durante las unidades anteriores se generaron videos como evidencia del desarrollo y funcionamiento del proyecto.

Los videos definitivos correspondientes al **Examen Final** serán incorporados en esta sección una vez finalizada la grabación de la entrega.

---

# Resumen

MapuEscuela comenzó con el modelamiento BPMN del proceso de venta y fue evolucionando progresivamente hasta convertirse en un MVP integrado.

La solución final combina:

```text
Portal de Clientes
        +
Panel Administrativo
        +
Spring Boot
        +
MySQL
        +
Flowable
        +
BPMN
```

El Portal de Clientes permite registrar usuarios, iniciar sesión, recuperar el acceso mediante correo electrónico, consultar productos, generar pedidos y consultar el historial correspondiente a la cuenta autenticada.

El Panel Administrativo permite gestionar pedidos, inventario y clientes, además de continuar las distintas actividades administrativas del proceso.

Flowable controla el avance del proceso BPMN mientras Spring Boot implementa la lógica de la aplicación y MySQL mantiene la información del sistema.

Como resultado se obtiene un MVP funcional que permite ejecutar el proceso principal de venta de MapuEscuela desde la solicitud inicial del cliente hasta la entrega mediante retiro o despacho.