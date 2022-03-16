package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Task;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskDtoMapper mapper;
    private final CacheService cacheService;
    private final TaskRepo repository;

    public TaskService(TaskDtoMapper mapper, CacheService cacheService, TaskRepo repository) {
        this.mapper = mapper;
        this.cacheService = cacheService;
        this.repository = repository;
    }

    public TaskDto getById(Long id) {
        if (cacheService.isValid()) {
//            return (TaskDto) cacheService.getTask(id);
            return null;
        } else {
            Task task = repository.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Task Id %d is not found", id)));
            return mapper.convertToDto(task);
        }
    }

    public List<TaskDto> getAll() {
        if (cacheService.isValid()) {
            return cacheService.getAllTask();
        } else {
            return getTaskDtos(repository.findAll());
        }
    }

    public List<TaskDto> getByGroupId(Long id) {
//        return getTaskDtos(repository.findByGroupsId(id));
        return null;
    }

    public List<TaskDto> getByPersonId(Long id) {
//        return getTaskDtos(repository.findByPersonsId(id));
        return null;
    }

    public TaskDto add(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        Task save = repository.save(task);
        TaskDto dto = mapper.convertToDto(save);
//        cacheService.put(dto.getId(), Task.class, dto);
        return dto;
    }

    public TaskDto update(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        Task save = repository.save(task);
        cacheService.setValid(false);
        return mapper.convertToDto(save);
    }

    public List<TaskDto> search(SearchTaskDto searchTaskDto) {
        List<Task> tasks = repository.searchCustom(
                searchTaskDto.getName(),
                searchTaskDto.getMinStartTime(),
                searchTaskDto.getMaxStartTime(),
                searchTaskDto.getFromProgress(),
                searchTaskDto.getToProgress()
        );
        return getTaskDtos(tasks);
    }

    private List<TaskDto> getTaskDtos(List<Task> tasks) {
        return tasks.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public TaskDto updateProgress(Long id, Integer progress) {
        repository.updateProgress(id, progress);
        repository.setSpentTime(id);

        Task task = repository.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Task Id %d is not found", id)));
        cacheService.setValid(false);
        return mapper.convertToDto(task);
    }

    public TaskDto delete(Long id) {
        repository.findById(id).orElseThrow(() -> new ChangesNotAppliedException(String.format("Task id: %d is not found", id)));
        repository.deleteById(id);
//        TaskDto taskDto = (TaskDto) cacheService.getTask(id);
//        cacheService.remove(id, Task.class);
//        return taskDto;
        return null;
    }
}