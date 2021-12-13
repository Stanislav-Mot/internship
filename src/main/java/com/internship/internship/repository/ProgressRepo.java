package com.internship.internship.repository;

import com.internship.internship.model.Progress;
import com.internship.internship.model.mapper.ProgressMapper;
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
        String sql = "select * from progresses p left join tasks t on p.id = t.id_progress where p.id = ?";

        return jdbcTemplate.queryForObject(sql, new ProgressMapper(), id);
    }

    public List<Progress> getAll() {
        String sql = "select * from progresses";

        return jdbcTemplate.query(sql, new ProgressMapper());
    }


    public Integer add(Progress progress) {
        String sql =
            "insert into progresses (id, id_task, percents) values (?,?,?);" +
            "update tasks set id_progress = ? where id = ?";

        Long taskID = (progress.getTask() != null) ? progress.getTask().getId() : null;

        return jdbcTemplate.update(
            sql,
            progress.getId(),
            taskID,
            progress.getPercents(),
            progress.getId(),
            taskID
        );
    }

    public Integer update(Progress progress) {
        String sql =
            "update tasks set id_progress= null where id = ?; " +
            "update tasks set id_progress= ? where id = ?; " +
            "update progresses set percents = ?, id_task = ? where id = ?";

        Long taskID = (progress.getTask() != null) ? progress.getTask().getId() : null;

        return jdbcTemplate.update(
            sql,
            taskID,
            progress.getId(),
            taskID,
            progress.getPercents(),
            taskID,
            progress.getId());
    }

    public Integer delete(Long id) {
        String sql =
            "update tasks set id_progress = null where id = ?;" +
            "delete from progresses where id = ?";

        return jdbcTemplate.update(sql,id,id);
    }
}
