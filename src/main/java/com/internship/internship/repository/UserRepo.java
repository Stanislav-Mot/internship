package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.UserMapper;
import com.internship.internship.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class UserRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public KeyHolder addUser(SqlParameterSource parameters) {
        String sql = "INSERT INTO users (email, password) VALUES (:email, :password)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
        return keyHolder;
    }

    public User updatePerson(SqlParameterSource parameters) {
        String sql = "UPDATE users SET password = :password WHERE id = :id;";
        namedParameterJdbcTemplate.update(sql, parameters);
        return getUserById((Long) parameters.getValue("id"));
    }

    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserMapper(), id);
            return user;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getUserById method");
            throw new DataNotFoundException(String.format("User Id %d is not found", id));
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserMapper(), email);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        return users;
    }
}
