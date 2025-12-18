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
    public DiversityTarget create(@RequestBody DiversityTarget target) {
        return service.createTarget(target);
    }

    @PutMapping("/{id}")
    public DiversityTarget update(@PathVariable Long id, @RequestBody DiversityTarget target) {
        return service.updateTarget(id, target);
    }

    @GetMapping
    public List<DiversityTarget> getAll() {
        return service.getAllTargets();
    }

    @GetMapping("/year/{year}")
    public List<DiversityTarget> getByYear(@PathVariable Integer year) {
        return service.getTargetsByYear(year);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateTarget(id);
    }
}
