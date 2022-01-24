package com.internship.internship.mapper;

import com.internship.internship.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {

        Person person = new Person();

        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("firstname"));
        person.setLastName(rs.getString("lastname"));

        Date birthdate = rs.getDate("birthdate");
        if (birthdate != null) {
            person.setBirthdate(birthdate.toLocalDate());
        }
        return person;
    }
}
