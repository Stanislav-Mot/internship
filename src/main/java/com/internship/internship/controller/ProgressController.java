package com.internship.internship.controller;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.service.ProgressService;
import com.internship.internship.transfer.Transfer;
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
    public Integer add(@Valid @RequestBody ProgressDto progressDto) {
        return progressService.add(progressDto);
    }

    @Validated(Transfer.Update.class)
    @PutMapping("/progress")
    public Integer update(@Valid @RequestBody ProgressDto progressDto) {
        return progressService.update(progressDto);
    }

    @DeleteMapping("/progress/{id}")
    public Integer delete(@PathVariable Long id) {
        return progressService.delete(id);
    }
}
