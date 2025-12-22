package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Many-to-many with DiversityClassification
    @ManyToMany
    @JoinTable(
            name = "supplier_classifications",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "classification_id")
    )
    private Set<DiversityClassification> diversityClassifications = new HashSet<>();

    // One-to-many with PurchaseOrder
    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    public Supplier() {}

    public Supplier(String name, String email, String registrationNumber) {
        this.name = name;
        this.email = email;
        this.registrationNumber = registrationNumber;
    }

    @PrePersist
    public void prePersist() {
        if (isActive == null) isActive = true;
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Set<DiversityClassification> getDiversityClassifications() { return diversityClassifications; }
    public void setDiversityClassifications(Set<DiversityClassification> diversityClassifications) { this.diversityClassifications = diversityClassifications; }
    public Set<PurchaseOrder> getPurchaseOrders() { return purchaseOrders; }
    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) { this.purchaseOrders = purchaseOrders; }
}
