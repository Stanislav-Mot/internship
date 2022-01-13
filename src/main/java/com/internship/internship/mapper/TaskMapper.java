package com.internship.internship.mapper;

import com.internship.internship.model.Person;
import com.internship.internship.model.Progress;
import com.internship.internship.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;


public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();

        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        task.setStartTime(rs.getString("start_time"));
        task.setDescription(rs.getString("description"));

        Time estimate = rs.getTime("estimate");
        if (estimate != null) {
            task.setEstimate(estimate.toLocalTime());
        }

        Time spent_time = rs.getTime("spent_time"); // снейккейс
        if (spent_time != null) {
            task.setSpentTime(spent_time.toLocalTime());
        }

        long personID = rs.getLong("id_person");
        if (personID > 0) {
            task.setPerson(new Person(personID));
        }

        long progressID = rs.getLong("id_progress");
        if (progressID > 0) {
            task.setProgress(new Progress(progressID));
        }

        return task;
    }
}
