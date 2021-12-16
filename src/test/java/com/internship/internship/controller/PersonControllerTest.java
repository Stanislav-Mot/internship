package com.internship.internship.controller;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Person;
import com.internship.internship.service.PersonService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @MockBean
    PersonService personService;

    @Autowired
    PersonController personController;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(personController);
    }

    @Test
    void getAllPersons() throws Exception {
        Person person = new Person(999L);
        person.setFirstName("Tester");
        List<Person> persons = Arrays.asList(person);

        Mockito.when(personService.getAll()).thenReturn(persons);

        mockMvc.perform(get("/person")).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

    }
    @Test
    void getPersonById() throws Exception {
        Long personId = 999L;
        long wrongPersonId = 4045L;
        Person person = new Person(personId);

        Mockito.when(personService.getById(personId)).thenReturn(person);

        mockMvc.perform(get("/person/{id}", personId)).andDo(print())
            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$..id", Matchers.contains(Math.toIntExact(personId))));

        mockMvc.perform(get("/person/{id}", wrongPersonId))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
    }

    @Test
    void addPerson() throws Exception {
        Long personId = 999L;
        Person person = new Person(personId);

        Mockito.when(personService.add(person)).thenReturn(1);

        mockMvc.perform(post("/person")
                .content(new ObjectMapper().writeValueAsString(person))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.is(0)));
    }

    @Test
    void addGroupToPerson() {
    }

    @Test
    void deleteGroupFromPerson() {
    }

    @Test
    void updatePerson() throws Exception {
        Person person = new Person(999L);

        personService.add(person);

        person.setFirstName("Tester");
        person.setLastName("Tester_ov");
        person.setAge(15);

        personService.update(person);

        when(personService.update(person)).thenReturn(0);

        verify(personService, times(2));
        mockMvc.perform(put("/person")
                .content(new ObjectMapper().writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.is(1)));

    }

    @Test
    void deletePerson() throws Exception {
        Person person = new Person(999L);

        personService.add(person);
        Mockito.when(personService.delete(person.getId())).thenReturn(1);

        mockMvc.perform(delete("/person/{id}", person.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.is(1)));

    }
}