package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.internship.internship.util.Helper.newPersonDtoForTest;
import static com.internship.internship.util.Helper.newTaskDtoForTest;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
@ExtendWith(SpringExtension.class)
class SearchControllerTest {

    @MockBean
    private PersonService personService;
    @MockBean
    private TaskService taskService;
    @Autowired
    private SearchController searchController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(searchController);
    }

    @Test
    void getPersonsByParameters() throws Exception {
        PersonDto personDto = newPersonDtoForTest();

        List<PersonDto> list = Collections.singletonList(personDto);

        Mockito.when(personService.search(anyString(), anyString(), anyInt(), anyInt(), anyInt())).thenReturn(list);

        mockMvc.perform(get("/search/person")
                .param("firstName", "1")
                .param("lastName", "1")
                .param("exactAge", "1")
                .param("rangeAgeStart", "1")
                .param("rangeAgeEnd", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        verify(personService, times(1)).search(anyString(), anyString(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void getTasksByParameters() throws Exception {
        TaskDto taskDto = newTaskDtoForTest();

        List<TaskDto> list = Collections.singletonList(taskDto);

        Mockito.when(taskService.search(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(list);

        mockMvc.perform(get("/search/task")
                .param("name", "1")
                .param("fromProgress", "1")
                .param("toProgress", "1")
                .param("minStartTime", "1")
                .param("maxStartTime", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", Matchers.contains("Tester")));

        verify(taskService, times(1)).search(anyString(), anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    void searchPersonByTokenInName() throws Exception {
        String token = "ster";
        PersonDto personDto = newPersonDtoForTest();

        List<PersonDto> list = Collections.singletonList(personDto);

        Mockito.when(personService.searchByTokenInName(token)).thenReturn(list);

        mockMvc.perform(get("/search/personByToken")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        verify(personService, times(1)).searchByTokenInName(Mockito.any(String.class));
    }
}