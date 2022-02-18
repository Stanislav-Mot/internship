package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.UserMapper;
import com.internship.internship.model.Role;
import com.internship.internship.model.User;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        String sql = "INSERT INTO person (email, password) VALUES (:email, :password)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
        return keyHolder;
    }

    public User updatePassword(SqlParameterSource parameters) {
        String sql = "UPDATE person SET password = :password WHERE email = :email;";
        namedParameterJdbcTemplate.update(sql, parameters);
        return getUserByEmail((String) parameters.getValue("email"));
    }

    public User getUserById(Long id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserMapper(), id);
            user.setRoles(getRoles(user.getId()));
            return user;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getUserById method");
            throw new DataNotFoundException(String.format("User Id %d is not found", id));
        }
    }

    private Set<Role> getRoles(Long id) {
        String sql = "SELECT role FROM user_role where user_id = ?";
        Map<String, Object> maps = jdbcTemplate.queryForMap(sql, id);

        return maps.values().stream().map(
                s -> Enum.valueOf(Role.class, s.toString())
        ).collect(Collectors.toSet());
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM person WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserMapper(), email);
            user.setRoles(getRoles(user.getId()));
            return user;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM person";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        return users;
    }

    public User updateRole(MapSqlParameterSource parameters) {
        String sql = "INSERT INTO user_role (user_id, role) VALUES ((Select id from person where email = :email),:roles)";
        namedParameterJdbcTemplate.update(sql, parameters);
        return getUserByEmail((String) parameters.getValue("email"));
    }
}
