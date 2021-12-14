package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.mapper.GroupMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final JdbcTemplate jdbcTemplate;

    public PersonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup =
            "select * from persons p join groups g on p.id = g.id_person where p.id = ?";

        List<Group> groupList = jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);

        return groupList;
    }
}
