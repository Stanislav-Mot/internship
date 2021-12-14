package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    GroupRepo groupRepo; // почему до сих пор не приватное поле?

    public GroupController(GroupRepo tasksGroupRepo) {
        this.groupRepo = tasksGroupRepo;
    }

    @GetMapping("/group/{id}")
    public Group getGroup(@PathVariable Long id ) {
        return groupRepo.getById(id);
    }

    @GetMapping("/group")
    public List<Group> getAllGroups(){
        return groupRepo.getAll();
    }

    @PostMapping("/group")
    public Integer addGroup(@RequestBody Group tasksGroup){
        return groupRepo.add(tasksGroup);
    }

    @PostMapping("/group/{id}/task")
    public Integer addGroupToTask(@PathVariable Long id, @RequestBody Task task){
        return groupRepo.addTask(id, task);
    }

    @DeleteMapping("/group/{id}/task/{idTask}")
    public Integer deleteGroupFromTask(@PathVariable Long id, @PathVariable Long idTask){
        return groupRepo.deleteTask(id,idTask);
    }

    @PutMapping("/group")
    public Integer updateGroup(@RequestBody Group tasksGroup){
        return groupRepo.update(tasksGroup);
    }

    @DeleteMapping("/group/{id}")
    public Integer delete(@PathVariable Long id ){
        return groupRepo.delete(id);
    }
}
