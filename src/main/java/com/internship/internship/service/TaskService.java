package com.internship.internship.service;

import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.repository.TaskRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(Task task) {
        Long personId = (task.getPerson() != null) ? task.getPerson().getId() : null;
        Long progressId = (task.getProgress() != null) ? task.getProgress().getId() : null;
        Date date = (task.getStartTime() != null) ? Date.valueOf(task.getStartTime()) : null;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", task.getId());
        parameters.addValue("name", task.getName());
        parameters.addValue("personId", personId);
        parameters.addValue("progressId", progressId);
        parameters.addValue("date", date);
        return parameters;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(SearchTask parameters) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("name", parameters.getName());
        mapSqlParameterSource.addValue("fromProgress", parameters.getFromProgress());
        mapSqlParameterSource.addValue("toProgress", parameters.getToProgress());
        mapSqlParameterSource.addValue("fromStartTime", parameters.getMinStartTime());
        mapSqlParameterSource.addValue("toStartTime", parameters.getMaxStartTime());
        return mapSqlParameterSource;
    }

    public Task getById(Long id) {
        return taskRepo.getTaskById(id);
    }

    public List<Task> getAll() {
        return taskRepo.getAllTasks();
    }

    public Integer add(Task task) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(task);

        return taskRepo.addTask(parameters);
    }

    public Integer update(Task task) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(task);

        return taskRepo.updateTask(parameters);
    }

    public Integer delete(Long id) {
        return taskRepo.deleteTask(id);
    }

    public List<Task> search(SearchTask parameters) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(parameters);

        return taskRepo.search(mapSqlParameterSource);
    }
}
