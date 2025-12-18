package com.example.demo.controller;

import com.example.demo.entity.SpendCategory;
import com.example.demo.service.SpendCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class SpendCategoryController {

    private final SpendCategoryService service;

    public SpendCategoryController(SpendCategoryService service) {
        this.service = service;
    }

    @PostMapping
    public SpendCategory create(@RequestBody SpendCategory category) {
        return service.createCategory(category);
    }

    @PutMapping("/{id}")
    public SpendCategory update(@PathVariable Long id, @RequestBody SpendCategory category) {
        return service.updateCategory(id, category);
    }

    @GetMapping("/{id}")
    public SpendCategory get(@PathVariable Long id) {
        return service.getCategoryById(id);
    }

    @GetMapping
    public List<SpendCategory> getAll() {
        return service.getAllCategories();
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateCategory(id);
    }
}
    