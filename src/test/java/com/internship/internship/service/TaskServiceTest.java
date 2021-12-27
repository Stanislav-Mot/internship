package com.internship.internship.service;

import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.repository.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.internship.internship.util.Helper.newTaskForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private final Long CORRECT_ID = 999L;
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepo taskRepo;

    @Test
    void getById() {
        Task task = newTaskForTest();

        when(taskRepo.getTaskById(task.getId())).thenReturn(task);

        Task taskFromService = taskService.getById(task.getId());

        assertEquals(taskFromService, task);

        verify(taskRepo, times(1)).getTaskById(task.getId());
    }

    @Test
    void getAll() {
        List<Task> list = new ArrayList<Task>();
        list.add(new Task(33L));
        list.add(new Task(44L));
        list.add(new Task(55L));

        when(taskRepo.getAllTasks()).thenReturn(list);

        List<Task> taskList = taskService.getAll();

        assertEquals(3, taskList.size());
        verify(taskRepo, times(1)).getAllTasks();
    }

    @Test
    void add() {
        Task task = newTaskForTest();

        when(taskRepo.addTask(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = taskService.add(task);

        assertEquals(1, result);

        verify(taskRepo, times(1)).addTask(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        Task task = newTaskForTest();

        when(taskRepo.updateTask(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = taskService.update(task);

        assertEquals(1, result);

        verify(taskRepo, times(1)).updateTask(any(MapSqlParameterSource.class));
    }

    @Test
    void delete() {
        Task task = newTaskForTest();

        when(taskRepo.deleteTask(task.getId())).thenReturn(1);

        Integer result = taskService.delete(task.getId());

        assertEquals(1, result);

        verify(taskRepo, times(1)).deleteTask(task.getId());
    }

    @Test
    void search() {
        SearchTask parameters = new SearchTask(CORRECT_ID, "Tester", null, null, null, null);
        Task task = newTaskForTest();
        List<Task> list = Collections.singletonList(task);

        when(taskRepo.search(any(MapSqlParameterSource.class))).thenReturn(list);

        List<Task> taskList = taskService.search(parameters);

        assertEquals(1, taskList.size());
        verify(taskRepo, times(1)).search(any(MapSqlParameterSource.class));
    }
}