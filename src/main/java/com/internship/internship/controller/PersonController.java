package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.PersonDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.transfer.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Integer> updateConstraints(@PathVariable Long id, @PathVariable Long groupId) {
        Integer countUpdatedRow = personService.deleteGroup(id, groupId);
        if (countUpdatedRow > 0) {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(countUpdatedRow, HttpStatus.NOT_MODIFIED);
        }
    }

    @Validated(Transfer.Update.class)
    @PutMapping("/person")
    public ResponseEntity<Integer> update(@Valid @RequestBody PersonDto personDto) {
        Integer countUpdatedRow = personService.update(personDto);
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
