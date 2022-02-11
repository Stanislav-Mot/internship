package com.internship.internship.mapper;

import com.internship.internship.dto.UserDto;
import com.internship.internship.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    private final ModelMapper modelMapper;

    public UserDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto convertToDto(KeyHolder holder) {
        return modelMapper.map(holder.getKeys(), UserDto.class);
    }

    public User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
