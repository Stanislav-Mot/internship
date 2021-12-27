package com.internship.internship.controller;

import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.model.search.SearchTask;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.internship.internship.util.Helper.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    void getPersonsByParameters() throws Exception {
        Person person = newPersonForTest();
        SearchPerson parameters = new SearchPerson(CORRECT_ID, "Tester", null, null, null);
        List<Person> list = Collections.singletonList(person);

        Mockito.when(personService.search(parameters)).thenReturn(list);

        mockMvc.perform(post("/search/person")
                        .content(asJsonString(parameters))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        mockMvc.perform(post("/search/person")
                        .content("Wrong JSON")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));


        verify(personService, times(1)).search(Mockito.any(SearchPerson.class));
    }

    @Test
    void getTasksByParameters() throws Exception {
        Task task = newTaskForTest();
        SearchTask parameters = new SearchTask(CORRECT_ID, "Tester", null, null, null, null);
        List<Task> list = Collections.singletonList(task);

        Mockito.when(taskService.search(parameters)).thenReturn(list);

        mockMvc.perform(post("/search/task")
                        .content(asJsonString(parameters))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", Matchers.contains("Tester")));

        mockMvc.perform(post("/search/person")
                        .content("Wrong JSON")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));

        verify(taskService, times(1)).search(Mockito.any(SearchTask.class));
    }

    @Test
    void searchPersonByTokenInName() throws Exception {
        String token = "ster";
        Person person = newPersonForTest();
        List<Person> list = Collections.singletonList(person);

        Mockito.when(personService.searchByTokenInName(token)).thenReturn(list);

        mockMvc.perform(post("/search/personByToken")
                        .content(token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        verify(personService, times(1)).searchByTokenInName(Mockito.any(String.class));
    }
}