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

    public AdminService(AdminRepo adminRepo, TaskRepo taskRepo, PersonRepo personRepo, PersonDtoMapper mapper, TaskDtoMapper taskDtoMapper) {
        this.adminRepo = adminRepo;
        this.taskRepo = taskRepo;
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.taskDtoMapper = taskDtoMapper;
    }

    public List<PersonDto> retrievingAllTasks() {
        List<Person> personList = adminRepo.getAllPerson();

        for (Person person : personList) {
            List<Assignment> assignments = new ArrayList<>();
            assignments.addAll(taskRepo.getByPersonId(person.getId()));
            person.setGroups(assignments);
        }
        return personList.stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
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
        return taskRepo.getByPersonId(clientId).stream().map(x -> taskDtoMapper.convertToDto(x)).collect(Collectors.toList());
    }
}
