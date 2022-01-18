package com.internship.internship.mapper;

import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;


public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();

        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        task.setDescription(rs.getString("description"));
        task.setEstimate(rs.getInt("estimate"));;
        task.setProgress(rs.getInt("progress"));
        task.setPriority(rs.getInt("priority"));
        task.setSpentTime(rs.getInt("spent_time"));

        task.setStartTime(convertToLocalDateTime(rs.getDate("start_time")));

        long personID = rs.getLong("id_person");
        if (personID > 0) {
            task.setPerson(new Person(personID));
        }

        return task;
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }
}
