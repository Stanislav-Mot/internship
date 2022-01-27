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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        String sql = "SELECT * FROM task WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new TaskMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getTaskById method");
            throw new DataNotFoundException(String.format("Task Id %d is not found", id));
        }
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM task";
        return jdbcTemplate.query(sql, new TaskMapper());
    }

    public List<Task> getByGroupId(Long id) {
        String sql = "SELECT * FROM task t WHERE id_group = ?";
        return jdbcTemplate.query(sql, new TaskMapper(), id);
    }

    public List<Task> getByPersonId(Long id) {
        String sql = "SELECT * FROM task t JOIN person_group pg ON t.id_group = pg.id_group WHERE pg.id_person = ?";
        return jdbcTemplate.query(sql, new TaskMapper(), id);
    }

    public KeyHolder addTask(SqlParameterSource parameters) {
        String sql = "INSERT INTO task (name, description, estimate, priority, progress) " +
                "VALUES (:name, :description, :estimate, :priority, 0);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
        return keyHolder;
    }

    public Task update(SqlParameterSource parameters) {
        String sql = "UPDATE task SET name = :name, description = :description, " +
                "estimate = :estimate, priority = :priority WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
        return getTaskById((Long) parameters.getValue("id"));
    }

    public Task updateProgress(Long id, Integer progress) {
        String sql = "UPDATE task SET  progress = ? WHERE id = ? AND start_time IS NOT NULL";
        jdbcTemplate.update(sql, progress, id);
        setSpentTime(id);
        return getTaskById(id);
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup = "SELECT * FROM group_of_tasks got JOIN task t ON got.id = t.id_group  WHERE t.id = ?";
        return jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);
    }

    public List<Task> search(MapSqlParameterSource mapSqlParameterSource) {
        String sql =
                "SELECT * FROM task WHERE (cast(:name AS VARCHAR) IS NULL OR task.name = :name) " +

                        "AND (cast(:fromStartTime AS TIMESTAMP(0)) IS NULL AND cast(:toStartTime AS TIMESTAMP(0)) IS NULL " +
                        "OR task.start_time BETWEEN :fromStartTime::TIMESTAMP and :toStartTime::TIMESTAMP) " +

                        "AND (cast(:fromProgress AS INT8) IS NULL AND cast(:toProgress AS INT8) IS NULL " +
                        "OR progress BETWEEN :fromProgress AND :toProgress);";
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new TaskMapper());
    }

    public Integer setStartTime(Long taskId) {
        String sql = "UPDATE task SET start_time = NOW()::timestamp(0) WHERE id = ?";
        return jdbcTemplate.update(sql, taskId);
    }

    public Integer setSpentTime(Long id) {
        String sql = "UPDATE task SET spent_time = EXTRACT(MINUTES FROM NOW() - start_time) WHERE id = ?";
        return jdbcTemplate.update(sql, id);

    }
}
