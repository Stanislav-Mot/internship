package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Task;
import com.internship.internship.dto.search.SearchTask;
import com.internship.internship.repository.TaskRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final TaskDtoMapper mapper;

    public TaskService(TaskRepo taskRepo, TaskDtoMapper mapper) {
        this.taskRepo = taskRepo;
        this.mapper = mapper;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(Task task) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", task.getId());
        parameters.addValue("name", task.getName());
        parameters.addValue("description", task.getDescription());
        parameters.addValue("estimate", task.getEstimate());
        parameters.addValue("progress", task.getProgress());

        return parameters;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(String name,
                                                                 String fromProgress,
                                                                 String toProgress,
                                                                 String minStartTime,
                                                                 String maxStartTime) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("name", name);
        mapSqlParameterSource.addValue("fromProgress", fromProgress);
        mapSqlParameterSource.addValue("toProgress", toProgress);
        mapSqlParameterSource.addValue("fromStartTime", minStartTime);
        mapSqlParameterSource.addValue("toStartTime", maxStartTime);
        return mapSqlParameterSource;
    }

    public TaskDto getById(Long id) {
        return mapper.convertToDto(taskRepo.getTaskById(id));
    }

    public List<TaskDto> getAll() {
        return getTaskDtos(taskRepo.getAllTasks());
    }

    public List<TaskDto> getByGroupId(Long id) {
        return getTaskDtos(taskRepo.getByGroupId(id));
    }

    public List<TaskDto> getByPersonId(Long id) {
        return getTaskDtos(taskRepo.getByPersonId(id));
    }

    public Integer add(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(task);

        return taskRepo.addTask(parameters);
    }

    public Integer update(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(task);

        return taskRepo.update(parameters);
    }

    public Integer delete(Long id) {
        return taskRepo.deleteTask(id);
    }

    public List<TaskDto> search(String name,
                                String fromProgress,
                                String toProgress,
                                String minStartTime,
                                String maxStartTime) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(name, fromProgress, toProgress, minStartTime, maxStartTime);

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
            return null;
        }
    }

    public Integer updateProgress(Long id, Integer progress) {
        return taskRepo.updateProgress(id, progress);
    }
}
