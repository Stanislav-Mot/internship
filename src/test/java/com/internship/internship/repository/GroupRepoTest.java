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
    private Integer countGroups = 4;

    @Test
    void getGroupById() {
        Group group = groupRepo.getGroupById(ID_FOR_GET);

        Assertions.assertThat(group).returns("forGet", from(Group::getName));
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

        Group group = groupRepo.updateGroup(groupForUpdate);

        Assertions.assertThat(group).returns("nameUpdate", from(Group::getName));
    }

    @Test
    void deleteGroup() {

        Group group = groupRepo.getGroupById(ID_FOR_DELETE);

        assertEquals(ID_FOR_DELETE, group.getId());

        groupRepo.deleteGroup(ID_FOR_DELETE);

        Assertions.assertThatThrownBy(() -> groupRepo.getGroupById(ID_FOR_DELETE)).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void addTaskToGroup() {
        groupRepo.addTaskToGroup(ID_FOR_GET, 1L);

        Iterable<Task> tasks = groupRepo.getTasksById(ID_FOR_GET);

        Assertions.assertThat(tasks).extracting(Task::getName).contains("cleaning");
    }

    @Test
    void deleteTaskFromGroup() {

        groupRepo.deleteTaskFromGroup(ID_FOR_GET, ID_FOR_DELETE);

        List<Task> tasks = groupRepo.getTasksById(ID_FOR_GET);

        Assertions.assertThat(tasks).extracting(Task::getName).isNotIn("for_delete");
    }

    @Test
    void getTasksById() {
        List<Task> tasks = groupRepo.getTasksById(ID_FOR_GET);

        Assertions.assertThat(tasks).extracting(Task::getName).contains("cleaning");
    }

    @Test
    void addGroupToGroup() {

        groupRepo.addGroupToGroup(2L, 3L);

        Group group = groupRepo.getGroupById(2L);

        assertEquals(1, group.getTasks().size());
    }

    @Test
    void deleteGroupFromGroup() {
        groupRepo.deleteGroupFromGroup(3L, 1L);

        Group group = groupRepo.getGroupById(3L);

        assertEquals(0, group.getTasks().size());
    }

    private MapSqlParameterSource getMapSqlParameterSource(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", group.getId());
        parameters.addValue("name", group.getName());
        return parameters;
    }
}