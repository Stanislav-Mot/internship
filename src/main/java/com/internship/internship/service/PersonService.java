package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.mapper.GroupMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    JdbcTemplate jdbcTemplate;

    public PersonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup =
            "select * from persons p join group g on p.id = g.id_person where g.id = ?";

        List<Group> groupList = jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);

        return groupList;
    }
}
