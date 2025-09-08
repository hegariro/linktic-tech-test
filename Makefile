# Makefile para Microservicios Spring Boot
# ===============================================

.PHONY: help build test clean logs status health shell db-shell test-product test-inventory run-product run-inventory run-all

# Variables
COMPOSE_FILE = docker-compose.yaml
PRODUCT_SERVICE = products-service
INVENTORY_SERVICE = inventory-service
MYSQL_SERVICE = mysql-db
NETWORK_NAME = microservices-network

# Colores para output
GREEN = \033[0;32m
YELLOW = \033[0;33m
RED = \033[0;31m
NC = \033[0m # No Color

# ===============================================
# COMANDOS DE AYUDA
# ===============================================
help: ## Muestra esta ayuda
	@echo "$(GREEN)Comandos disponibles para microservicios:$(NC)"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "$(YELLOW)%-25s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(GREEN)Ejemplos de uso:$(NC)"
	@echo "  make test-product      # Tests del microservicio product"
	@echo "  make test-inventory    # Tests del microservicio inventory"
	@echo "  make run-all          # Levanta todos los servicios"

# ===============================================
# VARIABLES DE AMBIENTE
# ===============================================

ENV_ARGS := $(shell grep -v '^#' .env | xargs -I {} echo '--build-arg {}')

# ===============================================
# COMANDOS DE TESTS INDIVIDUALES
# ===============================================

test-product: ## Ejecuta tests unitarios del microservicio product
	@echo "$(GREEN)🧪 Ejecutando tests unitarios del microservicio PRODUCT...$(NC)"
	@echo "$(YELLOW)⚠️  Solo se ejecutarán tests que no requieran servicios externos$(NC)"
	@cd product && docker build --target builder -t product-tests -f Dockerfile .
	@cd product && docker run --env-file .env --rm \
		--name product-unit-tests product-tests mvn test \
		-Dspring.profiles.active=test -Dtest="**/*Test.java" --batch-mode

test-inventory: ## Ejecuta tests unitarios del microservicio inventory  
	@echo "$(GREEN)🧪 Ejecutando tests unitarios del microservicio INVENTORY...$(NC)"
	@echo "$(YELLOW)⚠️  Solo se ejecutarán tests que no requieran product-service$(NC)"
	@cd inventory && docker build --target builder -t inventory-tests -f Dockerfile .
	@cd inventory && docker run --env-file .env --rm \
		--name inventory-unit-tests inventory-tests mvn test \
		-Dspring.profiles.active=test -Dtest="**/*Test.java" --batch-mode

test-inventory-onlyone: ## Ejecuta un único test unitario del microservicio inventory  
	@echo "$(GREEN)🧪 Ejecutando tests unitarios del microservicio INVENTORY...$(NC)"
	@echo "$(YELLOW)⚠️  Solo se ejecutarán tests que no requieran product-service$(NC)"
	@cd inventory && docker build --target builder -t inventory-tests -f Dockerfile .
	@cd inventory && docker run --env-file .env --rm \
		--name inventory-unit-tests inventory-tests mvn test \
		-Dspring.profiles.active=test \
		-Dtest='$(TEST_TITLE)' \
		--batch-mode
		#-Dtest="com.example.inventory.api.v1.dto.*Test.java" \
		#-Dtest="com.example.inventory.InventoryApplicationTest.java" \

test-product-with-db: ## Ejecuta tests del microservicio product con base de datos
	@echo "$(GREEN)🧪 Ejecutando tests del microservicio PRODUCT con base de datos...$(NC)"
	@$(MAKE) ensure-db-running
	@cd product && docker build --target builder -t product-tests-db -f Dockerfile .
	@cd product && docker run --env-file .env --rm --name product-integration-tests \
		--network $(NETWORK_NAME) \
		product-tests-db mvn test --batch-mode

test-inventory-with-deps: ## Ejecuta tests del microservicio inventory con todas las dependencias
	@echo "$(GREEN)🧪 Ejecutando tests del microservicio INVENTORY con dependencias...$(NC)"
	@$(MAKE) ensure-deps-running
	@cd inventory && docker build --target builder -t inventory-tests-deps -f Dockerfile .
	@cd inventory && docker run --env-file .env --rm --name inventory-integration-tests \
		--network $(NETWORK_NAME) \
		inventory-tests-deps mvn test --batch-mode

test-all: ## Ejecuta todos los tests de todos los microservicios
	@echo "$(GREEN)🧪 Ejecutando todos los tests...$(NC)"
	@$(MAKE) test-product
	@$(MAKE) test-inventory

# ===============================================
# COMANDOS DE EJECUCIÓN DE SERVICIOS
# ===============================================

run-db: ## Levanta solo la base de datos MySQL
	@echo "$(GREEN)🚀 Iniciando base de datos MySQL...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) up -d $(MYSQL_SERVICE)
	@$(MAKE) wait-for-db

run-product: ## Levanta product-service con su base de datos
	@echo "$(GREEN)🚀 Iniciando microservicio PRODUCT...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) up -d $(MYSQL_SERVICE) $(PRODUCT_SERVICE)
	@$(MAKE) wait-for-service SERVICE=$(PRODUCT_SERVICE) PORT=8080

run-inventory: ## Levanta inventory-service con todas sus dependencias
	@echo "$(GREEN)🚀 Iniciando microservicio INVENTORY...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) up -d $(MYSQL_SERVICE) $(PRODUCT_SERVICE) $(INVENTORY_SERVICE)
	@$(MAKE) wait-for-service SERVICE=$(INVENTORY_SERVICE) PORT=8081

run-all: ## Levanta todos los servicios
	@echo "$(GREEN)🚀 Iniciando todos los microservicios...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) up -d
	@$(MAKE) wait-for-all-services

run-dev: ## Levanta todos los servicios con logs visibles
	@echo "$(GREEN)🚀 Iniciando en modo desarrollo...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) up

# ===============================================
# COMANDOS DE CONSTRUCCIÓN
# ===============================================

build-product: ## Construye imagen del microservicio product
	@echo "$(GREEN)🔨 Construyendo microservicio PRODUCT...$(NC)"
	@cd product && docker build -t product-service:latest .

build-inventory: ## Construye imagen del microservicio inventory
	@echo "$(GREEN)🔨 Construyendo microservicio INVENTORY...$(NC)"
	@cd inventory && docker build -t inventory-service:latest .

build-all: ## Construye todas las imágenes
	@echo "$(GREEN)🔨 Construyendo todos los microservicios...$(NC)"
	@$(MAKE) build-product
	@$(MAKE) build-inventory

rebuild: clean build-all ## Reconstruye todas las imágenes desde cero

# ===============================================
# COMANDOS DE MONITOREO
# ===============================================

status: ## Muestra el estado de todos los servicios
	@echo "$(GREEN)📊 Estado de los servicios:$(NC)"
	@docker-compose -f $(COMPOSE_FILE) ps

logs-product: ## Muestra logs del microservicio product
	@echo "$(GREEN)📋 Logs del microservicio PRODUCT:$(NC)"
	@docker-compose -f $(COMPOSE_FILE) logs -f $(PRODUCT_SERVICE)

logs-inventory: ## Muestra logs del microservicio inventory
	@echo "$(GREEN)📋 Logs del microservicio INVENTORY:$(NC)"
	@docker-compose -f $(COMPOSE_FILE) logs -f $(INVENTORY_SERVICE)

logs-db: ## Muestra logs de la base de datos
	@echo "$(GREEN)📋 Logs de MySQL:$(NC)"
	@docker-compose -f $(COMPOSE_FILE) logs -f $(MYSQL_SERVICE)

logs-all: ## Muestra logs de todos los servicios
	@echo "$(GREEN)📋 Logs de todos los servicios:$(NC)"
	@docker-compose -f $(COMPOSE_FILE) logs -f

health: ## Verifica la salud de todos los servicios
	@echo "$(GREEN)🏥 Verificando salud de los servicios...$(NC)"
	@echo "$(YELLOW)MySQL:$(NC)"
	@docker-compose -f $(COMPOSE_FILE) exec $(MYSQL_SERVICE) mysqladmin ping -h localhost -u root -p$${MYSQL_ROOT_PASSWORD} || echo "❌ MySQL no disponible"
	@echo "$(YELLOW)Product Service:$(NC)"
	@curl -f http://localhost:8080/actuator/health 2>/dev/null && echo "✅ Product Service OK" || echo "❌ Product Service no disponible"
	@echo "$(YELLOW)Inventory Service:$(NC)"
	@curl -f http://localhost:8081/actuator/health 2>/dev/null && echo "✅ Inventory Service OK" || echo "❌ Inventory Service no disponible"

# ===============================================
# COMANDOS DE DESARROLLO
# ===============================================

shell-product: ## Abre shell en el contenedor del microservicio product
	@docker-compose -f $(COMPOSE_FILE) exec $(PRODUCT_SERVICE) /bin/sh

shell-inventory: ## Abre shell en el contenedor del microservicio inventory
	@docker-compose -f $(COMPOSE_FILE) exec $(INVENTORY_SERVICE) /bin/sh

shell-db: ## Abre shell en MySQL
	@docker-compose -f $(COMPOSE_FILE) exec $(MYSQL_SERVICE) mysql -u root -p$${MYSQL_ROOT_PASSWORD} $${MYSQL_DATABASE:-linktic_inventory_db}

# ===============================================
# COMANDOS DE LIMPIEZA
# ===============================================

stop: ## Para todos los servicios
	@echo "$(GREEN)⏹️  Deteniendo servicios...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) down

stop-product: ## Para solo el microservicio product
	@echo "$(GREEN)⏹️  Deteniendo microservicio PRODUCT...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) stop $(PRODUCT_SERVICE)

stop-inventory: ## Para solo el microservicio inventory
	@echo "$(GREEN)⏹️  Deteniendo microservicio INVENTORY...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) stop $(INVENTORY_SERVICE)

clean: ## Limpia contenedores, redes y volúmenes
	@echo "$(GREEN)🧹 Limpiando recursos...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) down -v --remove-orphans
	@docker system prune -f

clean-all: ## Limpieza completa incluyendo imágenes
	@echo "$(GREEN)🧹 Limpieza completa...$(NC)"
	@docker-compose -f $(COMPOSE_FILE) down -v --rmi all --remove-orphans
	@docker system prune -af

restart: stop run-all ## Reinicia todos los servicios

restart-product: ## Reinicia solo el microservicio product
	@$(MAKE) stop-product
	@$(MAKE) run-product

restart-inventory: ## Reinicia solo el microservicio inventory
	@$(MAKE) stop-inventory  
	@$(MAKE) run-inventory

# ===============================================
# COMANDOS DE UTILIDAD (FUNCIONES INTERNAS)
# ===============================================

ensure-network: ## Asegura que la red existe
	@docker network ls | grep -q $(NETWORK_NAME) || docker network create $(NETWORK_NAME)

ensure-db-running: ## Asegura que MySQL esté ejecutándose
	@$(MAKE) ensure-network
	@echo "$(YELLOW)Verificando que MySQL esté ejecutándose...$(NC)"
	@if [ "$$(docker-compose -f $(COMPOSE_FILE) ps -q $(MYSQL_SERVICE))" = "" ]; then \
		echo "$(YELLOW)Iniciando MySQL...$(NC)"; \
		docker-compose -f $(COMPOSE_FILE) up -d $(MYSQL_SERVICE); \
		$(MAKE) wait-for-db; \
	fi

ensure-deps-running: ## Asegura que todas las dependencias estén ejecutándose
	@$(MAKE) ensure-network
	@echo "$(YELLOW)Verificando dependencias...$(NC)"
	@if [ "$$(docker-compose -f $(COMPOSE_FILE) ps -q $(MYSQL_SERVICE))" = "" ]; then \
		echo "$(YELLOW)Iniciando MySQL...$(NC)"; \
		docker-compose -f $(COMPOSE_FILE) up -d $(MYSQL_SERVICE); \
		$(MAKE) wait-for-db; \
	fi
	@if [ "$$(docker-compose -f $(COMPOSE_FILE) ps -q $(PRODUCT_SERVICE))" = "" ]; then \
		echo "$(YELLOW)Iniciando Product Service...$(NC)"; \
		docker-compose -f $(COMPOSE_FILE) up -d $(PRODUCT_SERVICE); \
		$(MAKE) wait-for-service SERVICE=$(PRODUCT_SERVICE) PORT=8080; \
	fi

wait-for-db: ## Espera a que MySQL esté listo
	@echo "$(YELLOW)Esperando que MySQL esté listo...$(NC)"
	@for i in {1..5}; do \
		if docker-compose -f $(COMPOSE_FILE) --env-file .env exec -T $(MYSQL_SERVICE) sh -c "MYSQL_PWD=\$$MYSQL_ROOT_PASSWORD mysqladmin ping -h localhost -u root --silent" >/dev/null 2>&1; then \
			echo "$(GREEN)✅ MySQL está listo$(NC)"; \
			exit 0; \
		fi; \
		echo "Intento $$i fallido, esperando 2 segundos..."; \
		sleep 6; \
	done; \
	echo "$(RED)❌ Timeout esperando MySQL$(NC)"; \
	exit 1

wait-for-db-debug: ## Debug de conexión MySQL
	@echo "$(YELLOW)Debug: Verificando MySQL...$(NC)"
	docker-compose -f $(COMPOSE_FILE) --env-file .env exec -T $(MYSQL_SERVICE) sh -c "echo 'Variable MYSQL_ROOT_PASSWORD:' \$$MYSQL_ROOT_PASSWORD"
	docker-compose -f $(COMPOSE_FILE) --env-file .env exec -T $(MYSQL_SERVICE) sh -c "MYSQL_PWD=\$$MYSQL_ROOT_PASSWORD mysqladmin ping -h localhost -u root -v"

wait-for-service: ## Espera a que un servicio esté listo
	@echo "$(YELLOW)Esperando que $(SERVICE) esté listo en puerto $(PORT)...$(NC)"
	@timeout 30 sh -c 'until curl -f http://localhost:$(PORT)/api/actuator/health >/dev/null 2>&1; do sleep 3; done'
	@echo "$(GREEN)✅ $(SERVICE) está listo$(NC)"

wait-for-all-services: ## Espera a que todos los servicios estén listos
	@$(MAKE) wait-for-db
	@$(MAKE) wait-for-service SERVICE=$(PRODUCT_SERVICE) PORT=8080
	@$(MAKE) wait-for-service SERVICE=$(INVENTORY_SERVICE) PORT=8081
	@echo "$(GREEN)✅ Todos los servicios están listos$(NC)"

# ===============================================
# COMANDOS DE PIPELINE DE DESARROLLO
# ===============================================

dev-product: ## Pipeline completo de desarrollo para product
	@echo "$(GREEN)🔄 Pipeline de desarrollo para PRODUCT...$(NC)"
	@$(MAKE) test-product
	@$(MAKE) build-product
	@$(MAKE) run-product
	@$(MAKE) health

dev-inventory: ## Pipeline completo de desarrollo para inventory
	@echo "$(GREEN)🔄 Pipeline de desarrollo para INVENTORY...$(NC)"
	@$(MAKE) test-inventory
	@$(MAKE) build-inventory
	@$(MAKE) run-inventory
	@$(MAKE) health

dev-all: ## Pipeline completo de desarrollo para todos los servicios
	@echo "$(GREEN)🔄 Pipeline de desarrollo completo...$(NC)"
	@$(MAKE) test-all
	@$(MAKE) build-all
	@$(MAKE) run-all
	@$(MAKE) health

# ===============================================
# COMANDOS RÁPIDOS
# ===============================================

quick-test-product: ## Test rápido del microservicio product (sin reconstruir)
	@echo "$(GREEN)⚡ Test rápido PRODUCT...$(NC)"
	@cd product && mvn test -Dspring.profiles.active=test -Dtest="**/*Test.java" --batch-mode

quick-test-inventory: ## Test rápido del microservicio inventory (sin reconstruir)
	@echo "$(GREEN)⚡ Test rápido INVENTORY...$(NC)"
	@cd inventory && mvn test -Dspring.profiles.active=test -Dtest="**/*Test.java" --batch-mode

