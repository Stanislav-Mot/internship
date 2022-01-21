package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.exeption.ChangesNotAppliedExemption;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDtoMapper mapper;

    public PersonService(PersonRepo personRepo, PersonDtoMapper mapper) {
        this.personRepo = personRepo;
        this.mapper = mapper;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(Person person) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", person.getId());
        parameters.addValue("firstname", person.getFirstName());
        parameters.addValue("lastname", person.getLastName());
        parameters.addValue("birthdate", person.getBirthdate());
        return parameters;
    }

    public static Map<String, Object> getMapParamFromToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", "%" + token + "%");
        return params;
    }

    private MapSqlParameterSource getMapSqlParameterSource(String firstName, String lastName, Integer exactAge, Integer rangeAgeStart, Integer rangeAgeEnd) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("firstName", firstName);
        mapSqlParameterSource.addValue("lastName", lastName);
        mapSqlParameterSource.addValue("exactAge", exactAge);
        mapSqlParameterSource.addValue("rangeAgeStart", rangeAgeStart);
        mapSqlParameterSource.addValue("rangeAgeEnd", rangeAgeEnd);

        return mapSqlParameterSource;
    }

    public PersonDto getById(Long id) {
        return mapper.convertToDto(personRepo.getPersonById(id));
    }

    public List<PersonDto> getAll() {
        List<Person> list = personRepo.getAllPersons();
        return getPersonDtos(list);
    }

    public PersonDto add(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        personRepo.addPerson(parameters, keyHolder);

        return mapper.getDtoFromHolder(keyHolder);
    }

    public PersonDto update(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        Person response = personRepo.updatePerson(parameters);

        return mapper.convertToDto(response);
    }

    public void delete(Long id) {
        personRepo.deletePerson(id);
    }

    public void deleteGroup(Long personId, Long groupId) {
        Integer answer = personRepo.deleteGroupFromPerson(personId, groupId);
        if (answer < 1) {
            throw new ChangesNotAppliedExemption(
                    String.format("Person id: %d or Group id %d is wrong", personId, groupId));
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

    public List<PersonDto> search(String firstName, String lastName, Integer exactAge, Integer rangeAgeStart, Integer rangeAgeEnd) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(firstName, lastName, exactAge, rangeAgeStart, rangeAgeEnd);
        List<Person> list = personRepo.search(mapSqlParameterSource);
        return getPersonDtos(list);
    }

    private List<PersonDto> getPersonDtos(List<Person> list) {
        if (list != null) {
            List<PersonDto> dtoList = new ArrayList<>();

            for (Person person : list) {
                dtoList.add(mapper.convertToDto(person));
            }
            return dtoList;
        } else {
            return null;
        }
    }
}
