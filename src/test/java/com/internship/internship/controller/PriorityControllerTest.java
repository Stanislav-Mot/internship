package com.internship.internship.controller;

import com.internship.internship.dto.PriorityDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Priority;
import com.internship.internship.service.PriorityService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriorityController.class)
class PriorityControllerTest {

    public static final Long CORRECT_ID = 999L;
    public static final Long WRONG_ID = 9999L;
    @MockBean
    private PriorityService priorityService;
    @Autowired
    private PriorityController priorityController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(priorityController);
    }

    @Test
    void getById() throws Exception {
        PriorityDto priorityDto = newPriorityDtoForTest();

        Mockito.when(priorityService.getById(CORRECT_ID)).thenReturn(priorityDto);

        mockMvc.perform(get("/priority/{id}", CORRECT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(Math.toIntExact(CORRECT_ID))))
                .andExpect(jsonPath("$.priority", Matchers.is(34)));

        Mockito.when(priorityService.getById(WRONG_ID)).thenThrow(DataNotFoundException.class).thenReturn(null);

        mockMvc.perform(get("/priority/{id}", WRONG_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

        verify(priorityService, times(2)).getById(Mockito.any());
    }

    @Test
    void getAll() throws Exception {
        PriorityDto priorityDto = newPriorityDtoForTest();

        List<PriorityDto> priorityDtoList = Collections.singletonList(priorityDto);

        Mockito.when(priorityService.getAll()).thenReturn(priorityDtoList);

        mockMvc.perform(get("/priority"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].priority", Matchers.is(34)));
    }

    @Test
    void getByGroupId() throws Exception {
        PriorityDto priorityDto = newPriorityDtoForTest();

        List<PriorityDto> priorityDtoList = Collections.singletonList(priorityDto);

        Mockito.when(priorityService.getByGroupId(priorityDto.getGroup().getId())).thenReturn(priorityDtoList);

        mockMvc.perform(get("/priority/group/{id}", priorityDto.getGroup().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].priority", Matchers.is(34)));
    }

    @Test
    void add() throws Exception {
        PriorityDto priorityDto = newPriorityDtoForTest();

        Mockito.when(priorityService.add(any(PriorityDto.class))).thenReturn(1);

        mockMvc.perform(post("/priority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(priorityDto))
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)))
                .andReturn();

        verify(priorityService, times(1)).add(Mockito.any(PriorityDto.class));
    }

    @Test
    void update() throws Exception {
        Priority priority = newPriorityForTest();

        when(priorityService.update(any(PriorityDto.class))).thenReturn(1);

        mockMvc.perform(put("/priority")
                        .content(asJsonString(priority))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        mockMvc.perform(put("/priority")
                        .content("Wrong JSON")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));

        verify(priorityService, times(1)).update(Mockito.any(PriorityDto.class));
    }

    @Test
    void deletePriority() throws Exception {
        Priority priority = newPriorityForTest();

        Mockito.when(priorityService.delete(priority.getId())).thenReturn(1);

        mockMvc.perform(delete("/priority/{id}", priority.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(priorityService, times(1)).delete(Mockito.any(Long.class));
    }
}