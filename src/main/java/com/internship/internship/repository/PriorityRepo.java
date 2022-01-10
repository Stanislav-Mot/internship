package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.PriorityMapper;
import com.internship.internship.model.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class PriorityRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriorityRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PriorityRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Integer deletePriority(Long id) {
        String sql = "delete from priority_of_task where id = ?";

        return jdbcTemplate.update(sql, id);
    }

    public Integer updatePriority(MapSqlParameterSource parameters) {
        String sql = "update priority_of_task set priority = :priority where id = :id_group";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer addPriority(MapSqlParameterSource parameters) {
        String sql =
                "insert into priority_of_task (id_group, id_task, priority) " +
                        "values (:id_group, :id_task, :priority);";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Priority getPriorityById(Long id) {
        String sql = "select * from priority_of_task p where p.id_group = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PriorityMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getPriorityById method");

            throw new DataNotFoundException(String.format("Priority Id %d is not found", id));
        }
    }

    public List<Priority> getAllPriorityByGroupId(Long id) {
        String sql = "select * from priority_of_task p where p.id_group = ?";

        return jdbcTemplate.query(sql, new PriorityMapper());
    }

    public List<Priority> getAllPriority() {
        String sql = "select * from priority_of_task";

        return jdbcTemplate.query(sql, new PriorityMapper());
    }
}
