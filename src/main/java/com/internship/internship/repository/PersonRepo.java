package com.internship.internship.repository;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.mapper.PersonMapper;
import com.internship.internship.service.PersonService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepo {

    JdbcTemplate jdbcTemplate;
    PersonService personService;

    public PersonRepo(JdbcTemplate jdbcTemplate, PersonService personService) {
        this.jdbcTemplate = jdbcTemplate;
        this.personService = personService;
    }

    public Integer delete(Long id) {
        String sql = "update groups set id_person = null where id_person = ?; delete from persons where id = ?;";

        return jdbcTemplate.update(sql,id,id);
    }

    public Integer add(Person person) {
        String sql = "insert into persons (id, firstname, lastname, age) values (?,?,?,?);";

        return jdbcTemplate.update(
            sql,
            person.getId(),
            person.getFirstName(),
            person.getLastName(),
            person.getAge());
    }

    public Integer update(Person person) {
        String sql = "update persons set firstname = ?, lastname = ?, age = ? where id = ?;";

        return jdbcTemplate.update(sql,
            person.getFirstName(),
            person.getLastName(),
            person.getAge(),
            person.getId());
     }

    public Person getById(Long id) {
        String sql = "select * from persons p left join groups g on g.id_person = p.id  where p.id = ?";

        Person person = jdbcTemplate.queryForObject(sql, new PersonMapper(), id);

        person.setGroupTasks(personService.getGroupsById(id));

        return person;
    }

    public List<Person> getAll() {
        String sql = "select * from persons p left join groups g on g.id_person = p.id";

        List<Person> personList = jdbcTemplate.query(sql, new PersonMapper());

        for (Person person: personList) {
            person.setGroupTasks(personService.getGroupsById(person.getId()));
        }

        return personList;
    }

    public Integer addGroup(Long id, Group group) {
        String sql = "insert into groups (id_person, id) values (?,?) ";
        return jdbcTemplate.update(sql,id,group.getId());

    }

    public Integer deleteGroup(Long id, Long idGroup) {
        String sql = "delete from groups where id_person = ? and id= ?";
        return jdbcTemplate.update(sql,id,idGroup);

    }
}
