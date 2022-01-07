package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.PersonDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.transfer.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person/{id}")
    public PersonDto getId(@PathVariable Long id) {
        return personService.getById(id);
    }

    @GetMapping("/person")
    public List<PersonDto> getAll() {
        return personService.getAll();
    }

    @Validated(Transfer.New.class)
    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer add(@Valid @RequestBody PersonDto personDto) {
        return personService.add(personDto);
    }

    @Validated(Transfer.Update.class)
    @PostMapping("/person/{id}/group")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addGroupToPerson(@PathVariable Long id, @Valid @RequestBody GroupDto groupDto) {
        return personService.addGroup(id, groupDto);
    }

    @PutMapping("/person/{id}/group/{groupId}")
    public Integer updateConstraints(@PathVariable Long id, @PathVariable Long groupId) {
        return personService.deleteGroup(id, groupId);
    }

    @Validated(Transfer.Update.class)
    @PutMapping("/person")
    public Integer update(@Valid @RequestBody PersonDto personDto) {
        return personService.update(personDto);
    }

    @DeleteMapping("/person/{id}")
    public Integer delete(@PathVariable Long id) {
        return personService.delete(id);
    }
}
