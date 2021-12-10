package com.internship.internship.controller;

import com.internship.internship.model.TasksGroup;
import com.internship.internship.repository.TasksGroupRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TasksGroupController {

    TasksGroupRepo tasksGroupRepo;

    public TasksGroupController(TasksGroupRepo tasksGroupRepo) {
        this.tasksGroupRepo = tasksGroupRepo;
    }

    @GetMapping("/group/{id}")
    public TasksGroup getGroup(@PathVariable Long id ) {
        return tasksGroupRepo.getById(id);
    }

    @GetMapping("/group")
    public List<TasksGroup> getAllGroups(){
        return tasksGroupRepo.getAll();
    }

    @PostMapping("/group")
    public Integer addGroup(@RequestBody TasksGroup tasksGroup){
        return tasksGroupRepo.add(tasksGroup);
    }

    @PutMapping("/group")
    public Integer updateGroup(@RequestBody TasksGroup tasksGroup){
        return tasksGroupRepo.update(tasksGroup);
    }

    @DeleteMapping("/group/{id}")
    public Integer delete(@PathVariable Long id ){
        return tasksGroupRepo.delete(id);
    }
}
