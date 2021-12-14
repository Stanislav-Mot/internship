package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.service.TaskService;
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
    public Integer addTask(@RequestBody Task task) {
        return taskService.add(task);
    }

    @PostMapping("/task/{id}/group")
    public Integer addGroupToTask(@PathVariable Long id, @RequestBody Group group) {
        return taskService.addGroup(id, group);
    }

    @DeleteMapping("/task/{id}/group/{idGroup}")
    public Integer deleteGroupFromTask(@PathVariable Long id, @PathVariable Long groupId) {
        return taskService.deleteGroup(id, groupId);
    }

    @PutMapping("/task")
    public Integer updateTask(@RequestBody Task task) {
        return taskService.update(task);
    }

    @DeleteMapping("/task/{id}")
    public Integer delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
