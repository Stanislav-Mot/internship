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
import org.springframework.transaction.annotation.Transactional;

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

    public Integer addPerson(SqlParameterSource parameters) {
        String sql = "insert into person (id, firstname, lastname, age) " +
                "values (:id, :firstname, :lastname, :age);";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer updatePerson(SqlParameterSource parameters) {
        String sql = "update person set firstname = :firstname," +
                " lastname = :lastname, age = :age where id = :id;";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deletePerson(Long id) {
        String deletePerson = "delete from person where id = ?;";
        String deleteConstrains = "update groupOfTasks set id_person = null where id_person = ?;";

        jdbcTemplate.update(deleteConstrains, id);

        return jdbcTemplate.update(deletePerson, id);
    }

    public Person getPersonById(Long id) {
        String sql = "select * from person p where p.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PersonMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            LOGGER.warn("handling 404 error on getPersonById method");

            throw new DataNotFoundException(String.format("Person Id %d is not found", id));
        }
    }

    public List<Person> getAllPersons() {
        String sql = "select * from person";
        List<Person> persons = jdbcTemplate.query(sql, new PersonMapper());
        for (Person person : persons) {
            person.setGroups(getGroupsById(person.getId()));
        }
        return persons;
    }

    public Integer addGroupToPerson(Long id, Group group) {
        String sql = "update groupOfTasks set id_person = ? where id = ?;";
        return jdbcTemplate.update(sql, id, group.getId());
    }

    public Integer deleteGroupFromPerson(Long personId, Long groupId) {
        String sql = "update groupOfTasks set id_person = null  where id_person = ? and id = ?;";
        return jdbcTemplate.update(sql, personId, groupId);

    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup =
                "select * from person p join groupOfTasks g on p.id = g.id_person where p.id = ?";

        return jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);
    }

    public List<Person> search(SqlParameterSource sqlParameterSource) {
        String sql =
                "select * from person where (cast(:firstName as VARCHAR) is null or person.firstname = :firstName) " +
                        "and (cast(:lastName as VARCHAR) is null or person.lastname = :lastName) " +
                        "and (cast(:exactAge as SMALLINT) is null or person.age >= cast(:exactAge as SMALLINT)) " +
                        "and (cast(:rangeAge as SMALLINT) is null or person.age <= cast(:rangeAge as SMALLINT)) " +
                        "and (cast(:rangeAge as SMALLINT) is not null or person.age <= cast(:exactAge as SMALLINT)) ";

        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new PersonMapper());
    }


    public List<Person> searchByTokenInName(Map<String, Object> params) {
        String sql = "select * from person where CONCAT(firstname, ' ' , lastname) like :token";
        return namedParameterJdbcTemplate.query(sql, params, new PersonMapper());
    }
}
