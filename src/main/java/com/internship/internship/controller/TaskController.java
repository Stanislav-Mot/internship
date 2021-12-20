package com.internship.internship.controller;

import com.internship.internship.model.Task;
import com.internship.internship.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/task/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @GetMapping("/task")
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    @PostMapping("/task")
    public ResponseEntity<Integer> addTask(@RequestBody Task task) {
        Integer countUpdatedRow = taskService.add(task);
        if(countUpdatedRow > 0){
            return new ResponseEntity<Integer>(countUpdatedRow, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<Integer>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/task")
    public ResponseEntity<Integer> updateTask(@RequestBody Task task) {
        Integer countUpdatedRow = taskService.update(task);
        if(countUpdatedRow > 0){
            return new ResponseEntity<Integer>(countUpdatedRow, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<Integer>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }    }

    @DeleteMapping("/task/{id}")
    public Integer delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
