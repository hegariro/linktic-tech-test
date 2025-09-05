# Arquitectura de Microservicios — linktic-tech-test

## 1. Introducción
El proyecto `linktic-tech-test` implementa una arquitectura basada en microservicios, organizada en módulos independientes que interactúan entre sí para cubrir diferentes responsabilidades de negocio.

## 2. Descripción general
El sistema se compone principalmente de tres dominios:
- **Auth**: Gestión de autenticación de usuarios mediante credenciales y tokens JWT.
- **Inventory**: Manejo de inventarios, productos disponibles y transacciones de compra/venta.
- **Product**: Administración y consulta de información de productos.

Cada dominio está diseñado como un servicio desacoplado, con su propia capa de aplicación, dominio e infraestructura.

## 3. Comunicación entre servicios
- La aplicación `Inventory` actúa como orquestador, interactuando con `Auth` y `Product` según las operaciones requeridas.
- La comunicación entre servicios se realiza mediante **REST APIs** expuestas por cada microservicio.

## 4. Base de datos
Cada microservicio mantiene su propio modelo de persistencia (basado en JPA y entidades). Esto permite independencia en la evolución de cada servicio.

## 5. Estilo arquitectónico
- **Arquitectura Hexagonal (Puertos y Adaptadores):** Cada microservicio implementa casos de uso y define puertos para interactuar con repositorios y servicios externos.
- **Vertical Slicing:** La aplicación está organizada por funcionalidades/domínios y no en capas técnicas, lo cual favorece el mantenimiento y escalabilidad.
- **Screaming Architecture:** Los nombres de los paquetes gritan el dominio (auth, management_inventory, management_product).

## 6. Despliegue
El proyecto está preparado para ejecutarse con **Docker Compose**, lo que permite levantar todos los servicios (incluyendo la base de datos) de manera orquestada.

## 7. Conclusiones
La arquitectura planteada asegura:
- Desacoplamiento entre servicios.
- Escalabilidad y facilidad de mantenimiento.
- Claridad en la organización del código gracias a los principios de arquitectura hexagonal y vertical slicing.
