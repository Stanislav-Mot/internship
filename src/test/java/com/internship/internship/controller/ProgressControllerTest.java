package com.internship.internship.controller;

import com.internship.internship.dto.ProgressDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.ProgressDtoMapper;
import com.internship.internship.model.Progress;
import com.internship.internship.service.ProgressService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProgressController.class)
class ProgressControllerTest {

    public static final Long CORRECT_ID = 999L;
    public static final Long WRONG_ID = 9999L;
    @MockBean
    private ProgressService progressService;
    @Autowired
    private ProgressController progressController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(progressController);
    }

    @Test
    void getProgress() throws Exception {
        ProgressDto progressDto = newProgressDtoForTest();

        Mockito.when(progressService.getById(CORRECT_ID)).thenReturn(progressDto);

        mockMvc.perform(get("/progress/{id}", CORRECT_ID)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(Math.toIntExact(CORRECT_ID))))
                .andExpect(jsonPath("$.percents", Matchers.is(99)));

        Mockito.when(progressService.getById(WRONG_ID)).thenThrow(DataNotFoundException.class).thenReturn(null);

        mockMvc.perform(get("/progress/{id}", WRONG_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

        verify(progressService, times(2)).getById(Mockito.any());

    }

    @Test
    void getAllProgress() throws Exception {
        ProgressDto progressDto = newProgressDtoForTest();

        List<ProgressDto> progresses = Collections.singletonList(progressDto);

        Mockito.when(progressService.getAll()).thenReturn(progresses);

        mockMvc.perform(get("/progress")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..percents", Matchers.contains(99)));
    }

    @Test
    void addProgress() throws Exception {
        Progress progress = newProgressForTest();

        Mockito.when(progressService.add(any(ProgressDto.class))).thenReturn(1);

        mockMvc.perform(post("/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(progress))
                        .characterEncoding("utf-8")
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.is(1)))
                .andReturn();

        verify(progressService, times(1)).add(Mockito.any(ProgressDto.class));

    }

    @Test
    void updateProgress() throws Exception {
        Progress progress = newProgressForTest();

        when(progressService.update(any(ProgressDto.class))).thenReturn(1);

        mockMvc.perform(put("/progress")
                        .content(asJsonString(progress))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", Matchers.is(1)));

        mockMvc.perform(put("/progress")
                        .content("Wrong JSON")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));

        verify(progressService, times(1)).update(Mockito.any(ProgressDto.class));
    }

    @Test
    void deleteProgress() throws Exception {
        Progress progress = newProgressForTest();

        Mockito.when(progressService.delete(progress.getId())).thenReturn(1);

        mockMvc.perform(delete("/progress/{id}", progress.getId()))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", Matchers.is(1)));

        verify(progressService, times(1)).delete(Mockito.any(Long.class));
    }
}