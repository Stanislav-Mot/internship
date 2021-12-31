package com.internship.internship.mapper;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.model.Progress;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProgressDtoMapper {

    private ModelMapper modelMapper;

    public ProgressDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProgressDto convertToDto(Progress progress){
        return modelMapper.map(progress, ProgressDto.class);
    }

    public  Progress convertToEntity(ProgressDto progressDto){
        return modelMapper.map(progressDto, Progress.class);
    }
}
