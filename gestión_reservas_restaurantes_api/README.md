Sistema de Reservas para Restaurantes

Prueba Técnica – Backend

Este proyecto es un backend desarrollado en Spring Boot como parte de una prueba técnica. Permite administrar mesas, clientes y reservas de un restaurante, controlando la disponibilidad en tiempo real. También incluye una lógica sencilla de fidelización y manejo de lista VIP.

Funcionalidades principales

Gestión de mesas: crear, editar, eliminar y listar.

Gestión de clientes.

Gestión completa de reservas con todas las reglas de negocio solicitadas.

Confirmación y finalización de reservas.

Consulta de mesas disponibles según rango horario, capacidad y si requieren VIP.

Manejo de lista de espera VIP.

Sistema básico de puntos y niveles (Bronce, Plata, Oro).

Estructura del proyecto
src/
├── config/        → Configuración (CORS)
├── controller/    → Endpoints de la API
├── dto/           → Objetos usados para transferir datos
├── mapper/        → Conversión entre entidades y DTOs
├── model/         → Entidades JPA
├── repository/    → Acceso a datos
├── scheduling/    → Tareas automáticas programadas
├── service/       → Interfaces de servicios
└── service/impl/  → Implementación de lógica de negocio

Reglas de negocio implementadas

No se permiten reservas en mesas ocupadas.

Validación de capacidad antes de reservar.

La duración mínima de una reserva es de 1 hora.

Las reservas solo pueden ser con fecha y hora futura.

Las reservas solo pueden hacerse en el horario de 10:00 a 22:00.

Una reserva debe confirmarse 30 minutos antes; si no, se cancela automáticamente.

Mesas VIP solo accesibles para clientes VIP.

Lista de espera VIP con límite del 20%.

Sistema de puntos según nivel del cliente.

Base de datos

El proyecto usa PostgreSQL. Basta con crear una base de datos vacía.

Ejemplo:

CREATE DATABASE reservations;

Cómo ejecutar el backend
Requisitos

Java 17 o superior

PostgreSQL

Maven

Pasos

Clonar este repositorio.

Crear la base de datos.

Revisar el archivo:

src/main/resources/application.properties


y configurar las credenciales de PostgreSQL.

Ejecutar:

mvn spring-boot:run


La API estará disponible en:

http://localhost:8080

Endpoints principales
Recurso	Ruta	Acción
Clientes	POST /api/clients	Crear cliente
Mesas	POST /api/tables	Crear mesa
Reservas	POST /api/reservations	Crear reserva
Confirmar reserva	PUT /api/reservations/{id}/confirm	Confirmar
Finalizar reserva	PUT /api/reservations/{id}/finish	Finalizar
Mesas disponibles	GET /api/reservations/available-tables	Consultar
Lista VIP	GET /api/vip-waitlist	Ver lista
Comentario final

Este backend cumple con los requisitos de la prueba técnica. Se desarrolló buscando mantener una estructura clara, fácil de entender, mantener y extender.