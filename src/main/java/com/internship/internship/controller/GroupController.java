package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.repository.GroupRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    GroupRepo tasksGroupRepo;

    public GroupController(GroupRepo tasksGroupRepo) {
        this.tasksGroupRepo = tasksGroupRepo;
    }

    @GetMapping("/group/{id}")
    public Group getGroup(@PathVariable Long id ) {
        return tasksGroupRepo.getById(id);
    }

    @GetMapping("/group")
    public List<Group> getAllGroups(){
        return tasksGroupRepo.getAll();
    }

    @PostMapping("/group")
    public Integer addGroup(@RequestBody Group tasksGroup){
        return tasksGroupRepo.add(tasksGroup);
    }

    @PutMapping("/group")
    public Integer updateGroup(@RequestBody Group tasksGroup){
        return tasksGroupRepo.update(tasksGroup);
    }

    @DeleteMapping("/group/{id}")
    public Integer delete(@PathVariable Long id ){
        return tasksGroupRepo.delete(id);
    }
}
