package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.service.GroupService;
import com.internship.internship.transfer.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/group/{id}")
    public GroupDto get(@PathVariable Long id) {
        return groupService.getById(id);
    }

    @GetMapping("/group")
    public List<GroupDto> getAll() {
        return groupService.getAll();
    }

    @Validated(Transfer.New.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/group")
    public Integer add(@Valid @RequestBody GroupDto groupDto) {
        return groupService.add(groupDto);
    }

    @Validated(Transfer.Update.class)
    @PostMapping("/group/{id}/task")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addTaskToGroup(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        return groupService.addTask(id, taskDto);
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

    @Validated(Transfer.Update.class)
    @PutMapping("/group")
    public ResponseEntity<Integer> update(@Valid @RequestBody GroupDto groupDto) {
        Integer countUpdatedRow = groupService.update(groupDto);
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
