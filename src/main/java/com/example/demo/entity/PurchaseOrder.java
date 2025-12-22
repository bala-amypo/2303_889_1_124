package com.example.demo.entity;

import com.example.demo.exception.BadRequestException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String poNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dateIssued;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SpendCategory category;

    public PurchaseOrder() {}

    public PurchaseOrder(String poNumber, BigDecimal amount, LocalDate dateIssued, Supplier supplier, SpendCategory category) {
        this.poNumber = poNumber;
        this.amount = amount;
        this.dateIssued = dateIssued;
        this.supplier = supplier;
        this.category = category;
    }

    @PrePersist
    @PreUpdate
    public void validate() {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be positive");
        }
        if (dateIssued.isAfter(LocalDate.now())) {
            throw new BadRequestException("dateIssued cannot be in the future");
        }
        if (supplier == null || !supplier.getIsActive()) {
            throw new BadRequestException("Supplier is inactive or null");
        }
        if (category == null || !category.getActive()) {
            throw new BadRequestException("Category is inactive or null");
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDateIssued() { return dateIssued; }
    public void setDateIssued(LocalDate dateIssued) { this.dateIssued = dateIssued; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
    public SpendCategory getCategory() { return category; }
    public void setCategory(SpendCategory category) { this.category = category; }
}
