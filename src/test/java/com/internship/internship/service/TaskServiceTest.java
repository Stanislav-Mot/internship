package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Task;
import com.internship.internship.repository.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.internship.internship.util.Helper.newTaskDtoForTest;
import static com.internship.internship.util.Helper.newTaskForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private TaskDtoMapper mapper;

    @Test
    void getById() {
        Task task = newTaskForTest();
        TaskDto taskDto = newTaskDtoForTest();

        when(taskRepo.getById(task.getId())).thenReturn(task);
        when(mapper.convertToDto(task)).thenReturn(taskDto);
        TaskDto taskFromService = taskService.getById(task.getId());

        assertEquals(taskFromService, taskDto);

        verify(taskRepo, times(1)).getById(task.getId());
    }

    @Test
    void getAll() {
        List<Task> list = new ArrayList<Task>();
        list.add(new Task());
        list.add(new Task());
        list.add(new Task());

        when(taskRepo.findAll()).thenReturn(list);

        List<TaskDto> taskList = taskService.getAll();

        assertEquals(3, taskList.size());
        verify(taskRepo, times(1)).findAll();
    }

    @Test
    void add() {
        TaskDto taskDto = new TaskDto(1L);
        Task task = new Task();

        when(mapper.convertToEntity(any(TaskDto.class))).thenReturn(task);
        when(taskRepo.save(task)).thenReturn(task);
        when(mapper.getDtoFromHolder(any(KeyHolder.class))).thenReturn(taskDto);

        TaskDto result = taskService.add(taskDto);
        assertEquals(result.getId(), result.getId());
        verify(taskRepo, times(1)).save(task);
    }

    @Test
    void update() {
        TaskDto taskDto = newTaskDtoForTest();
        Task task = newTaskForTest();

        when(taskRepo.save(task)).thenReturn(task);
        when(mapper.convertToEntity(taskDto)).thenReturn(task);
        when(mapper.convertToDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.update(taskDto);
        assertEquals(result.getId(), result.getId());
        verify(taskRepo, times(1)).save(task);
    }

    @Test
    void search() {
        Task task = newTaskForTest();
        List<Task> list = Collections.singletonList(task);

        when(taskRepo.searchCustom(null, null, null, null, null)).thenReturn(list);

        SearchTaskDto searchTaskDto = new SearchTaskDto("", 1, 1, null, null);
        List<TaskDto> taskList = taskService.search(searchTaskDto);
        assertEquals(1, taskList.size());
        verify(taskRepo, times(1)).searchCustom(null, null, null, null, null);
    }
}