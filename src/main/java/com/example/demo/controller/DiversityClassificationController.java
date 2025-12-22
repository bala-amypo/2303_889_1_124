package com.example.demo.controller;

import com.example.demo.entity.DiversityClassification;
import com.example.demo.service.DiversityClassificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classifications")
public class DiversityClassificationController {

    private final DiversityClassificationService service;

    public DiversityClassificationController(DiversityClassificationService service) {
        this.service = service;
    }

    @PostMapping
    public DiversityClassification create(@RequestBody DiversityClassification dc) {
        return service.createClassification(dc);
    }

    @GetMapping("/{id}")
    public DiversityClassification getById(@PathVariable Long id) {
        return service.getClassificationById(id);
    }

    @GetMapping
    public List<DiversityClassification> getAll() {
        return service.getAllClassifications();
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateClassification(id);
    }
}
