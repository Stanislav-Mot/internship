package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TaskRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Task getTaskById(Long id) {
        String sql = "select * from task t " +
                "left join person p on p.id = t.id_person " +
                "left join progress pr on pr.id = t.id_progress " +
                "where t.id = ?";
        try {
            Task task = jdbcTemplate.queryForObject(sql, new TaskMapper(), id);
            task.setGroupsList(getGroupsById(id));
            return task;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getTaskById method");

            throw new DataNotFoundException(String.format("Task Id %d is not found", id));
        }
    }

    public List<Task> getAllTasks() {
        String sql = "select * from task t " +
                "left join person p on p.id = t.id_person " +
                "left join progress pr on pr.id = t.id_progress";
        List<Task> tasks = jdbcTemplate.query(sql, new TaskMapper());
        for (Task task : tasks) {
            task.setGroupsList(getGroupsById(task.getId()));
        }
        return tasks;
    }

    public Integer addTask(SqlParameterSource parameters) {
        String sql = "insert into task (id, name, start_time, id_person, id_progress) " +
                "values (:id, :name, :date, :personId, :progressId);";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateTask(SqlParameterSource parameters) {
        String sql = "update task set name = :name," +
                "start_time = :date, id_person = :personId, " +
                "id_progress = :progressId where id = :id";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deleteTask(Long id) {
        String sql = "update progress set id_task = null where id_task = ?; " +
                "delete from task_group where id_task = ?; " +
                "delete from task where id = ?;";

        return jdbcTemplate.update(sql, id, id, id);
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup = "select * from task t join task_group tg on t.id = tg.id_task " +
                "join groupOfTasks g on tg.id_group = g.id where t.id = ?";

        return jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);
    }

    public List<Task> search(MapSqlParameterSource mapSqlParameterSource) {
        String sql =
                "select * from task LEFT join progress on task.id = progress.id_task " +
                        "where cast(:name as VARCHAR) is null or task.name = :name " +
                        "and (cast(:fromStartTime as date) is null or cast(:toStartTime as date) is null) " +
                        "or task.start_time BETWEEN :fromStartTime::timestamp and :toStartTime::timestamp " +
                        "and (cast(:fromProgress as SMALLINT) is null or cast(:toProgress as SMALLINT) is null) " +
                        "Or progress.percents BETWEEN :fromProgress and :toProgress;";

        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new TaskMapper());
    }
}
