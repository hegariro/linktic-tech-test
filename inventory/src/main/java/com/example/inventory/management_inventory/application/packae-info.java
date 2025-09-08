/**
 * Capa de aplicación del módulo de gestión de inventario.
 * 
 * <p>Esta capa orquesta las operaciones del sistema, coordinando las interacciones
 * entre el dominio y la infraestructura. Implementa los casos de uso del negocio
 * y maneja las transacciones y la coordinación entre diferentes agregados.</p>
 * 
 * <h2>Responsabilidades:</h2>
 * <ul>
 *   <li>Implementar los casos de uso específicos del negocio</li>
 *   <li>Orquestar llamadas al dominio y servicios externos</li>
 *   <li>Manejar transacciones y consistencia de datos</li>
 *   <li>Validar datos de entrada antes de pasar al dominio</li>
 *   <li>Coordinar operaciones entre múltiples agregados</li>
 * </ul>
 * 
 * <h2>Arquitectura de puertos y adaptadores:</h2>
 * <p>Esta capa define los puertos (interfaces) que serán implementados 
 * por los adaptadores en la capa de infraestructura:</p>
 * 
 * <h3>Puertos de entrada (Driving Ports):</h3>
 * <ul>
 *   <li>{@link com.example.inventory.management_inventory.application.ports.in.InventoryUseCase} - Define casos de uso de inventario</li>
 * </ul>
 * 
 * <h3>Puertos de salida (Driven Ports):</h3>
 * <ul>
 *   <li>{@link com.example.inventory.management_inventory.application.ports.out.InventoryRepository} - Contrato para persistencia</li>
 * </ul>
 * 
 * <h2>Casos de uso implementados:</h2>
 * <ul>
 *   <li>{@link com.example.inventory.management_inventory.application.usecases.InventoryUseCaseImpl} - Implementación de casos de uso de inventario</li>
 * </ul>
 * 
 * <h2>Reglas de dependencia:</h2>
 * <ul>
 *   <li>✅ Puede depender de la capa de dominio</li>
 *   <li>✅ Define interfaces para servicios externos (puertos)</li>
 *   <li>✅ Puede usar anotaciones de framework (@Service, @Transactional)</li>
 *   <li>❌ NO debe depender de la capa de infraestructura</li>
 *   <li>❌ NO debe importar implementaciones concretas de repositorios</li>
 *   <li>❌ NO debe contener detalles de persistencia o frameworks web</li>
 * </ul>
 * 
 * <h2>Patrones implementados:</h2>
 * <ul>
 *   <li><strong>Use Case Pattern</strong> - Casos de uso específicos del negocio</li>
 *   <li><strong>Port and Adapter Pattern</strong> - Separación clara de interfaces e implementaciones</li>
 *   <li><strong>Dependency Inversion</strong> - Dependencia de abstracciones, no de implementaciones</li>
 * </ul>
 * 
 * @since 1.0
 */
package com.example.inventory.management_inventory.application;
