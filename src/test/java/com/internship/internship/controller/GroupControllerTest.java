package com.internship.internship.controller;

import com.internship.internship.dto.GroupDto;
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

import static com.internship.internship.util.Helper.asJsonString;
import static com.internship.internship.util.Helper.newGroupDtoForTest;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void getGroupByPersonIdTest() throws Exception {
        GroupDto groupDto = newGroupDtoForTest();
        List<GroupDto> groups = Collections.singletonList(groupDto);

        Mockito.when(groupService.getByPersonId(CORRECT_ID)).thenReturn(groups);

        mockMvc.perform(get("/group/person/{id}", CORRECT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Matchers.contains(Math.toIntExact(CORRECT_ID))))
                .andExpect(jsonPath("$..name", Matchers.contains("Tester")));

        verify(groupService, times(1)).getByPersonId(Mockito.any());
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
        group.setId(null);

        Mockito.when(groupService.add(any(GroupDto.class))).thenReturn(group);

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(group))
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", containsStringIgnoringCase("Tester")));

        verify(groupService, times(1)).add(Mockito.any(GroupDto.class));
    }

    @Test
    void updateGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();

        when(groupService.update(any(GroupDto.class))).thenReturn(group);

        mockMvc.perform(put("/group")
                .content(asJsonString(group))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsStringIgnoringCase("Tester")));

        mockMvc.perform(put("/group")
                .content("Wrong JSON")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", containsStringIgnoringCase("wrong JSON format")));

        verify(groupService, times(1)).update(Mockito.any(GroupDto.class));
    }

    @Test
    void addGroupToGroup() throws Exception {
        GroupDto group = newGroupDtoForTest();

        Mockito.when(groupService.addGroup(anyLong(), anyLong())).thenReturn(group);

        mockMvc.perform(put("/group/{id}/addGroup/{groupID}", anyLong(), anyLong())
                .content(asJsonString(group))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsStringIgnoringCase("Tester")));

        verify(groupService, times(1)).addGroup(anyLong(), anyLong());
    }

    @Test
    void deleteGroupFromGroup() throws Exception {
        doNothing().when(groupService).deleteGroup(anyLong(), anyLong());

        mockMvc.perform(put("/group/{id}/deleteGroup/{groupId}", anyLong(), anyLong()))
                .andExpect(status().isOk());

        verify(groupService, times(1)).deleteGroup(anyLong(), anyLong());
    }

    @Test
    void addTaskToGroup() throws Exception {
        GroupDto groupDto = newGroupDtoForTest();

        Mockito.when(groupService.addTask(anyLong(), anyLong())).thenReturn(groupDto);

        mockMvc.perform(put("/group/{id}/addTask/{taskId}", anyLong(), anyLong())
                .content(asJsonString(groupDto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsStringIgnoringCase("Tester")));

        verify(groupService, times(1))
                .addTask(anyLong(), anyLong());
    }

    @Test
    void deleteTaskFromGroup() throws Exception {
        doNothing().when(groupService).deleteTask(anyLong(), anyLong());

        mockMvc.perform(put("/group/{id}/deleteTask/{taskId}", any(Long.class), any(Long.class)))
                .andExpect(status().isOk());

        verify(groupService, times(1)).deleteTask(any(Long.class), any(Long.class));
    }
}