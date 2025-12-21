package com.example.demo.controller;

import com.example.demo.entity.DiversityTarget;
import com.example.demo.service.DiversityTargetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/targets")
public class DiversityTargetController {

    private final DiversityTargetService service;

    public DiversityTargetController(DiversityTargetService service) {
        this.service = service;
    }

    @PostMapping
    public DiversityTarget createTarget(
            @RequestBody DiversityTarget target) {
        return service.createTarget(target);
    }

    @GetMapping("/active")
    public List<DiversityTarget> getActiveTargets() {
        return service.getActiveTargets();
    }
}
