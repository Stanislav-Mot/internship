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

    public AdminService(TaskRepo taskRepo, PersonRepo personRepo, PersonMapper mapper, TaskMapper taskMapper) {
        this.taskRepo = taskRepo;
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.taskMapper = taskMapper;
    }

    public List<PersonDto> retrievingAllTasks() {
        List<Person> personList = personRepo.findAll();
        for (Person person : personList) {
            List<Assignment> assignments = new ArrayList<>(taskRepo.findByPersonsId(person.getId()));
//            person.getGroups(assignments);
        }
        return personList.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    public PersonDto clearTaskByClientIdOrProgress(Long clientId, Long taskProgress) {
        Person person = personRepo.findById(clientId).orElseThrow(() -> new DataNotFoundException(String.format("Person Id %d is not found", clientId)));

        personRepo.clearTasks(clientId, taskProgress);
//        person.getGroups(Collections.unmodifiableList(taskRepo.findByPersonsId(clientId)));
        return mapper.convertToDto(person);
    }

    public List<TaskDto> resetProgressOfAllTasks(Long clientId) {
        personRepo.resetProgress(clientId);
        return taskRepo.findByPersonsId(clientId).stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
