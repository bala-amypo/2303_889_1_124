package com.example.demo.service;

import com.example.demo.entity.DiversityTarget;
import java.util.List;

public interface DiversityTargetService {
    DiversityTarget createTarget(DiversityTarget target);
    List<DiversityTarget> getTargetsByYear(int year);
    List<DiversityTarget> getAllTargets();
    void deactivateTarget(Long id);
}
