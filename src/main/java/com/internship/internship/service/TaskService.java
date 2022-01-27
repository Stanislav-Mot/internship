package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Task;
import com.internship.internship.repository.TaskRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final TaskDtoMapper mapper;

    public TaskService(TaskRepo taskRepo, TaskDtoMapper mapper) {
        this.taskRepo = taskRepo;
        this.mapper = mapper;
    }

    private MapSqlParameterSource getMapSqlParameterSource(Task task) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", task.getId());
        parameters.addValue("name", task.getName());
        parameters.addValue("description", task.getDescription());
        parameters.addValue("estimate", task.getEstimate());
        parameters.addValue("priority", task.getPriority());
        return parameters;
    }

    private MapSqlParameterSource getMapSqlParameterSource(SearchTaskDto searchTaskDto) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", searchTaskDto.getName());
        mapSqlParameterSource.addValue("fromProgress", searchTaskDto.getFromProgress());
        mapSqlParameterSource.addValue("toProgress", searchTaskDto.getToProgress());

        LocalDateTime localDateTime = null;
        if (searchTaskDto.getMinStartTime() != null) {
            localDateTime = LocalDateTime.parse(searchTaskDto.getMinStartTime());
        }
        mapSqlParameterSource.addValue("fromStartTime", localDateTime);

        localDateTime = null;
        if (searchTaskDto.getMaxStartTime() != null) {
            localDateTime = LocalDateTime.parse(searchTaskDto.getMaxStartTime());
        }
        mapSqlParameterSource.addValue("toStartTime", localDateTime);

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

    public TaskDto add(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        MapSqlParameterSource parameters = getMapSqlParameterSource(task);
        KeyHolder keyHolder = taskRepo.addTask(parameters);
        return mapper.getDtoFromHolder(keyHolder);
    }

    public TaskDto update(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        MapSqlParameterSource parameters = getMapSqlParameterSource(task);
        Task response = taskRepo.update(parameters);
        return mapper.convertToDto(response);
    }

    public List<TaskDto> search(SearchTaskDto searchTaskDto) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(searchTaskDto);
        List<Task> tasks = taskRepo.search(mapSqlParameterSource);
        return getTaskDtos(tasks);
    }

    private List<TaskDto> getTaskDtos(List<Task> tasks) {
        return tasks.stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
    }

    public TaskDto updateProgress(Long id, Integer progress) {
        Task task = taskRepo.updateProgress(id, progress);
        return mapper.convertToDto(task);
    }
}