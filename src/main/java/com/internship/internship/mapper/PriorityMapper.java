package com.internship.internship.mapper;

import com.internship.internship.model.Group;
import com.internship.internship.model.Priority;
import com.internship.internship.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriorityMapper implements RowMapper<Priority> {

    @Override
    public Priority mapRow(ResultSet rs, int rowNum) throws SQLException {

        Priority priority = new Priority();

        priority.setId(rs.getLong("id"));
        priority.setPriority(rs.getInt("priority"));

        long taskID = rs.getLong("id_task"); // почему у тебя здесь  task--->ID<---, а ниже group--->Id<---. делай все единообразно
        if (taskID > 0) {
            priority.setTask(new Task(taskID));
        }

        long groupId = rs.getLong("id_group");
        if (groupId > 0) {
            priority.setGroup(new Group(groupId));
        }
        return priority;
    }
}
