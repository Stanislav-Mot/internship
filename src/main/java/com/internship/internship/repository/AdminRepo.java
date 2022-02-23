package com.internship.internship.repository;

import com.internship.internship.mapper.PersonMapper;
import com.internship.internship.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AdminRepo {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AdminRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Person> getAllPerson() {
        String sql = "SELECT * FROM person";
        return jdbcTemplate.query(sql, new PersonMapper());
    }

    public void resetProgress(Long clientId) {
        String sql = "UPDATE task SET progress = 0 where id in " +
                "(Select t.id from task t JOIN person_group pg ON t.id_group = pg.id_group where pg.id_person = ?)";
        jdbcTemplate.update(sql, clientId);
    }

    public void clearTasks(SqlParameterSource sqlParameterSource) {
        String sql = "DELETE from task where " +
                "id in (Select t.id from task t JOIN person_group pg ON t.id_group = pg.id_group where pg.id_person = :clientId) " +
                "and (:taskProgress::INT8 IS NULL OR task.progress = :taskProgress)";
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
