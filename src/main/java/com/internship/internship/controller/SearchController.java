package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<PersonDto>> getPersonsByParameters(@RequestBody SearchPerson parameters) {
        List<PersonDto> persons = personService.search(parameters);

        return (persons != null) ?
                new ResponseEntity<>(persons, HttpStatus.OK) :
                new ResponseEntity<>(persons, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/personByToken")
    public List<PersonDto> searchPersonByTokenInName(@RequestParam String token) {
        return personService.searchByTokenInName(token);
    }

    @PostMapping("/search/task")
    public ResponseEntity<List<TaskDto>> getTasksByParameters(@RequestBody SearchTask parameters) {
        List<TaskDto> tasks = taskService.search(parameters);

        return (tasks != null) ?
                new ResponseEntity<>(tasks, HttpStatus.OK) :
                new ResponseEntity<>(tasks, HttpStatus.NOT_FOUND);
    }
}
