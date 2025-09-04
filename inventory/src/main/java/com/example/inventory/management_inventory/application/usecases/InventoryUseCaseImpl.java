package com.example.inventory.management_inventory.application.usecases;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.inventory.management_inventory.application.ports.in.InventoryUseCase;
import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.repositories.InventoryRepository;

@Service
public class InventoryUseCaseImpl implements InventoryUseCase {

  private final InventoryRepository repository;

  public InventoryUseCaseImpl(InventoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Inventory> findByIdProduct(String idProduct) {
    return repository.findByIdProduct(idProduct);
  }
}

