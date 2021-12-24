package com.internship.internship.controller;

import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.internship.internship.util.JsonHelper.isValidJSON;

@RestController
public class SearchController {

    private final PersonService personService;
    private final TaskService taskService;

    public SearchController(PersonService personService, TaskService taskService) {
        this.personService = personService;
        this.taskService = taskService;
    }

    @PostMapping("/search/person")
    public ResponseEntity<List<Person>> getPersonsByParameters(@RequestBody SearchPerson parameters) {
        List<Person> persons = personService.search(parameters);

        return (persons != null) ?
                new ResponseEntity<>(persons, HttpStatus.OK) :
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/search/task")
    public ResponseEntity<List<Task>> getTasksByParameters(@RequestBody SearchTask parameters) {
        List<Task> tasks = taskService.search(parameters);

        return (tasks != null) ?
                new ResponseEntity<>(tasks, HttpStatus.OK) :
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
