package com.internship.internship.mapper;

import com.internship.internship.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();

        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        task.setDescription(rs.getString("description"));
        task.setEstimate(rs.getInt("estimate"));
        task.setProgress(rs.getInt("progress"));
        task.setPriority(rs.getInt("priority"));
        task.setSpentTime(rs.getInt("spent_time"));

        Timestamp timestamp = rs.getTimestamp("start_time");
        if (timestamp != null) {
            task.setStartTime(timestamp.toLocalDateTime());
        }

        return task;
    }
}
