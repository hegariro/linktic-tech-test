package com.example.inventory.api.v1.dto;

import java.util.List;

import com.example.inventory.api.v1.dto.InventoryJsonApiResponse.InventoryData;
import com.example.inventory.api.v1.dto.InventoryJsonApiResponse.ProductDataResponse;
import com.example.inventory.api.v1.dto.RelationshipsResponse.Inventory;
import com.example.inventory.api.v1.dto.RelationshipsResponse.Inventory.Data;

public record InventoryResponse() {
   public static InventoryJsonApiResponse fromDomain(
      ProductResponse product,
      InventoryDomainResponse inventory
   ) {
      return new InventoryJsonApiResponse(List.of(
         new JsonApiDataWithRelationship<ProductDataResponse, RelationshipsResponse>(
            product.id(),
            "products",
            InventoryJsonApiResponse.ProductDataResponse.fromDomain(product),
            new RelationshipsResponse(
               new Inventory(new Data(inventory.id(), "inventories")))
         )
      ), List.of(
         new JsonApiData<InventoryData>(
            inventory.id(),
            "inventories",
            InventoryJsonApiResponse.InventoryData.fromDomain(inventory)
         )
      ));
   }
}
