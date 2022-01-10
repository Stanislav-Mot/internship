package com.internship.internship.mapper;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.model.Group;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupDtoMapper {

    private final ModelMapper modelMapper;

    public GroupDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GroupDto convertToDto(Group group) {
        return modelMapper.map(group, GroupDto.class);
    }

    public Group convertToEntity(GroupDto groupDto) {
        return modelMapper.map(groupDto, Group.class);
    }
}
