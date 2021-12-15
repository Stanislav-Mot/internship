package com.internship.internship.mapper;

import com.internship.internship.model.Progress;
import com.internship.internship.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProgressMapper implements RowMapper<Progress> {

    @Override
    public Progress mapRow(ResultSet rs, int rowNum) throws SQLException {

        Progress progress = new Progress();

        progress.setId(rs.getLong("id"));
        progress.setPercents(rs.getShort("percents"));

        Long taskID = rs.getLong("id_task");
        if (taskID > 0) {
            progress.setTask(new Task(taskID));
        }

        return progress;
    }
}
