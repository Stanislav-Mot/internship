package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Integer> addGroup(@RequestBody Group group) {
        Integer countUpdatedRow = groupService.add(group);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping("/group/{id}/task")
    public Integer addTaskToGroup(@PathVariable Long id, @RequestBody Task task) {
        return groupService.addTask(id, task);
    }

    @DeleteMapping("/group/{id}/task/{idTask}")
    public Integer deleteTaskFromGroup(@PathVariable Long id, @PathVariable Long idTask) {
        return groupService.deleteTask(id, idTask);
    }

    @PutMapping("/group")
    public ResponseEntity<Integer> updateGroup(@RequestBody Group group) {
        Integer countUpdatedRow = groupService.update(group);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/group/{id}")
    // почему у тебя с апдейтом логика разделена на accepted/not modified, а для delete - нет?
    public Integer delete(@PathVariable Long id) {
        return groupService.delete(id);
    }
}
