package com.internship.internship.controller;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.service.TaskService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Task", description = "Crud for tasks")
@Validated
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get task by id")
    @GetMapping("/task/{id}")
    public TaskDto getById(@PathVariable @Parameter(description = "Task id") Long id) {
        return taskService.getById(id);
    }

    @Operation(summary = "Get task by person id")
    @GetMapping("/task/person/{id}")
    public List<TaskDto> getByPersonId(@PathVariable @Parameter(description = "Task id") Long id) {
        return taskService.getByPersonId(id);
    }

    @Operation(summary = "Get task by group id")
    @GetMapping("/task/group/{id}")
    public List<TaskDto> getByGroupId(@PathVariable @Parameter(description = "Task id") Long id) {
        return taskService.getByGroupId(id);
    }

    @Operation(summary = "Get all tasks")
    @GetMapping("/task")
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }


    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"name\": \"Cooking\", " +
                            "\"description\": \"cook fish\"," +
                            "\"estimate\": \"2\"," +
                            "\"priority\": \"2\"}")}))
    @Operation(summary = "Add new Task")
    @Validated(Transfer.New.class)
    @PostMapping("/task")
    public TaskDto add(@Valid @RequestBody TaskDto taskDto) {
        return taskService.add(taskDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 0, " +
                            "\"name\": \"Cooking\", " +
                            "\"description\": \"should something\", " +
                            "\"priority\": 7, " +
                            "\"estimate\": \"3\"}"
            )}))
    @Operation(summary = "update name, description and estimate")
    @Validated(Transfer.Update.class)
    @PutMapping("/task")
    public Integer update(@Valid @RequestBody TaskDto taskDto) {
        return taskService.update(taskDto);
    }

    @Operation(summary = "update progress")
    @Validated(Transfer.Update.class)
    @PutMapping("/task/{id}/progress")
    public Integer updateProgress(@RequestBody Integer progress, @PathVariable Long id) {
        return taskService.updateProgress(id, progress);
    }

    @Operation(summary = "Delete task by id")
    @DeleteMapping("/task/{id}")
    public Integer delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
