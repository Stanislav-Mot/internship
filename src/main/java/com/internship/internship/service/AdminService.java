package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Person;
import com.internship.internship.repository.AdminRepo;
import com.internship.internship.repository.PersonRepo;
import com.internship.internship.repository.TaskRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepo adminRepo;
    private final TaskRepo taskRepo;
    private final PersonRepo personRepo;
    private final PersonDtoMapper mapper;
    private final TaskDtoMapper taskDtoMapper;
    private final CacheService cacheService;

    public AdminService(AdminRepo adminRepo, TaskRepo taskRepo, PersonRepo personRepo, PersonDtoMapper mapper, TaskDtoMapper taskDtoMapper, CacheService cacheService) {
        this.adminRepo = adminRepo;
        this.taskRepo = taskRepo;
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.taskDtoMapper = taskDtoMapper;
        this.cacheService = cacheService;
    }

    public List<PersonDto> retrievingAllTasks() {
        List<PersonDto> personList = adminRepo.getAllPerson().stream().map(mapper::convertToDto).collect(Collectors.toList());

        for (PersonDto person : personList) {
            List<Assignment> assignments = new ArrayList<>(taskRepo.getByPersonId(person.getId()));
            person.setGroups(assignments);
        }
        return personList;
    }

    public PersonDto clearTaskByClientIdOrProgress(Long clientId, Long taskProgress) {
        Person person = personRepo.getPersonById(clientId);

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("clientId", clientId);
        sqlParameterSource.addValue("taskProgress", taskProgress);

        adminRepo.clearTasks(sqlParameterSource);
        person.setGroups(Collections.unmodifiableList(taskRepo.getByPersonId(clientId)));
        return mapper.convertToDto(person);
    }

    public List<TaskDto> resetProgressOfAllTasks(Long clientId) {
        adminRepo.resetProgress(clientId);
        return taskRepo.getByPersonId(clientId).stream().map(taskDtoMapper::convertToDto).collect(Collectors.toList());
    }
}
