package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.service.GroupService;
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

import java.util.Collections;
import java.util.List;

import static com.internship.internship.util.Helper.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GroupController.class)
class GroupControllerTest {

    public static final Long CORRECT_ID = 999L;
    public static final Long WRONG_ID = 9999L;
    @MockBean
    private GroupService groupService;
    @Autowired
    private GroupController groupController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(groupController);
    }

    @Test
    void getGroup() throws Exception {
        GroupDto groupDto = newGroupDtoForTest();

        Mockito.when(groupService.getById(CORRECT_ID)).thenReturn(groupDto);

        mockMvc.perform(get("/group/{id}", CORRECT_ID))
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
        GroupDto groupDto = newGroupDtoForTest();

        List<GroupDto> groups = Collections.singletonList(groupDto);

        Mockito.when(groupService.getAll()).thenReturn(groups);

        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", Matchers.contains("Tester")));
    }

    @Test
    void addGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();

//        Mockito.when(groupService.add(any(GroupDto.class))).thenReturn(1);

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(group))
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).add(Mockito.any(GroupDto.class));
    }

    @Test
    void updateGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();

//        when(groupService.update(any(GroupDto.class))).thenReturn(1);

        mockMvc.perform(put("/group")
                        .content(asJsonString(group))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        mockMvc.perform(put("/group")
                        .content("Wrong JSON")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));

        verify(groupService, times(1)).update(Mockito.any(GroupDto.class));
    }

    @Test
    void deleteGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();

        Mockito.when(groupService.delete(group.getId())).thenReturn(1);

        mockMvc.perform(delete("/group/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).delete(Mockito.any(Long.class));
    }

    @Test
    void addGroupToGroup() throws Exception {
        GroupDto group = new GroupDto(1L);
        GroupDto groupIn = new GroupDto(2L);

//        Mockito.when(groupService.addGroup(group.getId(), groupIn.getId())).thenReturn(1);

        mockMvc.perform(put("/group/{id}/addGroup/{groupID}", group.getId(), groupIn.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).addGroup(group.getId(), groupIn.getId());
    }

    @Test
    void deleteGroupFromGroup() throws Exception {
        GroupDto group = new GroupDto(1L);
        GroupDto groupIn = new GroupDto(2L);

//        Mockito.when(groupService.deleteGroup(group.getId(), groupIn.getId())).thenReturn(1);

        mockMvc.perform(put("/group/{id}/deleteGroup/{groupId}", group.getId(), groupIn.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).deleteGroup(group.getId(), groupIn.getId());
    }

    @Test
    void addTaskToGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();
        TaskDto task = newTaskDtoForTest();

//        Mockito.when(groupService.addTask(anyLong(), anyLong())).thenReturn(1);

        mockMvc.perform(put("/group/{id}/addTask/{taskId}", group.getId(), task.getId())
                        .content(asJsonString(group))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1))
                .addTask(anyLong(), anyLong());
    }

    @Test
    void deleteTaskFromGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();
        TaskDto task = newTaskDtoForTest();

//        Mockito.when(groupService.deleteTask(group.getId(), task.getId())).thenReturn(1);

        mockMvc.perform(put("/group/{id}/deleteTask/{taskId}", group.getId(), task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(groupService, times(1)).deleteTask(group.getId(), task.getId());
    }
}