package com.internship.internship.controller;

import com.internship.internship.model.Person;
import com.internship.internship.model.Progress;
import com.internship.internship.repository.ProgressRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgressController {

    private ProgressRepo progressRepo;

    public ProgressController(ProgressRepo progressRepo) {
        this.progressRepo = progressRepo;
    }

    @GetMapping("/progress/{id}")
    public Person getProgress(@PathVariable Long id ) {
        return progressRepo.getById(id);
    }

    @GetMapping("/progress")
    public List<Person> getAllProgresses(){
        return progressRepo.getAll();
    }

    @PostMapping("/progress")
    public Person addProgress(Progress progress){
        return progressRepo.add(progress);
    }

    @PutMapping("/progress")
    public Person updateProgress(Progress progress){
        return progressRepo.update(progress);
    }

    @DeleteMapping("/progress/{id}")
    public Person delete(@PathVariable Long id ){
        return progressRepo.delete(id);
    }
}
