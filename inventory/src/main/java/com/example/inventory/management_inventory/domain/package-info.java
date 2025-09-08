/**
 * Capa de dominio del módulo de gestión de inventario.
 * 
 * <p>Esta capa contiene la lógica de negocio pura y los conceptos fundamentales 
 * del dominio de inventario. Es el núcleo de la aplicación y no depende de ninguna 
 * tecnología externa, framework o capa de infraestructura.</p>
 * 
 * <h2>Responsabilidades:</h2>
 * <ul>
 *   <li>Definir las entidades y value objects del dominio</li>
 *   <li>Implementar las reglas de negocio e invariantes</li>
 *   <li>Definir los contratos (interfaces) para acceso a datos</li>
 *   <li>Encapsular la lógica del dominio de inventario</li>
 * </ul>
 * 
 * <h2>Entidades del dominio:</h2>
 * <ul>
 *   <li>{@link com.example.inventory.management_inventory.domain.models.Inventory} - Representa el stock de productos</li>
 *   <li>{@link com.example.inventory.management_inventory.domain.models.Product} - Información del producto</li>
 *   <li>{@link com.example.inventory.management_inventory.domain.models.TransactionData} - Datos de transacción</li>
 *   <li>{@link com.example.inventory.management_inventory.domain.models.TransactionType} - Tipos de transacción</li>
 * </ul>
 * 
 * <h2>Reglas de dependencia:</h2>
 * <ul>
 *   <li>✅ Completamente independiente de frameworks externos</li>
 *   <li>✅ Define interfaces para servicios externos (repositories)</li>
 *   <li>❌ NO debe depender de capas de aplicación o infraestructura</li>
 *   <li>❌ NO debe contener anotaciones de framework (@Service, @Entity, etc.)</li>
 *   <li>❌ NO debe importar librerías específicas de persistencia o web</li>
 * </ul>
 * 
 * <h2>Patrones implementados:</h2>
 * <ul>
 *   <li><strong>Repository Pattern</strong> - Interfaces para acceso a datos</li>
 *   <li><strong>Value Objects</strong> - Records inmutables para datos</li>
 *   <li><strong>Domain Services</strong> - Lógica que no pertenece a una entidad específica</li>
 * </ul>
 * 
 * @since 1.0
 */
package com.example.inventory.management_inventory.domain;
