package com.internship.internship.service;

import com.internship.internship.dto.UserDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.UserDtoMapper;
import com.internship.internship.model.Role;
import com.internship.internship.model.User;
import com.internship.internship.repository.UserRepo;
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

    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper mapper;
    private final UserRepo repository;

    public UserService(PasswordEncoder passwordEncoder, UserDtoMapper userDtoMapper, UserRepo repository) {
        this.passwordEncoder = passwordEncoder;
        this.mapper = userDtoMapper;
        this.repository = repository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + email + "' not found"));
    }

    public UserDto getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format("User Id %d is not found", id)));
        return mapper.convertToDto(user);
    }

    public List<UserDto> getAll() {
        return repository.findAll().stream().map(user -> {
            user.setPassword("hidden");
            return mapper.convertToDto(user);
        }).collect(Collectors.toList());
    }

    @Transactional
    public UserDto add(UserDto userDto) {
        User user = mapper.convertToEntity(userDto);
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new ChangesNotAppliedException("User already exists with this email");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        User saved = repository.save(user);

        saved.setPassword("hidden");
        return mapper.convertToDto(user);
    }

    public UserDto updateRole(UserDto userDto, Boolean delete) {
        User user = repository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new ChangesNotAppliedException("User not exists with this email"));

        for (Role role : userDto.getRoles()) {
            if (delete) {
//                repository.deleteRole(user.getId(), role.name());
            } else {
//                repository.addRole(user.getId(), role.name());
            }
        }
        User updated = repository.getById(user.getId());
        return mapper.convertToDto(updated);
    }

    public UserDto updatePassword(UserDto userDto) {
        User user = repository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new ChangesNotAppliedException("User not exists with this email"));

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new ChangesNotAppliedException("passwords is different");
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User response = repository.save(user);
        return mapper.convertToDto(response);
    }
}
