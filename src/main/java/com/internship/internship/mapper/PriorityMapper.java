package com.internship.internship.mapper;

import com.internship.internship.model.Priority;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriorityMapper implements RowMapper<Priority> {

    @Override
    public Priority mapRow(ResultSet rs, int rowNum) throws SQLException {

        Priority priority = new Priority();

        priority.setId(rs.getLong("id"));
        priority.setTaskId(rs.getLong("taskId"));
        priority.setPriority(rs.getInt("priority"));

        return priority;
    }
}
