package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.model.mapper.GroupMapper;
import com.internship.internship.model.mapper.TaskMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    JdbcTemplate jdbcTemplate;

    public GroupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> getTasksById(Long id) {

        String sqlForGroup =
            "select * from groups g join tasks_groups tg on g.id = tg.id_group " +
                "join tasks t on tg.id_task = t.id where g.id = ?";

        List<Task> tasksList = jdbcTemplate.query(sqlForGroup, new TaskMapper(), id);

        return tasksList;
    }
}
