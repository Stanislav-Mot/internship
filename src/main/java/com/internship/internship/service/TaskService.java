package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
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

    public Task getById(Long id) {
        Task task = taskRepo.getTaskById(id);

        task.setGroupsList(taskRepo.getGroupsById(id));

        return task;
    }

    public List<Task> getAll() {
        List<Task> taskList = taskRepo.getAllTasks();

        for (Task task : taskList) {
            task.setGroupsList(taskRepo.getGroupsById(task.getId()));
        }

        return taskList;
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

    public Integer addGroup(Long id, Group group) {
        return taskRepo.addGroupToTask(id, group);
    }

    public Integer deleteGroup(Long id, Long groupId) {
        return taskRepo.deleteGroupFromTask(id, groupId);
    }

    private MapSqlParameterSource getMapSqlParameterSource(Task task) {
        Long personId = (task.getPerson() != null) ? task.getPerson().getId() : null;
        Long progressId = (task.getProgress() != null) ? task.getProgress().getId() : null;
        Date date = (task.getStartTime() != null) ? Date.valueOf(task.getStartTime()) : null;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", task.getId());
        parameters.addValue("name", task.getName());
        parameters.addValue("personId", personId);
        parameters.addValue("progressId", progressId);
        parameters.addValue("date", date
        );
        return parameters;
    }
}
