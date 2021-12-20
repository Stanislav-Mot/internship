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

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/test/schema-for-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/test/data-for-task-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TaskRepoTest {

    private final Long CORRECT_ID = 999L;
    private final Long ID_FOR_GET = 2L;
    private final Long ID_FOR_UPDATE= 9999L;
    private final Long ID_FOR_DELETE= 8888L;
    private Integer countTasks = 5;

    @Autowired
    TaskRepo taskRepo;

    @Test
    void getTaskById() {
        Task task = taskRepo.getTaskById(ID_FOR_GET);

        Assertions.assertThat(task).returns("cleaning", from(Task::getName));
    }

    @Test
    void getAllTasks() {
        List<Task> tasks = taskRepo.getAllTasks();

        assertEquals(countTasks, tasks.size());
    }

    @Test
    void addTask() {
        MapSqlParameterSource parameters = getMapSqlParameterSource(newTaskForTest());

        taskRepo.addTask(parameters);
        Iterable<Task> tasks = taskRepo.getAllTasks();

        Assertions.assertThat(tasks).extracting(Task::getName).contains("Tester");

        countTasks += 1;
    }

    @Test
    void updateTask() {
        Task taskFroUpdate = new Task(ID_FOR_UPDATE,"updated", "2001-01-01",null,null,null);

        Integer answer = taskRepo.updateTask(getMapSqlParameterSource(taskFroUpdate));

        assertEquals(1, answer);

        Task task = taskRepo.getTaskById(ID_FOR_UPDATE);

        Assertions.assertThat(task).returns("updated", from(Task::getName));
    }

    @Test
    void deleteTask() {
        Integer answer = taskRepo.deleteTask(ID_FOR_DELETE);

        assertEquals(1, answer);

        Assertions.assertThatThrownBy(() ->
            taskRepo.getTaskById(ID_FOR_DELETE)
        ).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void getGroupsById() {
        List<Group> groups = taskRepo.getGroupsById(2L);

        Assertions.assertThat(groups).extracting(Group::getName).contains("cleaning");
    }

    private Task newTaskForTest() {
        Task task = new Task(CORRECT_ID, "Tester", "2021-06-09", null, null, null);
        return task;
    }

    private MapSqlParameterSource getMapSqlParameterSource(Task task) {
        Long personId = (task.getPerson() != null) ? task.getPerson().getId() : null;
        Long progressId = (task.getProgress() != null) ? task.getProgress().getId() : null;
        Date date = (task.getStartTime() != null) ? Date.valueOf(task.getStartTime()) : null;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", task.getId());
        parameters.addValue("name", task.getName());
        parameters.addValue("personId", personId);
        parameters.addValue("progressId", progressId);
        parameters.addValue("date", date
        );
        return parameters;
    }
}