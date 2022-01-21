package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/search/person")
    public List<PersonDto> getPersonsByParameters(
            @Parameter(description = "Person name", example = "Denis")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Person surname", example = "Denisov")
            @RequestParam(required = false) String lastName,
            @Parameter(description = "Person age", example = "33")
            @RequestParam(required = false) Integer exactAge,
            @Parameter(description = "If you want search in a range age, set start", example = "1")
            @RequestParam(required = false) Integer rangeAgeStart,
            @Parameter(description = "If you want search in a range age, set end", example = "99")
            @RequestParam(required = false) Integer rangeAgeEnd
    ) {
        return personService.search(firstName, lastName, exactAge, rangeAgeStart, rangeAgeEnd);
    }

    @Operation(
            summary = "Search persons by token",
            description = "search for occurrence of a token in firstName + lastName"
    )
    @GetMapping("/search/personByToken")
    public List<PersonDto> searchPersonByTokenInName(
            @Parameter(description = "You can search like 'is Deni' if you want found like 'Denis Denisov'", example = "ov")
            @RequestParam String token) {
        return personService.searchByTokenInName(token);
    }

    @Operation(
            summary = "Search task by parameters",
            description = "search for occurrence of a parameters in name (exact) + progress (range) + startTime (range)"
    )
    @GetMapping("/search/task")
    public List<TaskDto> getTasksByParameters(
            @Parameter(description = "Task name", example = "cleaning")
            @RequestParam(required = false) String name,
            @Parameter(description = "Search from min progress", example = "0")
            @RequestParam(required = false) Integer fromProgress,
            @Parameter(description = "Search to max progress", example = "100")
            @RequestParam(required = false) Integer toProgress,
            @Parameter(description = "Search from min start time", example = "2012-12-12")
            @RequestParam(required = false) String minStartTime,
            @Parameter(description = "Search from to start time", example = "2022-12-12")
            @RequestParam(required = false) String maxStartTime
    ) {
        return taskService.search(name, fromProgress, toProgress, minStartTime, maxStartTime);
    }
}
