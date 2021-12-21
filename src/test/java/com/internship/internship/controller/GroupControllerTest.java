package com.internship.internship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import com.internship.internship.service.GroupService;
import lombok.SneakyThrows;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @MockBean
    GroupService groupService; // поля приватные

    @Autowired
    GroupController groupController;

    @Autowired
    MockMvc mockMvc;

    public static final Long CORRECT_ID = 999L;
    public static final Long WRONG_ID = 9999L;

    @Test // методы в junit5 package-private
    public void contextLoads() {
        Assertions.assertNotNull(groupController);
    }

    @Test
    void getGroup() throws Exception {
        Group group = newGroupForTest();

        Mockito.when(groupService.getById(CORRECT_ID)).thenReturn(group);

        mockMvc.perform(get("/group/{id}", CORRECT_ID)).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.is(Math.toIntExact(CORRECT_ID))))
            .andExpect(jsonPath("$.name", containsStringIgnoringCase("Tester")));

        Mockito.when(groupService.getById(WRONG_ID)).thenThrow(DataNotFoundException.class).thenReturn(null);

        mockMvc.perform(get("/group/{id}", WRONG_ID))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

        verify(groupService, times(2)).getById(Mockito.any());
    }

    @Test
    void getAllGroups() throws Exception {
        Group group = newGroupForTest();
        List<Group> groups = Collections.singletonList(group);

        Mockito.when(groupService.getAll()).thenReturn(groups);

        mockMvc.perform(get("/group")).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..name", Matchers.contains("Tester")));
    }

    @Test
    void addGroup() throws Exception {
        Group group = newGroupForTest();

        Mockito.when(groupService.add(any(Group.class))).thenReturn(1);

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(group))
                .characterEncoding("utf-8")
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", Matchers.is(1)))
            .andReturn();

        verify(groupService, times(1)).add(Mockito.any(Group.class));
    }

    @Test
    void addToTaskGroup() throws Exception {
        Group group = newGroupForTest();
        Mockito.when(groupService.addTask(any(Long.class),any(Task.class))).thenReturn(1);

        mockMvc.perform(post("/group/{id}/task", group.getId())
                .content(asJsonString(group))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1))
            .addTask(Mockito.any(Long.class), Mockito.any(Task.class));
    }

    @Test
    void deleteTaskFromGroup() throws Exception {
        Group group = newGroupForTest();
        Task task = newTaskForTest();

        Mockito.when(groupService.deleteTask(group.getId(), task.getId())).thenReturn(1);

        mockMvc.perform(delete("/group/{id}/task/{idTask}", group.getId(), task.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).deleteTask(group.getId(), task.getId());
    }

    @Test
    void updateGroup() throws Exception {
        Group group = newGroupForTest();

        when(groupService.update(any(Group.class))).thenReturn(1);

        mockMvc.perform(put("/group")
                .content(asJsonString(group))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$", Matchers.is(1)));

        mockMvc.perform(put("/group")
                .content("Wrong JSON")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));

        verify(groupService, times(1)).update(Mockito.any(Group.class));
    }

    @Test
    void deleteGroup() throws Exception {
        Group group = newGroupForTest();

        Mockito.when(groupService.delete(group.getId())).thenReturn(1);

        mockMvc.perform(delete("/group/{id}", group.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).delete(Mockito.any(Long.class));
    }

    @SneakyThrows
    private String asJsonString(final Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private Group newGroupForTest(){
        return new Group(CORRECT_ID, "Tester", null, new Person(1L));
    }

    private Task newTaskForTest() {
        return new Task(CORRECT_ID, "TesterGroup", "2021-06-09", null, null, null);
    }

}