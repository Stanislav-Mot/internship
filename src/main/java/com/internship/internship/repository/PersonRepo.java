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
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepo {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepo.class);

    public PersonRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Integer addPerson(SqlParameterSource parameters) {
        String sql = "insert into persons (id, firstname, lastname, age) " +
            "values (:id, :firstname, :lastname, :age);";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updatePerson(SqlParameterSource parameters) {
        String sql = "update persons set firstname = :firstname," +
            " lastname = :lastname, age = :age where id = :id;";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deletePerson(Long id) {
        String deletePerson = "delete from persons where id = ?;";
        String deleteConstrains = "update groups set id_person = null where id_person = ?;";

        jdbcTemplate.update(deleteConstrains, id);

        return jdbcTemplate.update(deletePerson, id);
    }

    public Person getPersonById(Long id) {
        String sql = "select * from persons p where p.id = ?";
        Person person;
        try {
            person = jdbcTemplate.queryForObject(sql, new PersonMapper(), id);
        }
        catch (EmptyResultDataAccessException exception) {
            LOGGER.debug("handling 404 error on getPersonById method");

            throw new DataNotFoundException(String.format("Person Id %d is not found", id));
        }
        return person;
    }

    public List<Person> getAllPersons() {
        String sql = "select * from persons p left join groups g on g.id_person = p.id";

        return jdbcTemplate.query(sql, new PersonMapper());
    }

    public Integer addGroupToPerson(Long id, Group group) {
        String sql = "insert into groups (id_person, id) values (?,?) ";
        return jdbcTemplate.update(sql, id, group.getId());

    }

    public Integer deleteGroupFromPerson(Long id, Long groupId) {
        String sql = "delete from groups where id_person = ? and id = ?;";
        return jdbcTemplate.update(sql, id, groupId);

    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup =
            "select * from persons p join groups g on p.id = g.id_person where p.id = ?";

        return jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);
    }
}
