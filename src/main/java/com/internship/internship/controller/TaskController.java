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
    public TaskDto getId(@PathVariable @Parameter(description = "Task id") Long id) {
        return taskService.getById(id);
    }

    @Operation(summary = "Get all tasks")
    @GetMapping("/task")
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }


    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 0, \"name\": \"Cooking\", \"start_time\": \"2012-06-09\", \"id_person\": 0}")})
    )
    @Operation(summary = "Add new Task")
    @Validated(Transfer.New.class)
    @PostMapping("/task")
    public Integer add(@Valid @RequestBody TaskDto taskDto) {
        return taskService.add(taskDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 0, \"name\": \"Cooking\", \"start_time\": \"2012-06-09\", \"id_person\": 0, \"id_progress\": 0}")})
    )
    @Operation(summary = "update task")
    @Validated(Transfer.Update.class)
    @PutMapping("/task")
    public Integer update(@Valid @RequestBody TaskDto taskDto) {
        return taskService.update(taskDto);
    }

    @Operation(summary = "Delete task by id")
    @DeleteMapping("/task/{id}")
    public Integer delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
