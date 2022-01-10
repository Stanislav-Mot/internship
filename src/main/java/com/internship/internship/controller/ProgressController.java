package com.internship.internship.controller;

import com.internship.internship.model.Progress;
import com.internship.internship.service.ProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/progress/{id}")
    public Progress getId(@PathVariable Long id) {
        return progressService.getById(id);
    }

    @GetMapping("/progress")
    public List<Progress> getAll() {
        return progressService.getAll();
    }

    @PostMapping("/progress")
    public ResponseEntity<Integer> add(@RequestBody Progress progress) {
        Integer countUpdatedRow = progressService.add(progress);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/progress")
    public ResponseEntity<Integer> update(@RequestBody Progress progress) {
        Integer countUpdatedRow = progressService.update(progress);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/progress/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        Integer countUpdatedRow = progressService.delete(id);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }
}
