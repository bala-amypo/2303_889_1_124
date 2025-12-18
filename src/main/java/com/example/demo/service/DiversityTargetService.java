package com.example.demo.service;

import com.example.demo.entity.DiversityTarget;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiversityTargetService {

    public DiversityTarget create(DiversityTarget d) {
        return d;
    }

    public List<DiversityTarget> getAll() {
        return List.of();
    }
}
