package com.example.demo.repository;

import com.example.demo.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByEmail(String email);
    List<Supplier> findByIsActiveTrue();
}
