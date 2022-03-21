package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.search.SearchPersonDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.PersonMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.PersonRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonMapper mapper;
    private final PersonRepo repository;
    private final GroupRepo groupRepo;
    private final CacheService cacheService;

    public PersonService(PersonMapper mapper, PersonRepo repository, GroupRepo groupRepo, CacheService cacheService) {
        this.mapper = mapper;
        this.repository = repository;
        this.groupRepo = groupRepo;
        this.cacheService = cacheService;
    }

    public PersonDto getById(Long id) {
        Person person = repository.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Person Id %d is not found", id)));
        PersonDto personDto = mapper.convertToDto(person);
        personDto.setAssignments(cacheService.findByPersonId(id));
        return personDto;
    }

    public List<PersonDto> getAll() {
        List<Person> persons = repository.findAll();
        return persons.stream()
                .map(person -> {
                    PersonDto personDto = mapper.convertToDto(person);
                    personDto.setAssignments(cacheService.findByPersonId(person.getId()));
                    return personDto;
                }).collect(Collectors.toList());
    }

    public PersonDto update(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);
        Person response = repository.save(person);
        return mapper.convertToDto(response);
    }

    @Transactional
    public PersonDto deleteGroup(Long personId, Long groupId) {
        Person person = repository.findById(personId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Person id: %d is wrong", personId)));
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Group id: %d is wrong", groupId)));
        person.getGroups().remove(group);
        return mapper.convertToDto(person);
    }

    @Transactional
    public PersonDto addGroup(Long personId, Long groupId) {
        Person person = repository.findById(personId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Person id: %d is wrong", personId)));
        Group group = groupRepo.findById(groupId).orElseThrow(() ->
                new ChangesNotAppliedException(String.format("Group id: %d is wrong", groupId)));
        person.getGroups().add(group);
        return mapper.convertToDto(person);
    }

    public List<PersonDto> searchByTokenInName(String token) {
        List<Person> list = repository.searchByTokenInName(token);
        return getPersonDtos(list);
    }

    public List<PersonDto> search(SearchPersonDto searchPersonDto) {
        List<Person> list = repository.searchByToken(
                searchPersonDto.getFirstName(),
                searchPersonDto.getLastName(),
                searchPersonDto.getExactAge(),
                searchPersonDto.getRangeAgeStart(),
                searchPersonDto.getRangeAgeEnd()
        );
        return getPersonDtos(list);
    }

    public void delete(Long id) {
        Person person = repository.findById(id).orElseThrow(() -> new ChangesNotAppliedException(String.format("Person id: %d is not found", id)));
        repository.delete(person);
    }

    private List<PersonDto> getPersonDtos(List<Person> list) {
        return list.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }
}
