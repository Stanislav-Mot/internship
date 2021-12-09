package com.internship.internship.repository;

import com.internship.internship.model.Person;
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

    public Person getById(Long id) {
        return null;
    }

    public List<Person> getAll() {
        return null;
    }

    public Person add(Progress progress) {
        return null;
    }

    public Person update(Progress progress) {
        return null;
    }

    public Person delete(Long id) {
        return null;
    }
}
