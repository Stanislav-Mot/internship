package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.ProgressMapper;
import com.internship.internship.model.Progress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgressRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProgressRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Progress getProgressById(Long id) {
        String sql = "select * from progress p " +
                "left join task t on p.id = t.id_progress where p.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ProgressMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getProgressById method");

            throw new DataNotFoundException(String.format("Progress Id %d is not found", id));
        }
    }

    public List<Progress> getAllProgresses() {
        String sql = "select * from progress";

        return jdbcTemplate.query(sql, new ProgressMapper());
    }


    public Integer addProgress(SqlParameterSource parameters) {
        String sql =
                "insert into progress (id, id_task, percents) " +
                        "values (:id, :id_task, :percents);" +
                        "update task set id_progress = :id where id = :id_task";


        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updateProgresses(SqlParameterSource parameters) {
        String sql = "update progress set percents = :percents where id = :id";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deleteProgress(Long id) {
        String sql = "update task set id_progress = null where id_progress = ?;" + "delete from progress where id = ?";

        return jdbcTemplate.update(sql, id, id);
    }
}
