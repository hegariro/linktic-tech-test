/**
 * Módulo de gestión de inventario implementando arquitectura hexagonal.
 * 
 * <p>Este módulo implementa todas las funcionalidades relacionadas con la gestión
 * de inventario de productos, incluyendo consultas de stock, transacciones de 
 * entrada y salida, y validación de disponibilidad.</p>
 * 
 * <h2>Funcionalidades principales:</h2>
 * <ul>
 *   <li><strong>Consulta de inventario</strong> - Búsqueda de productos por ID</li>
 *   <li><strong>Transacciones de entrada</strong> - Registro de compras y reposición de stock</li>
 *   <li><strong>Transacciones de salida</strong> - Registro de ventas con validación de stock</li>
 *   <li><strong>Validación de productos</strong> - Verificación de existencia en catálogo</li>
 * </ul>
 * 
 * <h2>Arquitectura del módulo:</h2>
 * <p>Implementa el patrón de arquitectura hexagonal (Ports and Adapters) con las siguientes capas:</p>
 * 
 * <h3>📦 Domain Layer:</h3>
 * <ul>
 *   <li><strong>Ubicación:</strong> {@code com.example.inventory.management_inventory.domain}</li>
 *   <li><strong>Propósito:</strong> Contiene la lógica de negocio pura y las entidades del dominio</li>
 *   <li><strong>Dependencias:</strong> Ninguna (núcleo independiente)</li>
 * </ul>
 * 
 * <h3>🔧 Application Layer:</h3>
 * <ul>
 *   <li><strong>Ubicación:</strong> {@code com.example.inventory.management_inventory.application}</li>
 *   <li><strong>Propósito:</strong> Orquestación de casos de uso y definición de puertos</li>
 *   <li><strong>Dependencias:</strong> Solo la capa de dominio</li>
 * </ul>
 * 
 * <h3>🌐 Infrastructure Layer:</h3>
 * <ul>
 *   <li><strong>Ubicación:</strong> {@code com.example.inventory.management_inventory.infrastructure}</li>
 *   <li><strong>Propósito:</strong> Adaptadores para frameworks externos y tecnologías específicas</li>
 *   <li><strong>Dependencias:</strong> Capas de aplicación y dominio, frameworks externos</li>
 * </ul>
 * 
 * <h2>Flujo de dependencias:</h2>
 * <pre>
 * Infrastructure ──→ Application ──→ Domain
 *      ↑                              ↑
 *   Adapters                     Core Business
 *   (Spring,                        Logic
 *    JPA, etc.)                  (Pure Java)
 * </pre>
 * 
 * <h2>Principios aplicados:</h2>
 * <ul>
 *   <li><strong>Dependency Inversion</strong> - Las dependencias apuntan hacia adentro</li>
 *   <li><strong>Single Responsibility</strong> - Cada capa tiene una responsabilidad específica</li>
 *   <li><strong>Open/Closed</strong> - Extensible mediante nuevos adaptadores</li>
 *   <li><strong>Interface Segregation</strong> - Interfaces específicas por funcionalidad</li>
 * </ul>
 * 
 * <h2>Tecnologías utilizadas:</h2>
 * <ul>
 *   <li><strong>Spring Boot</strong> - Framework de aplicación</li>
 *   <li><strong>Spring Data JPA</strong> - Persistencia de datos</li>
 *   <li><strong>Jakarta Persistence</strong> - Especificación JPA</li>
 *   <li><strong>Lombok</strong> - Reducción de código boilerplate</li>
 * </ul>
 * 
 * @since 1.0
 */
package com.example.inventory.management_inventory;
