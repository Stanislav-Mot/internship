package com.internship.internship.repository;

import com.internship.internship.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class TaskRepo {

    JdbcTemplate jdbcTemplate;

    public TaskRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Task getById(Long id) {
        String sql = "select * from tasks where tasks.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new Task(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("start_time"),
                rs.getLong("id_person"),
                rs.getLong("id_progress"),
                //
                rs.getArray("id_group")
            ), id);
    }

    public List<Task> getAll() {
        String sql = "select * from tasks";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Task(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("start_time"),
                rs.getLong("id_person"),
                rs.getLong("id_progress"),
                rs.getArray("id_group")
            )
        );
    }

    public Integer add(Task task) {
        String sql = "insert into tasks (id, name, start_time, id_person, id_progress) " +
            "values (?,?,?,?,?)";

        return jdbcTemplate.update(
            sql,
            task.getId(),
            task.getName(),
            task.getStartTime(),
            task.getPerson().getId(),
            task.getProgress().getId()
        );
    }

    public Integer update(Task task) {
        String sql = "update tasks set " +
            "name = ?, " +
            "start_time = ?, " +
            "id_person = ?, " +
            "id_progress = ? " +
            "where id = ?";

        return jdbcTemplate.update(sql,
            task.getName(),
            task.getStartTime(),
            task.getPerson().getId(),
            task.getProgress().getId(),
            task.getId());
    }

    public Integer delete(Long id) {
        String sql = "delete from tasks where id = ?";

        return jdbcTemplate.update(sql,id);
    }
}
