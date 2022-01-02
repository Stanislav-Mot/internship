package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/group/{id}")
    public Group get(@PathVariable Long id) {
        return groupService.getById(id);
    }

    @GetMapping("/group")
    public List<Group> getAll() {
        return groupService.getAll();
    }

    @PostMapping("/group")
    public ResponseEntity<Integer> add(@RequestBody Group group) {
        Integer countUpdatedRow = groupService.add(group);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED); // в каком случае у тебя будет not_modified?
        }
    }

    @PostMapping("/group/{id}/task")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addTaskToGroup(@PathVariable Long id, @RequestBody Task task) {
        return groupService.addTask(id, task);
    }

    @PutMapping("/group/{id}/task/{idTask}")
    public ResponseEntity<Integer> updateConstraints(@PathVariable Long id, @PathVariable Long idTask) {
        Integer countUpdatedRow = groupService.deleteTask(id, idTask);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/group")
    public ResponseEntity<Integer> update(@RequestBody Group group) {
        Integer countUpdatedRow = groupService.update(group);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        Integer countUpdatedRow = groupService.delete(id);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }
}
