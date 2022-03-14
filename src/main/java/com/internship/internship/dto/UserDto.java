package com.internship.internship.dto;

import com.internship.internship.model.Role;
import com.internship.internship.transfer.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;

    @Email(message = "Incorrect email")
    @NotBlank(message = "Email is required", groups = {Transfer.New.class, Transfer.Update.class, Transfer.UpdateRole.class})
    private String email;

    @NotBlank(message = "Password is required", groups = {Transfer.New.class, Transfer.Update.class,})
    private String password;

    @NotBlank(message = "passwordConfirmation is required", groups = {Transfer.Update.class, Transfer.New.class})
    private String passwordConfirmation;

    private Set<Role> roles;
}
