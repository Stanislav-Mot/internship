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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
        String sql = "select * from task t left join person p on p.id = t.id_person where t.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new TaskMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getTaskById method");

            throw new DataNotFoundException(String.format("Task Id %d is not found", id));
        }
    }

    public List<Task> getAllTasks() {
        String sql = "select * from task t left join person p on p.id = t.id_person ";

        return jdbcTemplate.query(sql, new TaskMapper());
    }

    public List<Task> getByGroupId(Long id) {
        String sql = "select * from task t " +
                "left join person p on p.id = t.id_person " +
                "left join person_group pg on t.id = pg.id_task " +
                "where pg.id_group = ?";

        return jdbcTemplate.query(sql, new TaskMapper(), id);
    }

    public List<Task> getByPersonId(Long id) {
        String sql = "select * from task t left join person p on p.id = t.id_person where p.id = ?";

        return jdbcTemplate.query(sql, new TaskMapper(), id);
    }

    public Integer addTask(SqlParameterSource parameters) {
        String sql = "insert into task (id, name, description, estimate, priority) " +
                "values (:id, :name, :description, :estimate, :priority);";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer update(SqlParameterSource parameters) {
        String sql = "update task set name = :name," +
                "start_time = :start_time, id_person = :personId where id = :id";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateProgress(Long id, Integer progress) {
        String sql = "update task set progress = ? where id = :id";
        // need set spent time
        return jdbcTemplate.update(sql, progress, id);
    }

    public Integer deleteTask(Long id) {
        String sql = "delete from task where id = ?;";

        return jdbcTemplate.update(sql, id);
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup = "select * from group_of_tasks got join task t on got.id = t.id_group  where t.id = ?";

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
