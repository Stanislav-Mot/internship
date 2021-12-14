package com.internship.internship.controller;

import com.internship.internship.model.Progress;
import com.internship.internship.repository.ProgressRepo;
import com.internship.internship.service.ProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/progress/{id}")
    public Progress getProgress(@PathVariable Long id) {
        return progressService.getById(id);
    }

    @GetMapping("/progress")
    public List<Progress> getAllProgress() {
        return progressService.getAll();
    }

    @PostMapping("/progress")
    public Integer addProgress(@RequestBody Progress progress) {
        return progressService.add(progress);
    }

    @PutMapping("/progress")
    public Integer updateTask(@RequestBody Progress progress) {
        return progressService.update(progress);
    }

    @DeleteMapping("/progress/{id}")
    public Integer delete(@PathVariable Long id) {
        return progressService.delete(id);
    }
}
