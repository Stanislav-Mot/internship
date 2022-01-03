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
    public ResponseEntity<Integer> add(@Valid @RequestBody TaskDto taskDto) {
        Integer countUpdatedRow = taskService.add(taskDto);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @Validated(Transfer.Update.class)
    @PutMapping("/task")
    public ResponseEntity<Integer> update(@Valid @RequestBody TaskDto taskDto) {
        Integer countUpdatedRow = taskService.update(taskDto);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        Integer countUpdatedRow = taskService.delete(id);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }
}
