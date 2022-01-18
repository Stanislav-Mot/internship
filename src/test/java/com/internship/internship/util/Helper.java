package com.internship.internship.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import lombok.SneakyThrows;

import java.time.LocalDate;

public class Helper {

    public static final Long CORRECT_ID = 999L;

    @SneakyThrows
    public static String asJsonString(final Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public static Group newGroupForTest() {
        return new Group(CORRECT_ID, "Tester", null, null);
    }

    public static Group newGroupForTest(Person person) {
        return new Group(CORRECT_ID, "TesterGroup", null, null);
    }


    public static Person newPersonForTest() {
        return new Person(CORRECT_ID, "Tester", "Rochester", LocalDate.of(2012, 12, 12), null);
    }

    public static Task newTaskForTest() {
        return new Task(CORRECT_ID, "Tester", null, null, null, null, null, null);
    }

    public static GroupDto newGroupDtoForTest() {
        return new GroupDto(CORRECT_ID, "Tester", null, null);
    }

    public static GroupDto newGroupDtoForTest(PersonDto person) {
        return new GroupDto(CORRECT_ID, "TesterGroup", null, null);
    }

    public static PersonDto newPersonDtoForTest() {
        return new PersonDto(CORRECT_ID, "Tester", "Rochester", LocalDate.of(2012, 12, 12), null);
    }

    public static TaskDto newTaskDtoForTest() {
        return new TaskDto(CORRECT_ID, "Tester", null, null, null, null, null, null);
    }
}
