package com.internship.internship.controller;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.service.ProgressService;
import com.internship.internship.transfer.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/progress/{id}")
    public ProgressDto getId(@PathVariable Long id) {
        return progressService.getById(id);
    }

    @GetMapping("/progress")
    public List<ProgressDto> getAll() {
        return progressService.getAll();
    }

    @Validated(Transfer.New.class)
    @PostMapping("/progress")
    public ResponseEntity<Integer> add(@Valid @RequestBody ProgressDto progressDto) {
        Integer countUpdatedRow = progressService.add(progressDto);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @Validated(Transfer.Update.class)
    @PutMapping("/progress")
    public ResponseEntity<Integer> update(@Valid @RequestBody ProgressDto progressDto) {
        Integer countUpdatedRow = progressService.update(progressDto);
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
