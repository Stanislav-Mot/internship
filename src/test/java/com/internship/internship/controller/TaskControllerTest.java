package com.internship.internship.controller;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Task;
import com.internship.internship.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.internship.internship.util.Helper.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    public static final Long CORRECT_ID = 999L;
    public static final Long WRONG_ID = 9999L;
    @MockBean
    private TaskService taskService;
    @Autowired
    private TaskController taskController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(taskController);
    }

    @Test
    void getTask() throws Exception {
        TaskDto taskDto = newTaskDtoForTest();

        Mockito.when(taskService.getById(CORRECT_ID)).thenReturn(taskDto);

        mockMvc.perform(get("/task/{id}", CORRECT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(Math.toIntExact(CORRECT_ID))))
                .andExpect(jsonPath("$.name", containsStringIgnoringCase("Tester")));

        Mockito.when(taskService.getById(WRONG_ID)).thenThrow(DataNotFoundException.class).thenReturn(null);

        mockMvc.perform(get("/task/{id}", WRONG_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

        verify(taskService, times(2)).getById(Mockito.any());
    }

    @Test
    void getAllTasks() throws Exception {
        TaskDto taskDto = newTaskDtoForTest();

        List<TaskDto> tasks = Arrays.asList(taskDto);

        Mockito.when(taskService.getAll()).thenReturn(tasks);

        mockMvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", Matchers.contains("Tester")));
    }

    @Test
    void addTask() throws Exception {
        Task task = newTaskForTest();

        Mockito.when(taskService.add(any(TaskDto.class))).thenReturn(1);

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(task))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)))
                .andReturn();

        verify(taskService, times(1)).add(Mockito.any(TaskDto.class));
    }

    @Test
    void updateTask() throws Exception {
        Task task = newTaskForTest();

        when(taskService.update(any(TaskDto.class))).thenReturn(1);

        mockMvc.perform(put("/task")
                .content(asJsonString(task))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        mockMvc.perform(put("/task")
                .content("Wrong JSON")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));

        verify(taskService, times(1)).update(Mockito.any(TaskDto.class));
    }

    @Test
    void deleteTask() throws Exception {
        Task task = newTaskForTest();

        Mockito.when(taskService.delete(task.getId())).thenReturn(1);

        mockMvc.perform(delete("/task/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(taskService, times(1)).delete(Mockito.any(Long.class));
    }
}