package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.PersonDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Person", description = "Crud for person")
@Validated
@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Get person by id")
    @GetMapping("/person/{id}")
    public PersonDto getId(@PathVariable Long id) {
        return personService.getById(id);
    }

    @Operation(summary = "Get all persons")
    @GetMapping("/person")
    public List<PersonDto> getAll() {
        return personService.getAll();
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"firstName\": \"Denis\", \"lastName\": \"Denisov\", \"age\": 12}")})
    )
    @Operation(summary = "Add new person")
    @Validated(Transfer.New.class)
    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer add(@Valid @RequestBody PersonDto personDto) {
        return personService.add(personDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1}")})
    )
    @Operation(summary = "Add group to person")
    @Validated(Transfer.Update.class)
    @PostMapping("/person/{id}/group")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addGroupToPerson(@PathVariable Long id, @Valid @RequestBody GroupDto groupDto) {
        return personService.addGroup(id, groupDto);
    }

    @Operation(summary = "Delete group from person")
    @PutMapping("/person/{id}/group/{groupId}")
    public Integer updateConstraints(@PathVariable Long id, @PathVariable Long groupId) {
        return personService.deleteGroup(id, groupId);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"id\": 1, \"firstName\": \"Denis\", \"lastName\": \"Denisov\", \"age\": 12}")})
    )
    @Operation(summary = "Update person")
    @Validated(Transfer.Update.class)
    @PutMapping("/person")
    public Integer update(@Valid @RequestBody PersonDto personDto) {
        return personService.update(personDto);
    }

    @Operation(summary = "Delete person by id")
    @DeleteMapping("/person/{id}")
    public Integer delete(@PathVariable Long id) {
        return personService.delete(id);
    }
}
