package com.internship.internship.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.internship.controller.GroupController;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.service.GroupService;
import org.hamcrest.Matchers;
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

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GroupController.class)
class GroupRepoTest {

//    @MockBean
//    GroupService groupService;
//
//    @Autowired
//    GroupController groupController;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    public static final Long CORRECT_ID = 999L;
//    public static final Long WRONG_ID = 9999L;
//
//    @Test
//    void getAll() throws Exception {
//        Group group = newGroupForTest();
//        List<Group> groups = Arrays.asList(group);
//
//        Mockito.when(groupService.getAll()).thenReturn(groups);
//
//        mockMvc.perform(get("/person")).andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));
//    }
//
//    @Test
//    void getGroupById() {
////        Person person = newPersonForTest();
//
//        Mockito.when(personService.getById(CORRECT_ID)).thenReturn(person);
//
//        mockMvc.perform(get("/person/{id}", CORRECT_ID)).andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$..id", Matchers.contains(Math.toIntExact(CORRECT_ID))))
//            .andExpect(jsonPath("$..firstName", Matchers.contains("Tester")));
//
//        Mockito.when(personService.getById(WRONG_ID)).thenThrow(DataNotFoundException.class).thenReturn(null);
//
//        mockMvc.perform(get("/person/{id}", WRONG_ID))
//            .andExpect(status().isNotFound())
//            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
//
//        verify(personService, times(2)).getById(Mockito.any());
//    }
//
//    @Test
//    void addGroup() {
////        Person person = newPersonForTest();
//
//        Mockito.when(personService.add(any(Person.class))).thenReturn(1);
//
//        mockMvc.perform(post("/person")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(person))
//                .characterEncoding("utf-8")
//            ).andDo(print())
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$", Matchers.is(1)))
//            .andReturn();
//
//        verify(personService, times(1)).add(Mockito.any(Person.class));
//    }
//
//    @Test
//    void updateGroup() {
//        Person person = newPersonForTest();
//
//        when(personService.update(any(Person.class))).thenReturn(1);
//
//        mockMvc.perform(put("/person")
//                .content(asJsonString(person))
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//            .andExpect(status().isAccepted())
//            .andExpect(jsonPath("$", Matchers.is(1)));
//
//        mockMvc.perform(put("/person")
//                .content("Wrong JSON")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.message", containsStringIgnoringCase("wrong JSON format")));
//
//        verify(personService, times(1)).update(Mockito.any(Person.class));
//    }
//
//    @Test
//    void deleteGroup() {
//        Person person = newPersonForTest();
//
//        Mockito.when(personService.delete(person.getId())).thenReturn(1);
//
//        mockMvc.perform(delete("/person/{id}", person.getId()))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", Matchers.is(1)));
//
//        verify(personService, times(1)).delete(Mockito.any(Long.class));
//    }
//
//    @Test
//    void addTaskToGroup() {
//        Person person = newPersonForTest();
//        Group group = newGroupForTest(person);
//        Mockito.when(personService.addGroup(any(Long.class),any(Group.class))).thenReturn(1);
//
//        mockMvc.perform(post("/person/{id}/group", person.getId())
//                .content(asJsonString(group))
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//            ).andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", Matchers.is(1)));
//
//        verify(personService, times(1))
//            .addGroup(Mockito.any(Long.class), Mockito.any(Group.class));
//    }
//
//    @Test
//    void deleteTaskFromGroup() {
//        Person person = newPersonForTest();
//        Group group = newGroupForTest(person);
//
//        Mockito.when(personService.deleteGroup(person.getId(), group.getId())).thenReturn(1);
//
//        mockMvc.perform(delete("/person/{id}/group/{idGroup}", person.getId(), group.getId()))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", Matchers.is(1)));
//
//        verify(personService, times(1)).deleteGroup(person.getId(), group.getId());
//    }
//
//    @Test
//    void getTasksById() {
//    }
//
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static Group newGroupForTest(){
//        Group group = new Group(CORRECT_ID, "Tester", null, new Person(99L));
//        return group;
//    }
}