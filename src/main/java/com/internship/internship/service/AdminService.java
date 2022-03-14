package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final TaskRepo taskRepo;
    private final PersonRepo personRepo;
    private final PersonDtoMapper mapper;
    private final TaskDtoMapper taskDtoMapper;

    public AdminService(TaskRepo taskRepo, PersonRepo personRepo, PersonDtoMapper mapper, TaskDtoMapper taskDtoMapper) {
        this.taskRepo = taskRepo;
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.taskDtoMapper = taskDtoMapper;
    }

    public List<PersonDto> retrievingAllTasks() {
        List<PersonDto> personList = personRepo.findAll().stream().map(mapper::convertToDto).collect(Collectors.toList());

        for (PersonDto person : personList) {
            List<Assignment> assignments = new ArrayList<>(taskRepo.findByPersonsId(person.getId()));
            person.setGroups(assignments);
        }
        return personList;
    }

    public PersonDto clearTaskByClientIdOrProgress(Long clientId, Long taskProgress) {
        Person person = personRepo.findById(clientId).orElseThrow(() -> new DataNotFoundException(String.format("Person Id %d is not found", clientId)));

        personRepo.clearTasks(clientId, taskProgress);
        person.setGroups(Collections.unmodifiableList(taskRepo.findByPersonsId(clientId)));
        return mapper.convertToDto(person);
    }

    public List<TaskDto> resetProgressOfAllTasks(Long clientId) {
        personRepo.resetProgress(clientId);
        return taskRepo.findByPersonsId(clientId).stream().map(taskDtoMapper::convertToDto).collect(Collectors.toList());
    }
}
