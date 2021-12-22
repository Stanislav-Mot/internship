package com.internship.internship.controller;

import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Person> getPersonByParameters(@RequestBody String json) {
        Person person = isValidJSON(json) ? personService.search(json) : null;

        return (person != null) ?
                new ResponseEntity<>(person, HttpStatus.OK) :
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/search/task")
    public ResponseEntity<Task> getTaskByParameters(@RequestBody String json) {
        Task task = isValidJSON(json) ? taskService.search(json) : null;

        return (task != null) ?
                new ResponseEntity<>(task, HttpStatus.OK) :
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
