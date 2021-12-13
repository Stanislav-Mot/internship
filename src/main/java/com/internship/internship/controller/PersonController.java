package com.internship.internship.controller;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    PersonRepo personRepo;

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

    @PostMapping("/person/{id}/group")
    public Integer addGroupToPerson(@PathVariable Long id, @RequestBody Group group){
        return personRepo.addGroup(id, group);
    }

    @DeleteMapping("/person/{id}/group/{idGroup}")
    public Integer deleteGroupFromPerson(@PathVariable Long id, @PathVariable Long idGroup){
        return personRepo.deleteGroup(id,idGroup);
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
