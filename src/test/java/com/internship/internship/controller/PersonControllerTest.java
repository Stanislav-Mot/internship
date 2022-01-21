package com.internship.internship.controller;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.exeption.DataNotFoundException;
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

import java.util.Collections;
import java.util.List;

import static com.internship.internship.util.Helper.asJsonString;
import static com.internship.internship.util.Helper.newPersonDtoForTest;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    public static final Long CORRECT_ID = 999L;
    public static final Long WRONG_ID = 9999L;
    @MockBean
    private PersonService personService;
    @Autowired
    private PersonController personController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(personController);
    }

    @Test
    void getAllPersons() throws Exception {
        PersonDto personDto = newPersonDtoForTest();

        List<PersonDto> persons = Collections.singletonList(personDto);

        Mockito.when(personService.getAll()).thenReturn(persons);

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));
    }

    @Test
    void getPersonById() throws Exception {
        PersonDto person = newPersonDtoForTest();
        person.setId(CORRECT_ID);

        Mockito.when(personService.getById(CORRECT_ID)).thenReturn(person);

        mockMvc.perform(get("/person/{id}", CORRECT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Matchers.contains(Math.toIntExact(CORRECT_ID))))
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        Mockito.when(personService.getById(WRONG_ID))
                .thenThrow(DataNotFoundException.class).thenReturn(null);

        mockMvc.perform(get("/person/{id}", WRONG_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

        verify(personService, times(2)).getById(Mockito.any());
    }

    @Test
    void addPerson() throws Exception {
        PersonDto person = newPersonDtoForTest();

        Mockito.when(personService.add(any(PersonDto.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                .content(asJsonString(person))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        verify(personService, times(1)).add(Mockito.any(PersonDto.class));
    }

    @Test
    void updatePerson() throws Exception {
        PersonDto person = newPersonDtoForTest();
        person.setId(CORRECT_ID);
        when(personService.update(any(PersonDto.class))).thenReturn(person);

        mockMvc.perform(put("/person")
                .content(asJsonString(person))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        mockMvc.perform(put("/person")
                .content("Wrong JSON")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", containsStringIgnoringCase("wrong JSON format")));

        verify(personService, times(1)).update(Mockito.any(PersonDto.class));
    }

    @Test
    void deletePerson() throws Exception {
        PersonDto person = newPersonDtoForTest();
        person.setId(CORRECT_ID);

        doNothing().when(personService).delete(person.getId());

        mockMvc.perform(delete("/person/{id}", person.getId()))
                .andExpect(status().isOk());

        verify(personService, times(1)).delete(person.getId());
    }

    @Test
    void addGroupToPerson() throws Exception {
        PersonDto person = newPersonDtoForTest();

        Mockito.when(personService.addGroup(anyLong(), anyLong())).thenReturn(person);

        mockMvc.perform(put("/person/{id}/addGroup/{groupId}", anyLong(), anyLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));

        verify(personService, times(1)).addGroup(anyLong(), anyLong());
    }

    @Test
    void deleteGroupFromPerson() throws Exception {
        doNothing().when(personService).deleteGroup(anyLong(), anyLong());

        mockMvc.perform(put("/person/{id}/deleteGroup/{groupId}", anyLong(), anyLong()))
                .andExpect(status().isOk());

        verify(personService, times(1)).deleteGroup(anyLong(), anyLong());
    }
}