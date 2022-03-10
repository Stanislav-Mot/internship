package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
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
    private final CacheService cacheService;

    public TaskService(TaskRepo taskRepo, TaskDtoMapper mapper, CacheService cacheService) {
        this.taskRepo = taskRepo;
        this.mapper = mapper;
        this.cacheService = cacheService;
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
        if (cacheService.isValid()) {
            return (TaskDto) cacheService.getTask(id);
        } else {
            return mapper.convertToDto(taskRepo.getTaskById(id));
        }
    }

    public List<TaskDto> getAll() {
        if (cacheService.isValid()) {
            return cacheService.getAllTask();
        } else {
            return getTaskDtos(taskRepo.getAllTasks());
        }
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
        TaskDto dtoFromHolder = mapper.getDtoFromHolder(keyHolder);
        cacheService.put(dtoFromHolder.getId(), Task.class, dtoFromHolder);
        return dtoFromHolder;
    }

    public TaskDto update(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        MapSqlParameterSource parameters = getMapSqlParameterSource(task);
        Task response = taskRepo.update(parameters);
        cacheService.setValid(false);
        return mapper.convertToDto(response);
    }

    public List<TaskDto> search(SearchTaskDto searchTaskDto) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(searchTaskDto);
        List<Task> tasks = taskRepo.search(mapSqlParameterSource);
        return getTaskDtos(tasks);
    }

    private List<TaskDto> getTaskDtos(List<Task> tasks) {
        return tasks.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    public TaskDto updateProgress(Long id, Integer progress) {
        Task task = taskRepo.updateProgress(id, progress);
        cacheService.setValid(false);
        return mapper.convertToDto(task);
    }

    public TaskDto delete(Long id) {
        TaskDto taskDto;

        Integer answer = taskRepo.delete(id);
        if (answer < 1) {
            throw new ChangesNotAppliedException(String.format("Task id: %d is not found", id));
        } else {
            taskDto = (TaskDto) cacheService.getTask(id);
            cacheService.remove(id, Task.class);
        }
        return taskDto;
    }
}