package com.example.demo.service;

import com.example.demo.entity.DiversityClassification;
import com.example.demo.repository.DiversityClassificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiversityClassificationService {

    private final DiversityClassificationRepository repository;

    public DiversityClassificationService(DiversityClassificationRepository repository) {
        this.repository = repository;
    }

    public List<DiversityClassification> getActiveClassifications() {
        return repository.findByActiveTrue();
    }
}
