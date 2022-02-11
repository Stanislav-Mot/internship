package com.internship.internship.controller;

import com.internship.internship.dto.UserDto;
import com.internship.internship.service.UserService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "User", description = "Crud for person")
@Validated
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/user/{id}")
    public UserDto getId(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Secured("ROLE_USER")
    @Operation(summary = "Get all users")
    @GetMapping("/user")
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @Secured("ROLE_ADMIN")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(value = "{\"email\": \"user@user.com\", \"password\": \"12345678\"}")}))
    @Operation(summary = "Add new user")
    @Validated(Transfer.New.class)
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserDto userDto) throws Exception {
        return userService.add(userDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(value = "{\"id\": 7, \"password\": \"12345678\"}")}))
    @Operation(summary = "Update user")
    @Validated(Transfer.Update.class)
    @PutMapping("/user")
    public UserDto update(@Valid @RequestBody UserDto userDto) {
        return userService.update(userDto);
    }
}
