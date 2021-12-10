package com.internship.internship.controller;

import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    private PersonRepo personRepo;

    public PersonController(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable Long id ) {
        return personRepo.getById(id);
    }

    @GetMapping("/person")
    public List<Person> getAllPersons(){
       return personRepo.getAll();
    }

    @PostMapping("/person")
    public Integer addPerson(@RequestBody Person person){
        return personRepo.add(person);
    }

    @PutMapping("/person")
    public Integer updatePerson(@RequestBody Person person){
        return personRepo.update(person);
    }

    @DeleteMapping("/person/{id}")
    public Integer delete(@PathVariable Long id ){
        return personRepo.delete(id);
    }
}
