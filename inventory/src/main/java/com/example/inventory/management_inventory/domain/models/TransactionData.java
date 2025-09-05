package com.example.inventory.management_inventory.domain.models;

import java.math.BigDecimal;
import java.util.List;

public record TransactionData(
    String id,
    TransactionType transactionType,
    int totalItemsOk,
    int totalMissingItems,
    BigDecimal total,
    List<String> idItemsList
) { }
