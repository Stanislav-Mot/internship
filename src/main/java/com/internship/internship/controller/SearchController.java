package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import javax.validation.Valid;
import java.util.List;

@Tag(name = "Search", description = "Api for searching")
@RestController
public class SearchController {

    private final PersonService personService;
    private final TaskService taskService;

    public SearchController(PersonService personService, TaskService taskService) {
        this.personService = personService;
        this.taskService = taskService;
    }

    @Operation(
            summary = "Search persons by parameters",
            description = "search for occurrence of parameters in firstName (exact) + lastName (exact) + age (range/exact)"
    )
    @PostMapping("/search/person")
    public List<PersonDto> getPersonsByParameters(@Valid @RequestBody SearchPerson parameters) {
        return personService.search(parameters);
    }

    @Operation(
            summary = "Search persons by token",
            description = "search for occurrence of a token in firstName + lastName"
    )
    @GetMapping("/search/personByToken")
    public List<PersonDto> searchPersonByTokenInName(@RequestParam String token) {
        return personService.searchByTokenInName(token);
    }

    @Operation(
            summary = "Search task by parameters",
            description = "search for occurrence of a parameters in name (exact) + progress (range) + startTime (range"
    )
    @PostMapping("/search/task")
    public List<TaskDto> getTasksByParameters(@Valid @RequestBody SearchTask parameters) {
        return taskService.search(parameters);
    }
}
