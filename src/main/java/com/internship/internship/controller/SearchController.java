package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchPersonDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Search persons by parameters",
            description = "search for occurrence of parameters in firstName (exact) + lastName (exact) + age (range/exact)")
    @GetMapping("/search/person")
    public List<PersonDto> getPersonsByParameters(@Valid SearchPersonDto searchPersonDto) {
        return personService.search(searchPersonDto);
    }

    @Operation(summary = "Search task by parameters",
            description = "search for occurrence of a parameters in name (exact) + progress (range) + startTime (range)")
    @GetMapping("/search/task")
    public List<TaskDto> getTasksByParameters(@Valid SearchTaskDto searchTaskDto) {
        return taskService.search(searchTaskDto);
    }

    @Operation(summary = "Search persons by token",
            description = "search for occurrence of a token in firstName + lastName")
    @GetMapping("/search/personByToken")
    public List<PersonDto> searchPersonByTokenInName(
            @Parameter(description = "You can search like 'is Deni' if you want found like 'Denis Denisov'", example = "ov")
            @RequestParam String token) {
        return personService.searchByTokenInName(token);
    }
}
