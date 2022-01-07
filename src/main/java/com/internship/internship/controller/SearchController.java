package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SearchController {

    private final PersonService personService;
    private final TaskService taskService;

    public SearchController(PersonService personService, TaskService taskService) {
        this.personService = personService;
        this.taskService = taskService;
    }

    @PostMapping("/search/person")
    public List<PersonDto> getPersonsByParameters(@Valid @RequestBody SearchPerson parameters) {
        return personService.search(parameters);
    }

    @GetMapping("/search/personByToken")
    public List<PersonDto> searchPersonByTokenInName(@RequestParam String token) {
        return personService.searchByTokenInName(token);
    }

    @PostMapping("/search/task")
    public List<TaskDto> getTasksByParameters(@Valid @RequestBody SearchTask parameters) {
        return taskService.search(parameters);
    }
}
