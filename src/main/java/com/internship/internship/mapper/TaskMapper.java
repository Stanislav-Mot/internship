package com.internship.internship.mapper;

import com.internship.internship.model.Person;
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
        task.setEstimate(rs.getInt("estimate"));;
        task.setProgress(rs.getShort("progress"));

        Time spent_time = rs.getTime("spent_time");
        if (spent_time != null) {
            task.setSpentTime(spent_time.toLocalTime());
        }

        long personID = rs.getLong("id_person");
        if (personID > 0) {
            task.setPerson(new Person(personID));
        }

        return task;
    }
}
