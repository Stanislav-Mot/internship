package com.internship.internship.controller;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.service.TaskService;
import com.internship.internship.transfer.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/task/{id}")
    public TaskDto getId(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @GetMapping("/task")
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }

    @Validated(Transfer.New.class)
    @PostMapping("/task")
    public Integer add(@Valid @RequestBody TaskDto taskDto) {
        return taskService.add(taskDto);
    }

    @Validated(Transfer.Update.class)
    @PutMapping("/task")
    public Integer update(@Valid @RequestBody TaskDto taskDto) {
        return taskService.update(taskDto);
    }

    @DeleteMapping("/task/{id}")
    public Integer delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
