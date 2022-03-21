package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.PersonMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final TaskRepo taskRepo;
    private final PersonRepo personRepo;
    private final PersonMapper mapper;
    private final TaskMapper taskMapper;
    private final CacheService cacheService;

    public AdminService(TaskRepo taskRepo, PersonRepo personRepo, PersonMapper mapper, TaskMapper taskMapper, CacheService cacheService) {
        this.taskRepo = taskRepo;
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.taskMapper = taskMapper;
        this.cacheService = cacheService;
    }

    public List<PersonDto> retrievingAllTasks() {
        List<Person> personList = personRepo.findAll();
        return personList.stream().map(person -> {
            PersonDto personDto = mapper.convertToDto(person);
            personDto.setAssignments(
                    cacheService.getTaskByPersonId(personDto.getId())
                            .stream().map(Assignment.class::cast).collect(Collectors.toList()));
            return personDto;
        }).collect(Collectors.toList());
    }

    public PersonDto clearTaskByClientIdOrProgress(Long clientId, Long taskProgress) {
        Person person = personRepo.findById(clientId).orElseThrow(() -> new DataNotFoundException(String.format("Person Id %d is not found", clientId)));

        List<TaskDto> tasks = getNativeTasksByPerson(clientId);
        List<Long> ids = tasks.stream().map(TaskDto::getId).collect(Collectors.toList());
        personRepo.clearTasks(ids, taskProgress);

        return mapper.convertToDto(person);
    }

    public List<TaskDto> resetProgressOfAllTasks(Long clientId) {
        List<TaskDto> tasks = getNativeTasksByPerson(clientId);
        taskRepo.resetProgresses(
                tasks.stream().map(TaskDto::getId)
                        .collect(Collectors.toList()));
        return tasks;
    }

    private List<TaskDto> getNativeTasksByPerson(Long id) {
        List<TaskDto> tasks = new ArrayList<>();
        List<Long> groupIds = taskRepo.findGroupIdByPersonId(id);
        groupIds.forEach(x ->
                tasks.addAll(taskRepo.findByGroupId(x)
                        .stream().map(task -> {
                            task.setPriority(0);
                            return taskMapper.convertToDto(task);
                        }).collect(Collectors.toList())));
        return tasks;
    }
}
