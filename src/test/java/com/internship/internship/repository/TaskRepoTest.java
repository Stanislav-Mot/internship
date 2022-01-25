package com.internship.internship.repository;

import com.internship.internship.dto.search.SearchTaskDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.internship.internship.util.Helper.newTaskForTest;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql("/schema-for-test.sql")
@Sql("/data-for-task-test.sql")
class TaskRepoTest {

    private final Long ID_FOR_GET = 1L;
    private final Long ID_FOR_UPDATE = 3L;
    private final Long ID_FOR_DELETE = 4L;
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
        Task taskFroUpdate = new Task(ID_FOR_UPDATE, "updated", null, "123", 2, null, 44, 22);
        Task task = taskRepo.update(getMapSqlParameterSource(taskFroUpdate));

        Assertions.assertThat(task).returns("updated", from(Task::getName));
        Assertions.assertThat(task).returns(2, from(Task::getEstimate));
        Assertions.assertThat(task).returns("123", from(Task::getDescription));
        Assertions.assertThat(task).returns(22, from(Task::getPriority));
    }

    @Test
    void deleteTask() {
        Integer answer = taskRepo.deleteTask(ID_FOR_DELETE);
        assertEquals(1, answer);
        Assertions.assertThatThrownBy(() -> taskRepo.getTaskById(ID_FOR_DELETE)).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void getGroupsById() {
        List<Group> groups = taskRepo.getGroupsById(1L);
        Assertions.assertThat(groups).extracting(Group::getName).contains("testGroup");
    }

    @Test
    void search() {
        Task taskFroSearch = new Task(null, "searching", LocalDateTime.now(), "123", 2, null, 44, 22);
        taskRepo.addTask(getMapSqlParameterSource(taskFroSearch));
        SearchTaskDto searchTaskDto = new SearchTaskDto();
        searchTaskDto.setName("searching");
        List<Task> tasks = taskRepo.search(getMapSqlParameterSource(searchTaskDto));
        assertEquals(1, tasks.size());
    }

    private MapSqlParameterSource getMapSqlParameterSource(Task task) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", task.getId());
        parameters.addValue("name", task.getName());
        parameters.addValue("description", task.getDescription());
        parameters.addValue("estimate", task.getEstimate());
        parameters.addValue("priority", task.getPriority());

        return parameters;
    }

    private MapSqlParameterSource getMapSqlParameterSource(SearchTaskDto searchTaskDto) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("name", searchTaskDto.getName());
        mapSqlParameterSource.addValue("fromProgress", searchTaskDto.getFromProgress());
        mapSqlParameterSource.addValue("toProgress", searchTaskDto.getToProgress());

        LocalDate localDate = null;
        if (searchTaskDto.getMinStartTime() != null) {
            localDate = LocalDate.parse(searchTaskDto.getMinStartTime());
        }
        mapSqlParameterSource.addValue("fromStartTime", localDate);

        localDate = null;
        if (searchTaskDto.getMaxStartTime() != null) {
            localDate = LocalDate.parse(searchTaskDto.getMaxStartTime());
        }
        mapSqlParameterSource.addValue("toStartTime", localDate);

        return mapSqlParameterSource;
    }
}