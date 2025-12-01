Frontend – Sistema de Gestión de Reservas en Restaurantes

Este proyecto corresponde al frontend del sistema de gestión de reservas para restaurantes, desarrollado como parte de una prueba técnica para un cargo de Desarrollo de Software Junior.
La aplicación consume un backend en Spring Boot mediante una API REST y permite gestionar mesas, clientes y reservas con todas las reglas de negocio definidas.

Tecnologías utilizadas

React 18

Vite

Fetch API para comunicación con el backend

CSS modularizado

React Router (si aplica)

Estructura del proyecto
src/
├── components/
│   ├── layout/          → Header, Sidebar
│   ├── dashboard/       → Panel principal
│   ├── tables/          → Gestión de mesas
│   ├── reservations/    → Reservas y lista de espera
│   ├── clients/         → Gestión de clientes
│   ├── schedule/        → Configuración de horarios
│   └── ui/              → Componentes reutilizables
├── App.jsx              → Componente raíz
├── main.jsx             → Punto de entrada
└── index.css            → Estilos globales


Esta estructura permite una separación clara de responsabilidades y facilita la escalabilidad del proyecto.

Funcionalidades implementadas

Panel principal con métricas generales (reservas activas, disponibilidad de mesas, clientes VIP).

Gestión completa de mesas (crear, editar, eliminar y listar).

Creación, confirmación, finalización y cancelación de reservas.

Validaciones de reglas de negocio como capacidad mínima, disponibilidad y rango de horarios.

Sistema de fidelización con niveles Bronce, Plata y Oro.

Lista de espera VIP activa cuando no hay mesas exclusivas disponibles.

Interfaz ordenada y enfocada en la administración del restaurante.

Instalación y ejecución

Ubicarse en la carpeta del proyecto.

Instalar dependencias:

npm install


Ejecutar el servidor de desarrollo:

npm run dev


Acceder desde el navegador:

http://localhost:5173


Asegurarse de que el backend esté disponible en:

http://localhost:8080

Cumplimiento de requisitos

Este frontend, en conjunto con el backend, cubre los módulos solicitados en la prueba técnica:

Gestión de mesas.

Gestión de reservas.

Validaciones de disponibilidad, duración mínima, horarios y capacidad.

Panel administrativo con información en tiempo real.

Lista de espera y sistema de fidelización.

Flujo completo de administración del restaurante.