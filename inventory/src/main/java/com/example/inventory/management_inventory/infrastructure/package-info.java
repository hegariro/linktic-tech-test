/**
 * Capa de infraestructura del módulo de gestión de inventario.
 * 
 * <p>Esta capa contiene los adaptadores que implementan los puertos definidos 
 * en la capa de aplicación, siguiendo el patrón de arquitectura hexagonal.</p>
 * 
 * <h2>Responsabilidades:</h2>
 * <ul>
 *   <li>Controladores REST que exponen los endpoints del API para la gestión de inventarios</li>
 *   <li>Implementaciones de repositorios que persisten los datos de inventario</li>
 *   <li>Mappers entre DTOs de API y objetos de dominio</li>
 *   <li>Configuraciones específicas de framework (Spring, JPA, etc.)</li>
 * </ul>
 * 
 * <h2>Reglas de dependencia:</h2>
 * <ul>
 *   <li>✅ Puede depender de la capa de aplicación y dominio</li>
 *   <li>✅ Puede usar frameworks externos (Spring, JPA, etc.)</li>
 *   <li>❌ La capa de dominio NO debe depender de esta capa</li>
 *   <li>❌ NO debe contener lógica de negocio</li>
 * </ul>
 * 
 * <h2>Componentes principales:</h2>
 * <ul>
 *   <li>{@link com.example.inventory.management_inventory.infrastructure.InventoryController} - Controlador REST principal</li>
 *   <li>Mappers para transformación de datos entre capas</li>
 *   <li>Adaptadores de persistencia y servicios externos</li>
 * </ul>
 * 
 * @since 1.0
 */
package com.example.inventory.management_inventory.infrastructure;
