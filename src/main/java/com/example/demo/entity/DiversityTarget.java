package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "diversity_targets")
public class DiversityTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int targetYear;

    private Double targetPercentage;

    @ManyToOne
    @JoinColumn(name = "classification_id", nullable = false)
    private DiversityClassification classification;

    @Column(nullable = false)
    private Boolean active = true;

    public DiversityTarget() {}

    public DiversityTarget(int targetYear, DiversityClassification classification, Double targetPercentage) {
        this.targetYear = targetYear;
        this.classification = classification;
        this.targetPercentage = targetPercentage;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (active == null) active = true;
        if (targetPercentage < 0 || targetPercentage > 100) {
            throw new IllegalArgumentException("targetPercentage must be between 0 and 100");
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getTargetYear() { return targetYear; }
    public void setTargetYear(int targetYear) { this.targetYear = targetYear; }
    public Double getTargetPercentage() { return targetPercentage; }
    public void setTargetPercentage(Double targetPercentage) { this.targetPercentage = targetPercentage; }
    public DiversityClassification getClassification() { return classification; }
    public void setClassification(DiversityClassification classification) { this.classification = classification; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
