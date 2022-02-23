package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.AuthenticationRequest;
import com.internship.internship.model.AuthenticationResponse;
import com.internship.internship.service.AdminService;
import com.internship.internship.service.JwtService;
import com.internship.internship.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtTokenUtil;
    private final UserService userDetailsService;
    private final AdminService adminService;

    public AdminController(AuthenticationManager authenticationManager, JwtService jwtTokenUtil, UserService userDetailsService, AdminService adminService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.adminService = adminService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Wrong password or email", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, userDetails));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/retrieving", method = RequestMethod.GET)
    public List<PersonDto> retrievingAllTasks() {
        return adminService.retrievingAllTasks();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/clear/task", method = RequestMethod.DELETE)
    public PersonDto clearTaskByClientIdOrProgress(
            @RequestParam(name = "clientId") Long clientId,
            @RequestParam(name = "taskProgress", required = false) Long taskProgress) {
        return adminService.clearTaskByClientIdOrProgress(clientId, taskProgress);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/reset/progress", method = RequestMethod.PUT)
    public List<TaskDto> resetProgressOfAllTasks(@RequestParam Long clientId) {
        return adminService.resetProgressOfAllTasks(clientId);
    }

}

