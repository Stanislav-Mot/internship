package com.internship.internship.controllers;

import com.internship.internship.models.Person;
import com.internship.internship.models.Progress;
import com.internship.internship.repositories.ProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgressController {
    @Autowired
    ProgressRepo progressRepo;

    @GetMapping("/progress/{id}")
    Person getProgress(@PathVariable Long id ) {
        return progressRepo.getById(id);
    }

    @GetMapping("/progress")
    List<Person> getAllProgresses(){
        return progressRepo.getAll();
    }

    @PostMapping("/progress")
    Person addProgress(Progress progress){
        return progressRepo.add(progress);
    }

    @PutMapping("/progress")
    Person updateProgress(Progress progress){
        return progressRepo.update(progress);
    }

    @DeleteMapping("/progress/{id}")
    Person delete(@PathVariable Long id ){
        return progressRepo.delete(id);
    }
}
