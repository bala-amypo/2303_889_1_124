package com.example.demo.repository;

import com.example.demo.entity.SpendCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpendCategoryRepository extends JpaRepository<SpendCategory, Long> {
    List<SpendCategory> findByActiveTrue();
}
