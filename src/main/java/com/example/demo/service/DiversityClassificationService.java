package com.example.demo.service;

import com.example.demo.entity.DiversityClassification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiversityClassificationService {

    public DiversityClassification create(DiversityClassification d) {
        return d;
    }

    public List<DiversityClassification> getAll() {
        return List.of();
    }
}
    