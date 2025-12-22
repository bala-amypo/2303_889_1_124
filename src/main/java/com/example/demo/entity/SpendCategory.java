package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "spend_categories")
public class SpendCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean active = true;

    // One-to-many with PurchaseOrder
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    public SpendCategory() {}

    public SpendCategory(String name) {
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (active == null) active = true;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Set<PurchaseOrder> getPurchaseOrders() { return purchaseOrders; }
    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) { this.purchaseOrders = purchaseOrders; }
}
