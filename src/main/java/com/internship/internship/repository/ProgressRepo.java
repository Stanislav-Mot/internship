package com.internship.internship.repository;

import com.internship.internship.model.Progress;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgressRepo {

    JdbcTemplate jdbcTemplate;

    public ProgressRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Progress getById(Long id) {
        String sql = "select * from progresses where progress.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new Progress(
                rs.getLong("id"),
                rs.getLong("id_task"),
                rs.getShort("percents")
            ), id);
    }

    public List<Progress> getAll() {
        String sql = "select * from progresses";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Progress(
                rs.getLong("id"),
                rs.getLong("id_task"),
                rs.getShort("percents")
            )
        );
    }

    public Integer add(Progress progress) {
        String sql = "insert into progresses (id, id_task, percents) values (?,?,?)";

        return jdbcTemplate.update(
            sql,
            progress.getId(),
            progress.getTask().getId(),
            progress.getPercents()

        );
    }

    public Integer update(Progress progress) {
        String sql = "update progresses set " +
            "id_progress = ? " +
            "where id = ?";

        return jdbcTemplate.update(sql,
            progress.getPercents(),
            progress.getId());
    }

    public Integer delete(Long id) {
        String sql = "delete from progresses where id = ?";

        return jdbcTemplate.update(sql,id);
    }
}
