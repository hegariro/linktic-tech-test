# Linktic Tech Test

Este proyecto es una aplicación Java construida con Spring Boot, diseñada para demostrar capacidades técnicas en un entorno orientado a microservicios. La aplicación utiliza Docker Compose para orquestar los servicios principales de Spring Boot junto con una base de datos, permitiendo un desarrollo y pruebas locales sencillas.

---

## 🚀 Características

- **Aplicaciones Spring Boot:** Microservicios backend implementados en Java.
- **Integración con Base de Datos:** Uso de contenedores de base de datos relacional para persistencia.
- **Entorno Dockerizado:** Todos los servicios se levantan con Docker Compose para mayor consistencia y simplicidad.
- **Documentación API:** Acceso a Swagger UI para explorar y probar la API de forma interactiva.

---

## 🛠️ Prerrequisitos

Antes de ejecutar este proyecto, asegúrate de tener instalado lo siguiente:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose Plugin](https://docs.docker.com/compose/install/)

---

## 📥 Clonando el Repositorio

Clona este repositorio en tu máquina local:

```bash
git clone https://github.com/hegariro/linktic-tech-test.git
cd linktic-tech-test
```

---

## 🐳 Ejecutando la Aplicación

Primero, asegúrate de tener un archivo `.env` en el directorio raíz con las variables de entorno requeridas (consulta `.env.example`).

Inicia todos los servicios (aplicaciones Spring Boot y base de datos) usando Docker Compose:

```bash
docker compose --env-file .env up -d
```

Este comando descargará las imágenes necesarias y levantará los contenedores en modo desacoplado.

---

## 🛑 Deteniendo la Aplicación

Para detener y eliminar los contenedores, ejecuta:

```bash
docker compose down
```

---

## 📁 Estructura del Proyecto

```
.
├── docker-compose.yaml  # Orquestación de servicios con Docker Compose
├── inventory/                  # Microservicio Inventory (Spring Boot)
│   ├── Dockerfile
│   ├── HELP.md
│   ├── logs/
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src/
│   └── target/
├── product/                    # Microservicio Product (Spring Boot)
│   ├── Dockerfile
│   ├── HELP.md
│   ├── logs/
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src/
│   └── target/
├── README.md                   # Documentación principal
└── storage/                    # Almacenamiento de datos persistente
    ├── inventory/
    ├── mysql/
    └── products/
```

- **inventory/**: Microservicio de inventario, implementado en Spring Boot.
- **product/**: Microservicio de productos, implementado en Spring Boot.
- **storage/**: Directorios de volúmenes para la persistencia de datos de los servicios y la base de datos.
- **docker-compose.yaml**: Archivo principal para levantar todos los servicios y la base de datos.
- **README.md**: Documentación del proyecto.

---

## 📡 Endpoints de la API & Pruebas

Una vez que los contenedores estén arriba, los microservicios Spring Boot estarán disponibles en los siguientes endpoints (puertos por defecto):

- Inventory Service: [http://localhost:8081/api](http://localhost:8081/api/)
- Product Service: [http://localhost:8080/api](http://localhost:8080/api)

Puedes probar las APIs con herramientas como [curl](https://curl.se/), [Postman](https://www.postman.com/) o directamente desde [Swagger UI Product-Service](http://localhost:8080/api/swagger-ui.html) o [Swagger UI Inventory-Service](http://localhost:8081/api/swagger-ui.html).

### Ejemplo de Endpoints

**Product**

- `GET /api/v1/products/` — Lista todos los productos

---

## 📖 Documentación Swagger

La documentación interactiva de la API para cada microservicio está disponible en:

- **Inventory Swagger UI:** [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **Product Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

También puedes consultar los OpenAPI Specs correspondientes:

- **Inventory OpenAPI Spec:** [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)
- **Product OpenAPI Spec:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📝 Notas

- Actualiza el archivo `.env` según sea necesario para tu entorno local.
- Asegúrate de que los puertos definidos en `docker-compose.yaml` no estén en uso.
- Los volúmenes en `storage/` garantizan persistencia de datos en los contenedores.

---

¡Siéntete libre de contribuir o abrir issues para sugerir mejoras!

