package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Priority;
import com.internship.internship.model.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.internship.internship.service.PriorityService.getPriorityMapSqlPS;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql("/schema-for-test.sql")
@Sql("/data-for-priority-test.sql")
class PriorityRepoTest {

    private final Long CORRECT_ID = 666L;
    private final Long ID_FOR_GET = 111L;
    private final Long ID_FOR_UPDATE = 222L;
    private final Long ID_FOR_DELETE = 333L;
    private final Long ID_GROUP = 4L;
    private final Long ID_TASK = 4L;
    @Autowired
    private PriorityRepo priorityRepo;
    private Integer countPriorities = 5;

    @Test
    void deletePriority() {
        Integer answer = priorityRepo.deletePriority(ID_FOR_DELETE);

        assertEquals(1, answer);

        Assertions.assertThatThrownBy(() ->
                        priorityRepo.getPriorityById(ID_FOR_DELETE))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void updatePriority() {
        MapSqlParameterSource parameters = getPriorityMapSqlPS(new Priority(ID_FOR_UPDATE, new Group(ID_GROUP), new Task(ID_TASK), 34));

        Integer answer = priorityRepo.updatePriority(parameters);

        assertEquals(1, answer);

        Priority priority = priorityRepo.getPriorityById(ID_FOR_UPDATE);

        Assertions.assertThat(priority).returns(34, from(Priority::getPriority));
    }

    @Test
    void addPriority() {
        MapSqlParameterSource parameters = getPriorityMapSqlPS(new Priority(CORRECT_ID, new Group(ID_GROUP), new Task(66L), 99));

        priorityRepo.addPriority(parameters);
        Iterable<Priority> priorities = priorityRepo.getAllPriority();

        Assertions.assertThat(priorities).extracting(Priority::getPriority).contains(99);

        countPriorities += 1;
    }

    @Test
    void getPriorityById() {
        Priority priority = priorityRepo.getPriorityById(ID_FOR_GET);

        Assertions.assertThat(priority).returns(ID_FOR_GET, from(Priority::getId));
    }

    @Test
    void getAllPriorityByGroupId() {
        List<Priority> priorities = priorityRepo.getAllPriorityByGroupId(ID_GROUP);

        assertEquals(2, priorities.size());
    }

    @Test
    void getAllPriority() {
        List<Priority> priorities = priorityRepo.getAllPriority();

        assertEquals(countPriorities, priorities.size());
    }
}