package com.example.inventory.management_inventory.application.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.inventory.management_inventory.application.ports.in.InventoryUseCase;
import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.Product;
import com.example.inventory.management_inventory.domain.models.TransactionData;
import com.example.inventory.management_inventory.domain.models.TransactionType;
import com.example.inventory.management_inventory.domain.repositories.InventoryRepository;
import com.example.inventory.management_inventory.domain.repositories.ProductServiceRespository;

@Service
public class InventoryUseCaseImpl implements InventoryUseCase {

  private final InventoryRepository repository;
  private final ProductServiceRespository producttService;
  private int totalMissingItems;

  public InventoryUseCaseImpl(
    InventoryRepository repository,
    ProductServiceRespository producttService
  ) {
    this.repository = repository;
    this.producttService = producttService;
    this.totalMissingItems = 0;
  }

  @Override
  public Optional<Inventory> findByIdProduct(String idProduct) {
    return repository.findByIdProduct(idProduct);
  }

  @Override
  public Optional<TransactionData> modifyInventory(TransactionType transactionType, List<Inventory> inventoryList) {
    Optional<TransactionData> response = Optional.empty();
    switch (transactionType) {
      case INBOUND -> response = executeTransaction(
        TransactionType.INBOUND, 
        inventoryTransactionValidator(inventoryList)
      );
      case OUTBOUND -> response = executeTransaction(
        TransactionType.OUTBOUND, 
        inventoryTransactionValidator(inventoryList)
      );
    }

    return response;
  }

  private List<Inventory> inventoryTransactionValidator(List<Inventory> inventoryList) {
    List<String> productIdList = inventoryList.stream().map(inv -> inv.idProduct()).toList();
    List<Product> productList = productListValidator(productIdList);

    if (productList.isEmpty()) {
      this.totalMissingItems = inventoryList.size();
      // ninguno de los productos existe en base de datos
      return new ArrayList<>();
    }

    this.totalMissingItems = inventoryList.size() - productList.size();
    List<String> productIdListFilter = productList.stream().map(p -> p.id()).toList();

    return inventoryList.stream().filter(inv -> productIdListFilter.contains(inv.idProduct())).toList();
  }

  private List<Product> productListValidator(List<String> productIdList) {
    List<Product> products = new ArrayList<>();
    for (String productId : productIdList) {
      Optional<Product> response = producttService.searchProductById(productId);

      if (response.isEmpty()) { /** Emitir error */ continue; }

      Product product = response.get();
      products.add(product);
    }

    return products;
  }

  private Optional<TransactionData> executeTransaction(
    TransactionType transactionType, List<Inventory> inventoryList
  ) {
    Optional<TransactionData> response = Optional.empty();

    for (Inventory inventory : inventoryList) {
      Optional<TransactionData> transactionSummary = repository.modifyInventory(transactionType, inventory);

      if (!transactionSummary.isPresent()) { continue; }
      
      TransactionData currentTransaction = transactionSummary.get();
      int totalItemsOk = currentTransaction.totalItemsOk();
      List<String> itemList = new ArrayList<>(currentTransaction.idItemsList());

      if (response.isPresent()) {
          totalItemsOk += response.get().totalItemsOk();
          itemList.addAll(response.get().idItemsList());
      }

      response = Optional.of(new TransactionData(
        UUID.randomUUID().toString(),
        transactionType,
        totalItemsOk,
        this.totalMissingItems,
        currentTransaction.total(),
        itemList
      ));
    }

    return response;
  }
}
