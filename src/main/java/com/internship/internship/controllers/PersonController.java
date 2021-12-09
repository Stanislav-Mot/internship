package com.internship.internship.controllers;

import com.internship.internship.models.Person;
import com.internship.internship.repositories.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonRepo personRepo;

    @GetMapping("/person/{id}")
    Person getPerson(@PathVariable Long id ) {
        return personRepo.getById(id);
    }

    @GetMapping("/person")
    List<Person> getAllPersons(){
       return personRepo.getAll();
    }

    @PostMapping("/person")
    Integer addPerson(@RequestBody Person person){
        return personRepo.add(person);
    }

    @PutMapping("/person")
    Integer updatePerson(@RequestBody Person person){
        return personRepo.update(person);
    }

    @DeleteMapping("/person/{id}")
    Integer delete(@PathVariable Long id ){
        return personRepo.delete(id);
    }
}
