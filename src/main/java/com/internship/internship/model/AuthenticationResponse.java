package com.internship.internship.model;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;

@Data
public class AuthenticationResponse implements Serializable {
    private final Long id;
    private final String email;
    private final Set<Role> roles;
    private final String token;

    public AuthenticationResponse(String jwt, UserDetails userDetails) {
        User user = (User) userDetails;
        this.id = user.getId();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.token = jwt;
    }
}
