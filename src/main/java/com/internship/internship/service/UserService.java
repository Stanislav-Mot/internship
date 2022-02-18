package com.internship.internship.service;

import com.internship.internship.dto.UserDto;
import com.internship.internship.mapper.UserDtoMapper;
import com.internship.internship.model.Role;
import com.internship.internship.model.User;
import com.internship.internship.repository.UserRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper mapper;

    public UserService(PasswordEncoder passwordEncoder, UserRepo userRepo, UserDtoMapper userDtoMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.mapper = userDtoMapper;
    }

    private MapSqlParameterSource getMapSqlParameterSource(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", user.getId());
        parameters.addValue("email", user.getEmail());
        parameters.addValue("password", user.getPassword());
        parameters.addValue("roles", user.getRoles());
        return parameters;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + email + "' not found");
    }

    public UserDto getById(Long id) {
        return mapper.convertToDto(userRepo.getUserById(id));
    }

    public List<UserDto> getAll() {
        return userRepo.getAll().stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
    }

    public UserDto add(UserDto userDto) throws Exception {
        User user = mapper.convertToEntity(userDto);
        if (userRepo.getUserByEmail(user.getEmail()) != null) {
            throw new Exception("User already exists with this email");
        }
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        MapSqlParameterSource parameters = getMapSqlParameterSource(user);
        KeyHolder keyHolder = userRepo.addUser(parameters);
        UserDto response = mapper.convertToDto(keyHolder);
        response.setPassword(null);
        return response;
    }

    public UserDto updateRole(UserDto userDto) throws Exception {
        if (userRepo.getUserByEmail(userDto.getEmail()) == null) {
            throw new Exception("User not exists with this email");
        }
        User user = mapper.convertToEntity(userDto);
        MapSqlParameterSource parameters = getMapSqlParameterSource(user);
        User response = userRepo.updateRole(parameters);
        return mapper.convertToDto(response);
    }

    public UserDto updatePassword(UserDto userDto, String passwordConfirmation) throws Exception {
        if (userRepo.getUserByEmail(userDto.getEmail()) == null) {
            throw new Exception("User not exists with this email");
        }
        if (!userDto.getPassword().equals(passwordConfirmation)) {
            throw new Exception("passwords is different");
        }
        User user = mapper.convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MapSqlParameterSource parameters = getMapSqlParameterSource(user);
        User response = userRepo.updatePassword(parameters);
        return mapper.convertToDto(response);
    }
}
