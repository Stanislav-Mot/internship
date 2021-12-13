package com.internship.internship.repository;

import com.internship.internship.model.Person;
import com.internship.internship.model.mapper.PersonMapper;
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
        String sql = "update groups set id_person = null where id_person = ?; delete from persons where id = ?;";

        return jdbcTemplate.update(sql,id,id);
    }

    public Integer add(Person person) {
        String sql = "insert into persons (id, id_groups, firstname, lastname, age) values (?,?,?,?,?);" +
            "update groups set id_person = ? where id = ?";

        Long groupID = (person.getGroupTasks() != null) ? person.getGroupTasks().getId() : null;

        return jdbcTemplate.update(
            sql,
            person.getId(),
            groupID,
            person.getFirstname(),
            person.getLastname(),
            person.getAge(),
            person.getId(),
            groupID);
    }

    public Integer update(Person person) {

        String sql = "update groups set id_person = null where id_person = ?; " +
            "update groups set id_person = ? where id = ?; " +
            "update persons set id_groups = ?, firstname = ?, lastname = ?, age = ? where id = ?;";

        Long groupID = (person.getGroupTasks() != null) ? person.getGroupTasks().getId() : null;

        return jdbcTemplate.update(sql,
            person.getId(),
            person.getId(),
            groupID,
            groupID,
            person.getFirstname(),
            person.getLastname(),
            person.getAge(),
            person.getId());
     }

    public Person getById(Long id) {
        String sql = "select * from persons p left join groups g on g.id_person = p.id  where p.id = ?";

        return jdbcTemplate.queryForObject(sql, new PersonMapper(), id);
    }

    public List<Person> getAll() {
        String sql = "select * from persons p left join groups g on g.id_person = p.id";

        return jdbcTemplate.query(sql, new PersonMapper());
    }
}
