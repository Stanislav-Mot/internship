package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Task;
import com.internship.internship.repository.TaskRepo;
import org.springframework.context.annotation.Lazy;
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
//        TaskDto taskDto;
//        if ((taskDto = (TaskDto) cacheService.getTask(id)) == null) {
//            taskDto = mapper.convertToDto(taskRepo.getTaskById(id));
//            cacheService.put(taskDto.getId(), "task", taskDto);
//        }
        return (TaskDto) cacheService.getTask(id);
    }


    public List<TaskDto> getAll() {
        return cacheService.getAllTask();
//        return getTaskDtos(taskRepo.getAllTasks());
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
        return tasks.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    public TaskDto updateProgress(Long id, Integer progress) {
        Task task = taskRepo.updateProgress(id, progress);
        return mapper.convertToDto(task);
    }

    public TaskDto delete(Long id) {
        TaskDto taskDto;

        Integer answer = taskRepo.delete(id);
        if (answer < 1) {
            throw new ChangesNotAppliedException(String.format("Task id: %d is not found", id));
        } else {
            taskDto = (TaskDto) cacheService.get(id);
        }
        return taskDto;
    }
}