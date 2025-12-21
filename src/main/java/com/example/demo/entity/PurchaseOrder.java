package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private SpendCategory category;

    private PurchaseOrder() {}

    public Supplier getSupplier() { return supplier; }
    public SpendCategory getCategory() { return category; }
}
