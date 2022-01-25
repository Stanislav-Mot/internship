package com.internship.internship.mapper;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.internship.internship.util.Helper.newTaskDtoForTest;
import static com.internship.internship.util.Helper.newTaskForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskDtoMapperTest {

    @Autowired
    private TaskDtoMapper taskDtoMapper;

    @Test
    void convertToDto() {
        Task task = newTaskForTest();
        TaskDto taskDto = taskDtoMapper.convertToDto(task);

        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getProgress(), taskDto.getProgress());
        assertEquals(task.getName(), taskDto.getName());
        assertEquals(task.getStartTime(), taskDto.getStartTime());
    }

    @Test
    void convertToEntity() {
        TaskDto taskDto = newTaskDtoForTest();
        Task task = taskDtoMapper.convertToEntity(taskDto);

        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getProgress(), taskDto.getProgress());
        assertEquals(task.getName(), taskDto.getName());
        assertEquals(task.getStartTime(), taskDto.getStartTime());
    }
}