package com.example.demo.service;

import com.example.demo.entity.DiversityClassification;
import java.util.List;

public interface DiversityClassificationService {
    DiversityClassification createClassification(DiversityClassification classification);
    DiversityClassification getClassificationById(Long id);
    List<DiversityClassification> getAllClassifications();
    void deactivateClassification(Long id);
}
