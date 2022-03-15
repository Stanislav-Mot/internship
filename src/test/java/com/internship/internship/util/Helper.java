package com.internship.internship.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.writeValueAsString(obj);
    }

    public static Group newGroupForTest() {
        return new Group(/*"Tester", null, null, null*/);
    }

    public static Person newPersonForTest() {
        return new Person(null, "Tester", "Rochester", LocalDate.of(2012, 12, 12), null, null);
    }

    public static Task newTaskForTest() {
        return new Task(/*"Tester", null, null, null, null, null, null*/);
    }

    public static GroupDto newGroupDtoForTest() {
        return new GroupDto(CORRECT_ID, "Tester", null, null);
    }

    public static PersonDto newPersonDtoForTest() {
        return new PersonDto(null, "Tester", "Rochester", LocalDate.of(2012, 12, 12), null);
    }

    public static TaskDto newTaskDtoForTest() {
        return new TaskDto(CORRECT_ID, "Tester", null, null, null, null, null,  null);
    }
}
