package com.internship.internship.mapper;

import com.internship.internship.dto.PriorityDto;
import com.internship.internship.model.Priority;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PriorityDtoMapper {
    private final ModelMapper modelMapper;

    public PriorityDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PriorityDto convertToDto(Priority priority) {
        return modelMapper.map(priority, PriorityDto.class);
    }

    public Priority convertToEntity(PriorityDto priorityDto) {
        return modelMapper.map(priorityDto, Priority.class);
    }
}