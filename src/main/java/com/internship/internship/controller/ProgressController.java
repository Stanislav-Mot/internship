package com.internship.internship.controller;

import com.internship.internship.model.Progress;
import com.internship.internship.repository.ProgressRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgressController {

    ProgressRepo progressRepo;

    public ProgressController(ProgressRepo progressRepo) {
        this.progressRepo = progressRepo;
    }

    @GetMapping("/progress/{id}")
    public Progress getProgress(@PathVariable Long id ) {
        return progressRepo.getById(id);
    }

    @GetMapping("/progress")
    public List<Progress> getAllProgress(){
        return progressRepo.getAll();
    }

    @PostMapping("/progress")
    public Integer addProgress(@RequestBody Progress progress){
        return progressRepo.add(progress);
    }

    @PutMapping("/progress")
    public Integer updateTask(@RequestBody Progress progress){
        return progressRepo.update(progress);
    }

    @DeleteMapping("/progress/{id}")
    public Integer delete(@PathVariable Long id ){
        return progressRepo.delete(id);
    }
}
