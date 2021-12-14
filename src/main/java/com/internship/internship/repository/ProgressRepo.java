package com.internship.internship.repository;

import com.internship.internship.mapper.ProgressMapper;
import com.internship.internship.model.Progress;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgressRepo {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProgressRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Progress getProgressById(Long id) {
        String sql = "select * from progresses p " +
            "left join tasks t on p.id = t.id_progress where p.id = ?";

        return jdbcTemplate.queryForObject(sql, new ProgressMapper(), id);
    }

    public List<Progress> getAllProgresses() {
        String sql = "select * from progresses";

        return jdbcTemplate.query(sql, new ProgressMapper());
    }


    public Integer addProgress(SqlParameterSource parameters) {
        String sql = "insert into progresses (id, id_task, percents) " +
                "values (:id, :taskId, :percents);" +
                "update tasks set id_progress = :id where id = :taskId";


        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateProgresses(SqlParameterSource parameters) {
        String sql = "update tasks set id_progress= :id where id = taskId; " +
                "update progresses set percents = :percents, " +
            "id_task = :taskId where id = :id";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deleteProgress(Long id) {
        String sql = "update tasks set id_progress = null where id = ?;" +
                "delete from progresses where id = ?";

        return jdbcTemplate.update(sql, id, id);
    }
}
