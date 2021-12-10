package com.internship.internship.repository;

import com.internship.internship.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepo {

    JdbcTemplate jdbcTemplate;

    public PersonRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer delete(Long id) {
        String sql = "delete from persons where id = ?";

        return jdbcTemplate.update(sql,id);
    }

    public Integer add(Person person) {
        String sql = "insert into persons (id, id_groups, firstname, lastname, age) " +
            "values (?,?,?,?,?)";

    return jdbcTemplate.update(
        sql,
        person.getId(),
        //
        person.getGroupTasks().getId(),
        person.getFirstname(),
        person.getLastname(),
        person.getAge());
    }

    public Integer update(Person person) {
        String sql = "update persons set " +
            "id_groups = ?, " +
            "firstname = ?, " +
            "lastname = ?, " +
            "age = ? " +
            "where id = ?";

        return jdbcTemplate.update(sql,
            person.getGroupTasks(),
            person.getFirstname(),
            person.getLastname(),
            person.getAge(),
            // update group
            person.getId());
     }

    public Person getById(Long id) {
        String sql = "select * from persons where persons.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new Person(
                rs.getLong("id"),
                //??
                rs.getLong("id_groups"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getInt("age")
            ), id);
    }

    public List<Person> getAll() {
        String sql = "select * from persons";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Person(
                rs.getLong("id"),
                //?
                rs.getLong("id_groups"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getInt("age")
            ));
    }
}
