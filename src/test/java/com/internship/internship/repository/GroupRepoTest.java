package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
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

import static com.internship.internship.util.Helper.newGroupForTest;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql("/schema-for-test.sql")
@Sql("/data-for-group-test.sql")
class GroupRepoTest {

    private final Long ID_FOR_GET = 1L;
    private final Long ID_FOR_UPDATE = 3L;
    private final Long ID_FOR_DELETE = 4L;
    @Autowired
    private GroupRepo groupRepo;
    private Integer countGroups = 3;

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

        Assertions.assertThatThrownBy(() -> groupRepo.getGroupById(ID_FOR_DELETE)).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void addTaskToGroup() {
        Group group = groupRepo.getGroupById(ID_FOR_GET);
        Task task = new Task(9999L);

        Integer answer = groupRepo.addTaskToGroup(group.getId(), task.getId());
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


    private MapSqlParameterSource getMapSqlParameterSource(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", group.getId());
        parameters.addValue("name", group.getName());
        return parameters;
    }


    @Test
    void addGroupToGroup() {
        Group groupIn = new Group(4L);

        Integer answer = groupRepo.addGroupToGroup(1L, 4L);

        assertEquals(1, answer);

        Group group = groupRepo.getGroupById(1L);

        assertEquals(4, group.getTasks().size());
    }

    @Test
    void deleteGroupFromGroup() {
        Integer answer = groupRepo.deleteGroupFromGroup(1L, 3L);

        assertEquals(1, answer);

        Group group = groupRepo.getGroupById(1L);

        assertEquals(2, group.getTasks().size());
    }
}