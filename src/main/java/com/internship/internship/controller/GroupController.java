package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.service.GroupService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Get all groups")
    @GetMapping("/group")
    public List<GroupDto> getAll() {
        return groupService.getAll();
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"name\": \"Home\"}")})
    )
    @Operation(summary = "Add new group")
    @Validated(Transfer.New.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/group")
    public Integer add(@Valid @RequestBody GroupDto groupDto) {
        return groupService.add(groupDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1}")})
    )
    @Operation(summary = "Add task to group")
    @Validated(Transfer.Update.class)
    @PostMapping("/group/{id}/task")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addTaskToGroup(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        return groupService.addTask(id, taskDto);
    }

    @Operation(summary = "Delete task from group")
    @PutMapping("/group/{id}/task/{idTask}")
    public Integer updateConstraints(@PathVariable Long id, @PathVariable Long idTask) {
        return groupService.deleteTask(id, idTask);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"name\": \"Home\"}")})
    )
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
}
