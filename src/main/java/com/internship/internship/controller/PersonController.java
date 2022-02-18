package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
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
                    value = "{\"id\": 1, " +
                            "\"firstName\": \"Denis\", " +
                            "\"lastName\": \"Denisov\", " +
                            "\"birthdate\": \"1967-05-10\"}")}))
    @Operation(summary = "Update person")
    @Validated(Transfer.Update.class)
    @PutMapping("/person")
    public PersonDto update(@Valid @RequestBody PersonDto personDto) {
        return personService.update(personDto);
    }

    @Operation(summary = "Add group to person")
    @PutMapping("/person/{personId}/addGroup/{groupId}")
    public PersonDto addGroupToPerson(@PathVariable Long personId, @PathVariable Long groupId) {
        return personService.addGroup(personId, groupId);
    }

    @Operation(summary = "Delete group from person")
    @PutMapping("/person/{personId}/deleteGroup/{groupId}")
    public void deleteGroupFromPerson(@PathVariable Long personId, @PathVariable Long groupId) {
        personService.deleteGroup(personId, groupId);
    }
}
