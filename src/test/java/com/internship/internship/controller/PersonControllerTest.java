package com.internship.internship.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
//@SpringBootTest
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
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));
    }
    @Test
    void getPerson() {
    }
}