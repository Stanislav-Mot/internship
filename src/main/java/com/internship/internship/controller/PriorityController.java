package com.internship.internship.controller;

import com.internship.internship.dto.PriorityDto;
import com.internship.internship.service.PriorityService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Priority", description = "Crud for priority")
@Validated
@RestController
public class PriorityController {
    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @Operation(summary = "Get priority by id")
    @GetMapping("/priority/{id}")
    public PriorityDto getById(@PathVariable Long id) {
        return priorityService.getById(id);
    }

    @Operation(summary = "Get all priority by id")
    @GetMapping("/priority")
    public List<PriorityDto> getAll() {
        return priorityService.getAll();
    }

    @Operation(summary = "Get priority by group id")
    @GetMapping("/priority/group/{id}")
    public List<PriorityDto> getByGroupId(@PathVariable Long id) {
        return priorityService.getByGroupId(id);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"task\": {\"id\": 4}, \"group\": {\"id\": 4}, \"priority\": 99}")})
    )
    @Operation(summary = "Add priority")
    @Validated(Transfer.New.class)
    @PostMapping("/priority")
    public Integer add(@Valid @RequestBody PriorityDto priorityDto) {
        return priorityService.add(priorityDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"priority\": 75}")})
    )
    @Operation(summary = "Update priority")
    @Validated(Transfer.Update.class)
    @PutMapping("/priority")
    public Integer update(@Valid @RequestBody PriorityDto priorityDto) {
        return priorityService.update(priorityDto);
    }

    @Operation(summary = "Delete priority by id")
    @DeleteMapping("/priority/{id}")
    public Integer delete(@PathVariable Long id) {
        return priorityService.delete(id);
    }
}
