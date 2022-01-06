package com.internship.internship.controller;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.service.ProgressService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Progress", description = "Crud for progress")
@Validated
@RestController
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @Operation(summary = "Get progress by id")
    @GetMapping("/progress/{id}")
    public ProgressDto getId(@PathVariable Long id) {
        return progressService.getById(id);
    }

    @Operation(summary = "Get all progress by id")
    @GetMapping("/progress")
    public List<ProgressDto> getAll() {
        return progressService.getAll();
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"id_task\": 4, \"percents\": 12}")})
    )
    @Operation(summary = "Add progress")
    @Validated(Transfer.New.class)
    @PostMapping("/progress")
    public Integer add(@Valid @RequestBody ProgressDto progressDto) {
        return progressService.add(progressDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"percents\": 12}")})
    )
    @Operation(summary = "Update progress")
    @Validated(Transfer.Update.class)
    @PutMapping("/progress")
    public Integer update(@Valid @RequestBody ProgressDto progressDto) {
        return progressService.update(progressDto);
    }

    @Operation(summary = "Delete progress by id")
    @DeleteMapping("/progress/{id}")
    public Integer delete(@PathVariable Long id) {
        return progressService.delete(id);
    }
}
