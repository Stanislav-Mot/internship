package com.internship.internship.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.Progress;
import com.internship.internship.model.Task;
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

    public static Progress newProgressForTest() {
        return new Progress(CORRECT_ID, new Task(9L), (short) 99);
    }

    public static Person newPersonForTest() {
        return new Person(CORRECT_ID, "Tester", "Rochester", 99, null);
    }

    public static Task newTaskForTest() {
        return new Task(CORRECT_ID, "Tester", "2021-06-09", null, null, null);
    }
}
