package com.example.inventory.management_inventory.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsResponse;
import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.api.v1.dto.SellProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.SellProductsResponse;
import com.example.inventory.management_inventory.application.ports.in.InventoryUseCase;
import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.TransactionData;
import com.example.inventory.management_inventory.domain.models.TransactionType;
import com.example.inventory.management_inventory.infrastructure.mapper.InventoryMapper;

/**
 * Controlador REST para la gestión de inventario.
 * 
 * <p>Este controlador proporciona endpoints para realizar operaciones de inventario
 * incluyendo búsqueda de productos, compra y venta de productos.
 * Implementa el patrón de arquitectura hexagonal actuando como adaptador
 * de entrada para las operaciones de inventario.</p>
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
public class InventoryController {
    
    private final InventoryUseCase inventoryUseCase;
    private final InventoryMapper mapper;

    /**
     * Constructor del controlador de inventario.
     *
     * @param inventoryUseCase el caso de uso de inventario para la lógica de negocio
     * @param mapper el mapper para convertir entre DTOs y objetos de dominio
     * @throws IllegalArgumentException si algún parámetro es null
     */
    public InventoryController(InventoryUseCase inventoryUseCase, InventoryMapper mapper) {
        this.inventoryUseCase = inventoryUseCase;
        this.mapper = mapper;
    }

    /**
     * Busca un producto en el inventario por su ID.
     *
     * @param idProduct el ID único del producto a buscar
     * @return un {@link Optional} que contiene {@link InventoryDomainResponse} del inventario 
     *         si se encuentra el producto o un {@link Optional#empty} si no se encuentra
     * @throws IllegalArgumentException si idProduct es null o vacío
     * 
     * @since 1.0
     */
    public Optional<InventoryDomainResponse> findByIdProduct(String idProduct) {
        var inventoryResponse = inventoryUseCase.findByIdProduct(idProduct);
        return mapper.toApi(inventoryResponse);
    }

    /**
     * Procesa una transacción de compra de productos.
     * 
     * <p>Esta operación incrementa el stock de los productos especificados
     * en el inventario mediante una transacción de tipo {@link TransactionType#INBOUND}.</p>
     *
     * @param products los atributos de los productos a comprar con sus cantidades
     * @return un {@link Optional} que contiene {@link BuyProductsResponse} de la transacción 
     *         de compra si es exitosa o un {@link Optional#empty} si la transacción falla
     * @throws IllegalArgumentException si products es null o contiene datos inválidos
     * 
     * @since 1.0
     */
    public Optional<BuyProductsResponse> buyProducts(BuyProductsJsonApiAttribs products) {
        List<Inventory> inventoryList = mapper.toDomain(products);
        var buyResponse = inventoryUseCase.modifyInventory(TransactionType.INBOUND, inventoryList);

        if (!buyResponse.isPresent()) {
            return Optional.empty();
        }

        TransactionData response = buyResponse.get();

        return mapper.toApi(response);
    }

    /**
     * Procesa una transacción de venta de productos para decrementar el stock.
     * 
     * <p>Realiza una transacción de tipo {@link TransactionType#OUTBOUND} que decrementa
     * el stock de los productos especificados. La operación valida que exista 
     * stock suficiente antes de ejecutar la transacción.</p>
     * 
     * @param products los atributos de los productos a vender incluyendo cantidades,
     *                no debe ser null y debe contener al menos un producto válido
     * @return un {@link Optional} que contiene {@link SellProductsResponse} si la transacción es exitosa,
     *         o {@link Optional#empty()} si la transacción falla (ej: stock insuficiente)
     * @throws IllegalArgumentException si products es null o contiene datos inválidos
     * 
     * @since 1.0
     */
    public Optional<SellProductsResponse> sellProducts(SellProductsJsonApiAttribs products) {
        List<Inventory> inventoryList = mapper.toDomain(products);
        var sellResponse = inventoryUseCase.modifyInventory(TransactionType.OUTBOUND, inventoryList);

        if (!sellResponse.isPresent()) {
            return Optional.empty();
        }

        TransactionData response = sellResponse.get();

        return mapper.toApiSell(response);
    }
}
