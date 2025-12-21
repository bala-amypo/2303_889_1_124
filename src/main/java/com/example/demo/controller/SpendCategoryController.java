package com.example.demo.controller;

import com.example.demo.entity.SpendCategory;
import com.example.demo.repository.SpendCategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class SpendCategoryController {

    private final SpendCategoryRepository repo;

    private SpendCategoryController(SpendCategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    private List<SpendCategory> all() {
        return repo.findAll();
    }
}
