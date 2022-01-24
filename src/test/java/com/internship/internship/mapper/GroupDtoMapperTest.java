package com.internship.internship.mapper;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.model.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.internship.internship.util.Helper.newGroupDtoForTest;
import static com.internship.internship.util.Helper.newGroupForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GroupDtoMapperTest {

    @Autowired
    private GroupDtoMapper groupDtoMapper;

    @Test
    void convertToDto() {
        Group group = newGroupForTest();
        GroupDto groupDto = groupDtoMapper.convertToDto(group);

        assertEquals(group.getId(), groupDto.getId());
        assertEquals(group.getName(), groupDto.getName());
    }

    @Test
    void convertToEntity() {
        GroupDto groupDto = newGroupDtoForTest();
        Group group = groupDtoMapper.convertToEntity(groupDto);

        assertEquals(group.getId(), groupDto.getId());
        assertEquals(group.getName(), groupDto.getName());
    }
}