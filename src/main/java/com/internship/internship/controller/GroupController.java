package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/group/{id}")
    public Group getGroup(@PathVariable Long id) {
        return groupService.getById(id);
    }

    @GetMapping("/group")
    public List<Group> getAllGroups() {
        return groupService.getAll();
    }

    @PostMapping("/group")
    public Integer addGroup(@RequestBody Group group) {
        return groupService.add(group);
    }

    @PostMapping("/group/{id}/task")
    public Integer addGroupToTask(@PathVariable Long id, @RequestBody Task task) {
        return groupService.addTask(id, task);
    }

    @DeleteMapping("/group/{id}/task/{idTask}")
    public Integer deleteGroupFromTask(@PathVariable Long id, @PathVariable Long idTask) {
        return groupService.deleteTask(id, idTask);
    }

    @PutMapping("/group")
    public Integer updateGroup(@RequestBody Group group) {
        return groupService.update(group);
    }

    @DeleteMapping("/group/{id}")
    public Integer delete(@PathVariable Long id) {
        return groupService.delete(id);
    }
}
