package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchTask;
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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.internship.internship.service.TaskService.getMapSqlParameterSource;
import static com.internship.internship.util.Helper.newTaskForTest;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql("/schema-for-test.sql")
@Sql("/data-for-task-test.sql")
class TaskRepoTest {

    private final Long CORRECT_ID = 999L;
    private final Long ID_FOR_GET = 2L;
    private final Long ID_FOR_UPDATE = 9999L;
    private final Long ID_FOR_DELETE = 8888L;
    private final Long ID_FOR_SEARCH = 2348L;
    @Autowired
    private TaskRepo taskRepo;
    private Integer countTasks = 5;

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
        Task taskFroUpdate = new Task(ID_FOR_UPDATE, "updated", "2001-01-01", null, null, null);

        Integer answer = taskRepo.updateTask(getMapSqlParameterSource(taskFroUpdate));

        assertEquals(1, answer);

        Task task = taskRepo.getTaskById(ID_FOR_UPDATE);

        Assertions.assertThat(task).returns("updated", from(Task::getName));
    }

    @Test
    void deleteTask() {
        Integer answer = taskRepo.deleteTask(ID_FOR_DELETE);

        assertEquals(1, answer);

        Assertions.assertThatThrownBy(() -> taskRepo.getTaskById(ID_FOR_DELETE)).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void getGroupsById() {
        List<Group> groups = taskRepo.getGroupsById(2L);

        Assertions.assertThat(groups).extracting(Group::getName).contains("cleaning");
    }

    @Test
    void search() {
        Task taskFroSearch = new Task(ID_FOR_SEARCH, "searching", "2001-01-01", null, null, null);
        taskRepo.addTask(getMapSqlParameterSource(taskFroSearch));

        List<Task> tasks = taskRepo.search(getMapSqlParameterSource(
                new SearchTask("searching",
                        LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40),
                        LocalDateTime.now())));

        assertEquals(1, tasks.size());
    }
}