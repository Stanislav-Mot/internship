package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchTask;
import com.internship.internship.repository.TaskRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final TaskDtoMapper mapper;

    public TaskService(TaskRepo taskRepo, TaskDtoMapper mapper) {
        this.taskRepo = taskRepo;
        this.mapper = mapper;
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
        parameters.addValue("start_time", date);
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

    public static MapSqlParameterSource getParameterForUpgradedUpdate(Task task) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("id", task.getId());
        mapSqlParameterSource.addValue("description", task.getDescription());
        mapSqlParameterSource.addValue("estimate", task.getEstimate());
        mapSqlParameterSource.addValue("spent_time", task.getSpentTime());

        return mapSqlParameterSource;
    }

    public TaskDto getById(Long id) {
        Task task = taskRepo.getTaskById(id);
        TaskDto taskDto = mapper.convertToDto(task); // инлайн
        return taskDto;
    }

    public List<TaskDto> getAll() {
        List<Task> tasks = taskRepo.getAllTasks();
        return getTaskDtos(tasks);
    }

    public Integer add(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(task);

        return taskRepo.addTask(parameters);
    }

    public Integer update(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(task);

        return taskRepo.updateTask(parameters);
    }

    public Integer delete(Long id) {
        return taskRepo.deleteTask(id);
    }

    public List<TaskDto> search(SearchTask parameters) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(parameters);
        List<Task> tasks = taskRepo.search(mapSqlParameterSource);

        return getTaskDtos(tasks);
    }

    private List<TaskDto> getTaskDtos(List<Task> tasks) {
        if (tasks != null) {
            List<TaskDto> dtoList = new ArrayList<>();

            for (Task task : tasks) {
                dtoList.add(mapper.convertToDto(task));
            }
            return dtoList;
        } else {
            return null; // null в коде не используется. Или эксепшн, или пустой лист
        }
    }

    public Integer upgradedUpdate(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);

        MapSqlParameterSource parameters = getParameterForUpgradedUpdate(task);

        return taskRepo.upgradedUpdate(parameters);
    }
}
