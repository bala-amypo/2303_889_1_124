package com.example.demo.service.impl;

import com.example.demo.entity.DiversityTarget;
import com.example.demo.repository.DiversityTargetRepository;
import com.example.demo.service.DiversityTargetService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DiversityTargetServiceImpl implements DiversityTargetService {

    private final DiversityTargetRepository repository;

    public DiversityTargetServiceImpl(DiversityTargetRepository repository) {
        this.repository = repository;
    }

    @Override
    public DiversityTarget createTarget(DiversityTarget target) {
        return repository.save(target);
    }

    @Override
    public List<DiversityTarget> getActiveTargets() {
        return repository.findByActiveTrue();
    }
}
