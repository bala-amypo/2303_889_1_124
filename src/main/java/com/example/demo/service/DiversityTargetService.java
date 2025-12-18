package com.example.demo.service;

import com.example.demo.entity.DiversityTarget;
import com.example.demo.repository.DiversityTargetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiversityTargetService {

    private final DiversityTargetRepository repository;

    public DiversityTargetService(DiversityTargetRepository repository) {
        this.repository = repository;
    }

    public List<DiversityTarget> getTargetsByYear(Integer year) {
        return repository.findByTargetYear(year);
    }

    public List<DiversityTarget> getAllTargets() {
        return repository.findAll();
    }
}
