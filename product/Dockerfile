# =========================================
# Multi-stage build
# =========================================

# Etapa 1: Build de la aplicación
FROM maven:3.9.4-amazoncorretto-17 AS builder

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven primero (para cache de dependencias)
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

# Hacer ejecutable el wrapper de Maven
RUN chmod +x mvnw

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src/

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests -B

# =========================================
# Etapa 2: Solo para tests
FROM builder AS test-stage

# Esta etapa está disponible para ejecutar tests
# Los tests se ejecutarán cuando se especifique al run del contenedor
# Ejemplo: docker run --rm test-stage mvn test

# =========================================
# Etapa 3: Imagen runtime optimizada
# =========================================
FROM amazoncorretto:17-alpine-jdk AS runtime

# Instalar herramientas necesarias
RUN apk add --no-cache \
    curl \
    tzdata \
    && rm -rf /var/cache/apk/*

# Configurar zona horaria
ENV TZ=America/Bogota

# Crear usuario no-root para seguridad
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser

# Crear directorios de trabajo
RUN mkdir -p /app/logs /app/tmp && \
    chown -R appuser:appgroup /app

# Cambiar al usuario no-root
USER appuser

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    SPRING_PROFILES_ACTIVE=docker \
    SERVER_PORT=8080 \
    LOGGING_LEVEL_ROOT=INFO \
    MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Configurar JVM para contenedores
ENV JAVA_OPTS="$JAVA_OPTS \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+OptimizeStringConcat \
    -XX:+UseStringDeduplication \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom \
    -Djava.awt.headless=true"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]

# Metadata adicional
LABEL org.opencontainers.image.title="Products Microservice"
LABEL org.opencontainers.image.description="Microservicio de gestión de productos"
LABEL org.opencontainers.image.vendor="LinkTic"
LABEL org.opencontainers.image.version="1.0.0"

