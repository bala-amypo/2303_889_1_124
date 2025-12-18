package com.example.demo.service;

import com.example.demo.entity.SpendCategory;
import com.example.demo.repository.SpendCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpendCategoryService {

    private final SpendCategoryRepository repository;

    public SpendCategoryService(SpendCategoryRepository repository) {
        this.repository = repository;
    }

    public List<SpendCategory> getActiveCategories() {
        return repository.findByActiveTrue();
    }
}
