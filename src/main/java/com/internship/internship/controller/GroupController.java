package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.service.GroupService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Group", description = "Crud for group")
@Validated
@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Get group by id")
    @GetMapping("/group/{id}")
    public GroupDto getById(@PathVariable Long id) {
        return groupService.getById(id);
    }

    @Operation(summary = "Get group by person id")
    @GetMapping("/group/person/{id}")
    public List<GroupDto> getByPersonId(@PathVariable Long id) {
        return groupService.getByPersonId(id);
    }

    @Operation(summary = "Get all groups")
    @GetMapping("/group")
    public List<GroupDto> getAll() {
        return groupService.getAll();
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"name\": \"Home\"}")}))
    @Operation(summary = "Add new group")
    @Validated(Transfer.New.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/group")
    public Integer add(@Valid @RequestBody GroupDto groupDto) {
        return groupService.add(groupDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"name\": \"Home\"}")}))
    @Operation(summary = "Update group")
    @Validated(Transfer.Update.class)
    @PutMapping("/group")
    public Integer update(@Valid @RequestBody GroupDto groupDto) {
        return groupService.update(groupDto);
    }

    @Operation(summary = "Delete group by id")
    @DeleteMapping("/group/{id}")
    public Integer delete(@PathVariable Long id) {
        return groupService.delete(id);
    }

    @Operation(summary = "Add group to group")
    @PutMapping("/group/{id}/addGroup/{groupId}")
    public Integer addGroupToGroup(@PathVariable Long id, @PathVariable Long groupId) {
        return groupService.addGroup(id, groupId);
    }

    @Operation(summary = "Delete group from group")
    @PutMapping("/group/{id}/deleteGroup/{groupId}")
    public Integer deleteGroupFromGroup(@PathVariable Long id, @PathVariable Long groupId) {
        return groupService.deleteGroup(id, groupId);
    }

    @Operation(summary = "Add task to group")
    @PutMapping("/group/{id}/addTask/{taskId}")
    public Integer addTaskToGroup(@PathVariable Long id, @PathVariable Long taskId) {
        return groupService.addTask(id, taskId);
    }

    @Operation(summary = "Delete task from group")
    @PutMapping("/group/{id}/deleteTask/{taskId}")
    public Integer deleteTaskFromGroup(@PathVariable Long id, @PathVariable Long taskId) {
        return groupService.deleteTask(id, taskId);
    }
}
