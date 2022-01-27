package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.PersonMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class PersonRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepo.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PersonRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public KeyHolder addPerson(SqlParameterSource parameters) {
        String sql = "INSERT INTO person (firstname, lastname, birthdate) VALUES (:firstname, :lastname, :birthdate);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
        return keyHolder;
    }

    public Person updatePerson(SqlParameterSource parameters) {
        String sql = "UPDATE person SET firstname = :firstname, lastname = :lastname WHERE id = :id;";
        namedParameterJdbcTemplate.update(sql, parameters);
        return getPersonById((Long) parameters.getValue("id"));
    }

    public Person getPersonById(Long id) {
        String sql = "SELECT * FROM person p WHERE p.id = ?";
        try {
            Person person = jdbcTemplate.queryForObject(sql, new PersonMapper(), id);
            person.setGroups(new ArrayList<>(getGroupsById(person.getId())));
            return person;
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getPersonById method");

            throw new DataNotFoundException(String.format("Person Id %d is not found", id));
        }
    }

    public List<Person> getAllPersons() {
        String sql = "SELECT * FROM person";
        List<Person> persons = jdbcTemplate.query(sql, new PersonMapper());
        persons.forEach(person -> person.setGroups(new ArrayList<>(getGroupsById(person.getId()))));
        return persons;
    }

    public Person addGroupToPerson(Long personId, Long groupId) {
        String sql = "INSERT INTO person_group (id_person, id_group) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, personId, groupId);
        Person person = getPersonById(personId);
        person.setGroups(new ArrayList<>(getGroupsById(person.getId())));
        return person;
    }

    public Integer deleteGroupFromPerson(Long personId, Long groupId) {
        String sql = "DELETE FROM person_group WHERE id_person = ? AND id_group = ?";
        return jdbcTemplate.update(sql, personId, groupId);
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup = "SELECT * FROM  group_of_tasks got LEFT JOIN person_group pg " +
                "ON got.id = pg.id_group WHERE pg.id_person = ?";
        return jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);
    }

    public List<Person> search(SqlParameterSource sqlParameterSource) {
        String sql =
                "SELECT * FROM person WHERE (:firstName::VARCHAR IS NULL OR person.firstname = :firstName) " +
                        "AND (:lastName::VARCHAR IS NULL OR person.lastname = :lastName) " +

                        "AND (:exactAge::INT8 IS NULL OR " +
                        "extract(YEAR FROM NOW()::date) - extract(YEAR FROM person.birthdate) = :exactAge) " +

                        "AND (:rangeAgeStart::INT8 IS NULL AND :rangeAgeEnd::INT8 IS NULL OR " +
                        "extract(YEAR FROM NOW()::date) - extract(YEAR FROM person.birthdate) " +
                        "BETWEEN :rangeAgeStart AND :rangeAgeEnd)";
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new PersonMapper());
    }

    public List<Person> searchByTokenInName(Map<String, Object> params) {
        String sql = "SELECT * FROM person WHERE LOWER(CONCAT(firstname, ' ' , lastname)) LIKE LOWER(:token)";
        return namedParameterJdbcTemplate.query(sql, params, new PersonMapper());
    }
}
