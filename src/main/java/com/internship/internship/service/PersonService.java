package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.search.SearchPersonDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonDtoMapper mapper;
    private final PersonRepo repository;

    public PersonService(PersonDtoMapper mapper, PersonRepo repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public PersonDto getById(Long id) {
        Person person = repository.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Person Id %d is not found", id)));
        return mapper.convertToDto(person);
    }

    public List<PersonDto> getAll() {
        return getPersonDtos(repository.findAll());
    }

    public PersonDto update(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);
        Person response = repository.save(person);
        return mapper.convertToDto(response);
    }

    public void deleteGroup(Long personId, Long groupId) {
        Integer answer = repository.deleteGroupFromPerson(personId, groupId);
        if (answer < 1) {
            throw new ChangesNotAppliedException(String.format("Person id: %d or Group id %d is wrong", personId, groupId));
        }
    }

    public PersonDto addGroup(Long personId, Long groupId) {
        Person person = repository.addGroupToPerson(personId, groupId);
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
        repository.findById(id).orElseThrow(() -> new ChangesNotAppliedException(String.format("Person id: %d is not found", id)));
        repository.deleteById(id);
    }

    private List<PersonDto> getPersonDtos(List<Person> list) {
        return list.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }
}
