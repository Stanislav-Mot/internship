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
    public Person getPerson(@PathVariable Long id) {
        return personService.getById(id);
    }

    @GetMapping("/person")
    public List<Person> getAllPersons() {
        return personService.getAll();
    }

    @PostMapping("/person")
    public ResponseEntity<Integer> addPerson(@RequestBody Person person) {
        return new ResponseEntity(personService.add(person) , HttpStatus.CREATED); // нужно параметризировать<>
    }

    @PostMapping("/person/{id}/group")
    public Integer addGroupToPerson(@PathVariable Long id, @RequestBody Group group) {
        return personService.addGroup(id, group);
    }

    @DeleteMapping("/person/{id}/group/{groupId}")
    public Integer deleteGroupFromPerson(@PathVariable Long id, @PathVariable Long groupId) {
        return personService.deleteGroup(id, groupId);
    }

    @PutMapping("/person")
    public ResponseEntity<Integer> updatePerson(@RequestBody Person person) {
        Integer countUpdatedRow = personService.update(person);
        if(countUpdatedRow > 0){
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);  // Достаточно пустых <>
        }else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/person/{id}")
    public Integer delete(@PathVariable Long id) {
        return personService.delete(id);
    }
}
