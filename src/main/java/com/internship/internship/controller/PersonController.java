package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person/{id}")
    public Person getId(@PathVariable Long id) {
        return personService.getById(id);
    }

    @GetMapping("/person")
    public List<Person> getAll() {
        return personService.getAll();
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer add(@RequestBody Person person) { // просто save/add
        return personService.add(person);
    }

    @PostMapping("/person/{id}/group")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addGroupToPerson(@PathVariable Long id, @RequestBody Group group) {
        return personService.addGroup(id, group);
    }

    @PutMapping("/person/{id}/group/{groupId}")
    public ResponseEntity<Integer> updateConstraints(@PathVariable Long id, @PathVariable Long groupId) {
        Integer countUpdatedRow = personService.deleteGroup(id, groupId);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/person")
    public ResponseEntity<Integer> update(@RequestBody Person person) {
        Integer countUpdatedRow = personService.update(person);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/person/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        Integer countUpdatedRow = personService.delete(id);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }
}
