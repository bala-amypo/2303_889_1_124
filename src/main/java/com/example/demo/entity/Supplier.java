package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;


@Entity
@Table(uniqueConstraints = {
@UniqueConstraint(columnNames = {"name"}),
@UniqueConstraint(columnNames = {"email"})
})
public class Supplier {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(nullable = false)
private String name;


@Column(unique = true)
private String registrationNumber;


@Column(nullable = false, unique = true)
private String email;


private String phone;
private String address;


@ManyToMany
private List<DiversityClassification> diversityClassifications;


private Boolean isActive = true;
private Instant createdAt;
private Instant updatedAt;


@PrePersist
public void onCreate() {
this.createdAt = Instant.now();
}
}