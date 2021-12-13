package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.TaskRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    TaskRepo taskRepo;

    public TaskController(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @GetMapping("/task/{id}")
    public Task getTask(@PathVariable Long id ) {
        return taskRepo.getById(id);
    }

    @GetMapping("/task")
    public List<Task> getAllTasks(){
        return taskRepo.getAll();
    }

    @PostMapping("/task")
    public Integer addTask(@RequestBody Task task){
        return taskRepo.add(task);
    }

    @PostMapping("/task/{id}/group")
    public Integer addGroupToTask(@PathVariable Long id, @RequestBody Group group){
        return taskRepo.addGroup(id, group);
    }

    @DeleteMapping("/task/{id}/group/{idGroup}")
    public Integer deleteGroupFromTask(@PathVariable Long id, @PathVariable Long idGroup){
        return taskRepo.deleteGroup(id,idGroup);
    }

    @PutMapping("/task")
    public Integer updateTask(@RequestBody Task task){
        return taskRepo.update(task);
    }

    @DeleteMapping("/task/{id}")
    public Integer delete(@PathVariable Long id ){
        return taskRepo.delete(id);
    }
}
