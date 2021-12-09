package com.internship.internship.repositories;

import com.internship.internship.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

     public Integer delete(Long id) {
        String sql = String.format("delete from persons where id = ?");

        return jdbcTemplate.update(sql,id);
     }

    public Integer add(Person person) {
         String sql = String.format(
                 "insert into persons " +
                 "(id, id_groups, firstname, lastname, age) values (?,?,?,?,?)");

        return jdbcTemplate.update(
                 sql,
                 person.getId(),
                 person.getGroupTasks().getId(),
                 person.getFirstname(),
                 person.getLastname(),
                 person.getAge());
     }

    public Integer update(Person person) {
        String sql = String.format(
                "update persons set " +
                "id_groups = ?, " +
                "firstname = ?, " +
                "lastname = ?, " +
                "age = ? " +
                "where id = ?");

        return jdbcTemplate.update(sql,
                 person.getGroupTasks(),
                 person.getFirstname(),
                 person.getLastname(),
                 person.getAge(),
                 person.getId());
     }

    public Person getById(Long id) {
         String sql = String.format("select * from persons where persons.id = ?", id);

         return jdbcTemplate.queryForObject(sql, new Object[] {id}, (rs, rowNum) ->
                 new Person(
                    rs.getLong("id"),
                    rs.getLong("id_groups"),
                         rs.getString("firstname"),
                         rs.getString("lastname"),
                         rs.getInt("age")
                 ));
     }

    public List<Person> getAll() {
        String sql = String.format("select * from persons");

         return jdbcTemplate.query(sql, (rs, rowNum) ->
                 new Person(
                         rs.getLong("id"),
                         rs.getLong("id_groups"),
                         rs.getString("firstname"),
                         rs.getString("lastname"),
                         rs.getInt("age")
                 ));
    }
}
