package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.mapper.GroupMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    JdbcTemplate jdbcTemplate;

    public TaskService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Group> getGroupsById(Long id) {
        String sqlForGroup =
            "select * from tasks t join tasks_groups tg on t.id = tg.id_task " +
                "join groups g on tg.id_group = g.id where t.id = ?";

        List<Group> groupList = jdbcTemplate.query(sqlForGroup, new GroupMapper(), id);

        return groupList;
    }
}
