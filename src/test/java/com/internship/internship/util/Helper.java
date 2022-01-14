package com.internship.internship.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.internship.dto.*;
import com.internship.internship.model.*;
import lombok.SneakyThrows;

public class Helper {

    public static final Long CORRECT_ID = 999L;

    @SneakyThrows
    public static String asJsonString(final Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public static Group newGroupForTest() {
        return new Group(CORRECT_ID, "Tester", null, new Person(1L));
    }

    public static Group newGroupForTest(Person person) {
        return new Group(CORRECT_ID, "TesterGroup", null, person);
    }


    public static Person newPersonForTest() {
        return new Person(CORRECT_ID, "Tester", "Rochester", 99, null);
    }

    public static Task newTaskForTest() {
        return new Task(CORRECT_ID, "Tester", "2021-06-09", null, null, null, null, null, null);
    }

    public static GroupDto newGroupDtoForTest() {
        return new GroupDto(CORRECT_ID, "Tester", null, new PersonDto(1L));
    }

    public static GroupDto newGroupDtoForTest(PersonDto person) {
        return new GroupDto(CORRECT_ID, "TesterGroup", null, person);
    }

    public static PersonDto newPersonDtoForTest() {
        return new PersonDto(CORRECT_ID, "Tester", "Rochester", 99, null);
    }

    public static TaskDto newTaskDtoForTest() {
        return new TaskDto(CORRECT_ID, "Tester", "2021-06-09", null, null, null, null, null, null);
    }
}
