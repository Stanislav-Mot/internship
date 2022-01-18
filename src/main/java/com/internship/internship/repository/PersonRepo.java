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

    public Integer addPerson(SqlParameterSource parameters, KeyHolder keyHolder) {
        String sql = "insert into person (firstname, lastname, birthdate) values (:firstname, :lastname, :birthdate);";

        return namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
    }

    public Integer updatePerson(SqlParameterSource parameters) {
        String sql = "update person set firstname = :firstname, lastname = :lastname where id = :id;";

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public Integer deletePerson(Long id) {
        String deletePerson = "delete from person where id = ?;";
        String deleteConstrains = "update group_of_tasks set id_person = null where id_person = ?;";

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
            person.setGroups(new ArrayList<>(getGroupsById(person.getId())));
        }
        return persons;
    }

    public Integer addGroupToPerson(Long personId, Long groupId) {
        String sql = "insert  into person_group (id_person, id_group) values (?, ?)";

        return jdbcTemplate.update(sql, personId, groupId);
    }

    public Integer deleteGroupFromPerson(Long personId, Long groupId) {
        String sql = "delete from person_group where id_person = ? and id_group = ?";

        return jdbcTemplate.update(sql, personId, groupId);
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup = "select * from  group_of_tasks got left join person_group pg " +
                "on got.id = pg.id_group where pg.id_person = ?";

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
