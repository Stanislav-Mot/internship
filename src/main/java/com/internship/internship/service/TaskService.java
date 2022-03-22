package com.internship.internship.service;

import com.internship.internship.dto.TaskDto;
import com.internship.internship.dto.search.SearchTaskDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Task;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskMapper mapper;
    private final TaskRepo repository;
    private final CacheService cacheService;

    public TaskService(TaskMapper mapper, TaskRepo repository, CacheService cacheService) {
        this.mapper = mapper;
        this.repository = repository;
        this.cacheService = cacheService;
    }

    public TaskDto getById(Long id) {
        return (TaskDto) cacheService.getTask(id);
    }

    public List<TaskDto> getAll() {
        return cacheService.getAllTask();
    }

    public List<TaskDto> getByGroupId(Long id) {
        return cacheService.getTaskByGroupId(id);
    }

    public List<TaskDto> getByPersonId(Long id) {
        return cacheService.getTaskByPersonId(id);
    }

    public TaskDto add(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        Task save = repository.save(task);
        TaskDto saveDto = mapper.convertToDto(save);
        cacheService.addAssignment(saveDto, Task.class);
        return saveDto;
    }

    public TaskDto update(TaskDto taskDto) {
        Task task = mapper.convertToEntity(taskDto);
        Task save = repository.save(task);
        cacheService.setInvalid(save.getId(), Task.class);
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
        Task task = repository.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Task Id %d is not found", id)));

        if (task.getStartTime() != null) {
            task.setProgress(progress);
            task.setSpentTime(LocalDateTime.now().getMinute() - task.getStartTime().getMinute());
        }
        return mapper.convertToDto(task);
    }

    public void delete(Long id) {
        Task task = repository.findById(id).orElseThrow(() -> new ChangesNotAppliedException(String.format("Task id: %d is not found", id)));
        cacheService.remove(id, Task.class);
        repository.delete(task);
    }
}