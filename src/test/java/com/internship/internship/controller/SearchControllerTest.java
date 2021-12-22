package com.internship.internship.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Person;
import com.internship.internship.service.PersonService;
import com.internship.internship.service.TaskService;
import io.swagger.v3.core.util.Json;
import org.checkerframework.checker.units.qual.A;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
@ExtendWith(SpringExtension.class)
class SearchControllerTest {

    public static final Long CORRECT_ID = 999L;
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
    void getPersonByParameters() throws Exception {
        Person person = newPersonForTest();
        String correctJson = "{\"id\":5,\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\",\"age\":23,\"groups\":null}";
        String incorrectJson = "incorrect";

        Mockito.when(personService.search(correctJson)).thenReturn(person);

        mockMvc.perform(post("/search/person")
                .content(correctJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Matchers.contains(Math.toIntExact(CORRECT_ID))))
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        Mockito.when(personService.search(incorrectJson))
                .thenThrow(JsonParseException.class).thenReturn(null);

        mockMvc.perform(post("/search/person")
                .content(incorrectJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

        verify(personService, times(2)).search(Mockito.any(String.class));
    }

    @Test
    void getTaskByParameters() {
    }

    private Person newPersonForTest() {
        return new Person(CORRECT_ID, "Tester", "Rochester", 99, null);
    }
}