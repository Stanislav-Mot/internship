package com.internship.internship.controller;

import com.internship.internship.dto.UserDto;
import com.internship.internship.service.UserService;
import com.internship.internship.transfer.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get user by id")
    @GetMapping("/user/{id}")
    public UserDto getId(@PathVariable Long id) {
        return userService.getById(id);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all users")
    @GetMapping("/user")
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(
                    value = "{\"email\": \"user@user.com\", " +
                            "\"password\": \"12345678\", " +
                            "\"passwordConfirmation\": \"12345678\"}"
            )}))
    @Operation(summary = "Add new user")
    @Validated(Transfer.New.class)
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(value = "{\"email\": \"test@test.com\", \"password\": \"12345678\", \"passwordConfirmation\": \"12345678\"}")}))
    @Operation(summary = "Update user's password")
    @Validated(Transfer.Update.class)
    @PutMapping("/user/password")
    public UserDto updatePassword(@Valid @RequestBody UserDto userDto) {
        return userService.updatePassword(userDto);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {@ExampleObject(value = "{\"email\": \"test@test.com\", \"roles\": [ \"ROLE_USER\" ]}")}))
    @Operation(summary = "Update user")
    @Validated(Transfer.UpdateRole.class)
    @PutMapping("/user/role")
    public UserDto updateRole(@Valid @RequestBody UserDto userDto, @RequestParam(defaultValue = "false") Boolean delete) {
        return userService.updateRole(userDto, delete);
    }
}
