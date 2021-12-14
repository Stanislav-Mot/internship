package com.internship.internship.mapper;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(rs.getLong("id"));
        group.setName(rs.getString("name"));

        Long personID = rs.getLong("id_person");
        if (personID > 0) {
            group.setPerson(new Person(personID));
        }

        return group;
    }
}