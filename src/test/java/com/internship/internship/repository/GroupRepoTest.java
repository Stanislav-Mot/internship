package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
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

import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql("/test/schema-for-test.sql") // before_test_method - по дефолту, можно не указывать
@Sql(value = {"/test/data-for-group-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupRepoTest {

    private final Long CORRECT_ID = 999L;
    private final Long ID_FOR_GET = 1L;
    private final Long ID_FOR_UPDATE= 3L;
    private final Long ID_FOR_DELETE= 4L;
    private Integer countGroups = 3;

    @Autowired
    GroupRepo groupRepo;

    @Test
    void getGroupById() {
        Group group = groupRepo.getGroupById(ID_FOR_GET);

        Assertions.assertThat(group).returns("testGroup", from(Group::getName));
    }

    @Test
    void getAll() {
        List<Group> groups = groupRepo.getAll();

        assertEquals(countGroups, groups.size());
    }

    @Test
    void addGroup() {
        MapSqlParameterSource parameters = getMapSqlParameterSource(newGroupForTest());

        groupRepo.addGroup(parameters);
        Iterable<Group> groups = groupRepo.getAll();

        Assertions.assertThat(groups).extracting(Group::getName).contains("Tester");

        countGroups += 1;
    }

    @Test
    void updateGroup() {
        Group groupForUpdate = new Group(ID_FOR_UPDATE, "nameUpdate", null, null);

        Integer answer = groupRepo.updateGroup(groupForUpdate);

        assertEquals(1, answer);

        Group group = groupRepo.getGroupById(ID_FOR_UPDATE);

        Assertions.assertThat(group).returns("nameUpdate", from(Group::getName));
    }

    @Test
    void deleteGroup() {
        Integer answer = groupRepo.deleteGroup(ID_FOR_DELETE);

        assertEquals(1, answer);

        Assertions.assertThatThrownBy(() ->
            groupRepo.getGroupById(ID_FOR_DELETE)
        ).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void addTaskToGroup() {
        Group group = groupRepo.getGroupById(ID_FOR_GET);
        Task task = new Task(9999L);

        Integer answer = groupRepo.addTaskToGroup(group.getId(),task);
        assertEquals(1, answer);


        Iterable<Task> tasks = groupRepo.getTasksById(ID_FOR_GET);

        Assertions.assertThat(tasks).extracting(Task::getName).contains("do_something");
    }

    @Test
    void deleteTaskFromGroup() {

        Integer answer = groupRepo.deleteTaskFromGroup(1L, 8888L);

        assertEquals(1, answer);

        List<Task> tasks = groupRepo.getTasksById(1L);

        Assertions.assertThat(tasks).extracting(Task::getName).isNotIn("for_delete");
    }

    @Test
    void getTasksById() {
        List<Task> tasks = groupRepo.getTasksById(ID_FOR_UPDATE);

        Assertions.assertThat(tasks).extracting(Task::getName).contains("cleaning");
    }

    private Group newGroupForTest(){
        Group group = new Group(CORRECT_ID, "Tester", null, new Person(1L));
        return group;
    }

    private MapSqlParameterSource getMapSqlParameterSource(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", group.getId());
        parameters.addValue("name", group.getName());
        parameters.addValue("id_person", group.getPerson().getId());
        return parameters;
    }
}