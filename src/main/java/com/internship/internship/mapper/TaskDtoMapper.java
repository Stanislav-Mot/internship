package com.internship.internship.mapper;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoMapper {

    private final ModelMapper modelMapper;

    public TaskDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TaskDto convertToDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

    public Task convertToEntity(TaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }
}
