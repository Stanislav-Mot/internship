package com.internship.internship.mapper;

import com.internship.internship.dto.PriorityDto;
import com.internship.internship.model.Priority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.internship.internship.util.Helper.newPriorityDtoForTest;
import static com.internship.internship.util.Helper.newPriorityForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PriorityDtoMapperTest {

    @Autowired
    private PriorityDtoMapper mapper;

    @Test
    void convertToDto() {
        Priority priority = newPriorityForTest();
        PriorityDto priorityDto = mapper.convertToDto(priority);

        assertEquals(priority.getId(), priorityDto.getId());
        assertEquals(priority.getGroup().getId(), priorityDto.getGroupDto().getId());
        assertEquals(priority.getTask().getId(), priorityDto.getTaskDto().getId());
        assertEquals(priority.getPriority(), priorityDto.getPriority());
    }

    @Test
    void convertToEntity() {
        PriorityDto priorityDto = newPriorityDtoForTest();
        Priority priority = mapper.convertToEntity(priorityDto);

        assertEquals(priority.getId(), priorityDto.getId());
        assertEquals(priority.getGroup().getId(), priorityDto.getGroupDto().getId());
        assertEquals(priority.getTask().getId(), priorityDto.getTaskDto().getId());
        assertEquals(priority.getPriority(), priorityDto.getPriority());
    }
}