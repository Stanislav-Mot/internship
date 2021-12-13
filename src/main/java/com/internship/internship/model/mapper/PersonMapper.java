package com.internship.internship.model.mapper;

import com.internship.internship.model.Person;
import com.internship.internship.model.TasksGroup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {

        Person person = new Person();

        person.setId(rs.getLong("id"));
        person.setAge(rs.getInt("age"));
        person.setFirstname(rs.getString("firstname"));
        person.setLastname(rs.getString("lastname"));

        if(rs.getLong("id_groups") > 0){
            TasksGroup tasksGroup = new TasksGroup();
            tasksGroup.setId(rs.getLong("id_groups"));
            person.setGroupTasks(tasksGroup);
        }

        return person;
    }
}
