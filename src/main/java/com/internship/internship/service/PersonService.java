package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.search.SearchPersonDto;
import com.internship.internship.exeption.ChangesNotAppliedException;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDtoMapper mapper;

    public PersonService(PersonRepo personRepo, PersonDtoMapper mapper) {
        this.personRepo = personRepo;
        this.mapper = mapper;
    }

    private MapSqlParameterSource getMapSqlParameterSource(Person person) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", person.getId());
        parameters.addValue("first_name", person.getFirstName());
        parameters.addValue("last_name", person.getLastName());
        parameters.addValue("birthdate", person.getBirthdate());
        return parameters;
    }

    private Map<String, Object> getMapParamFromToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", "%" + token + "%");
        return params;
    }

    private MapSqlParameterSource getMapSqlParameterSource(SearchPersonDto searchPersonDto) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("first_name", searchPersonDto.getFirstName());
        mapSqlParameterSource.addValue("lastName", searchPersonDto.getLastName());
        mapSqlParameterSource.addValue("exactAge", searchPersonDto.getExactAge());
        mapSqlParameterSource.addValue("rangeAgeStart", searchPersonDto.getRangeAgeStart());
        mapSqlParameterSource.addValue("rangeAgeEnd", searchPersonDto.getRangeAgeEnd());
        return mapSqlParameterSource;
    }

    public PersonDto getById(Long id) {
        return mapper.convertToDto(personRepo.getPersonById(id));
    }

    public List<PersonDto> getAll() {
        return getPersonDtos(personRepo.getAllPersons());
    }

    public PersonDto update(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);
        MapSqlParameterSource parameters = getMapSqlParameterSource(person);
        Person response = personRepo.updatePerson(parameters);
        return mapper.convertToDto(response);
    }

    public void deleteGroup(Long personId, Long groupId) {
        Integer answer = personRepo.deleteGroupFromPerson(personId, groupId);
        if (answer < 1) {
            throw new ChangesNotAppliedException(String.format("Person id: %d or Group id %d is wrong", personId, groupId));
        }
    }

    public PersonDto addGroup(Long personId, Long groupId) {
        Person person = personRepo.addGroupToPerson(personId, groupId);
        return mapper.convertToDto(person);
    }

    public List<PersonDto> searchByTokenInName(String token) {
        List<Person> list = personRepo.searchByTokenInName(getMapParamFromToken(token));
        return getPersonDtos(list);
    }

    public List<PersonDto> search(SearchPersonDto searchPersonDto) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(searchPersonDto);
        List<Person> list = personRepo.search(mapSqlParameterSource);
        return getPersonDtos(list);
    }

    private List<PersonDto> getPersonDtos(List<Person> list) {
        return list.stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
    }

    public void delete(Long id) {
        Integer answer = personRepo.delete(id);
        if (answer < 1) {
            throw new ChangesNotAppliedException(String.format("Person id: %d is not found", id));
        }
    }
}
