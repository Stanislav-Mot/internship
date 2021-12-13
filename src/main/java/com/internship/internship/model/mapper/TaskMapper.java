package com.internship.internship.model.mapper;

import com.internship.internship.model.Person;
import com.internship.internship.model.Progress;
import com.internship.internship.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();

        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        task.setStartTime(rs.getString("start_time"));

        Long id_person = rs.getLong("id_person");
        if(id_person > 0) {
            task.setPerson(new Person(id_person));
        }

        Long id_progress = rs.getLong("id_progress");
        if(id_progress > 0) {
            task.setProgress(new Progress(id_progress));
        }

        return task;
    }
}
