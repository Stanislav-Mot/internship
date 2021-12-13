package com.internship.internship.model.mapper;

import com.internship.internship.model.TasksGroup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TasksGroupMapper implements RowMapper<TasksGroup> {


    @Override
    public TasksGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
        TasksGroup tasksGroup = new TasksGroup();
        tasksGroup.setId(rs.getLong("id"));

        System.out.println(rs.getArray("id_task"));
        System.out.println(rs.getLong("id_task"));
        System.out.println(rs.getString("id_task"));
        System.out.println(rs.getAsciiStream("id_task"));

        return tasksGroup;
    }
}
