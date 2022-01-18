package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
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

    public static MapSqlParameterSource getMapSqlParameterSource(String firstName, String lastName, String exactAge, String rangeAge) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("firstName", firstName);
        mapSqlParameterSource.addValue("lastName", lastName);
        mapSqlParameterSource.addValue("exactAge", exactAge);
        mapSqlParameterSource.addValue("rangeAge", rangeAge);

        return mapSqlParameterSource;
    }

    public static Map<String, Object> getMapParamFromToken(String token) {
        Map<String, Object> params = new HashMap<>();
        System.out.println(token);
        params.put("token", "%" + token + "%");
        return params;
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

    public Integer update(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        return personRepo.updatePerson(parameters);
    }

    public Integer delete(Long id) {
        return personRepo.deletePerson(id);
    }

    public Integer deleteGroup(Long personId, Long groupId) {
        return personRepo.deleteGroupFromPerson(personId, groupId);
    }

    public Integer addGroup(Long personId, Long groupId) {
        return personRepo.addGroupToPerson(personId, groupId);
    }

    public List<PersonDto> searchByTokenInName(String token) {
        List<Person> list = personRepo.searchByTokenInName(getMapParamFromToken(token));
        return getPersonDtos(list);
    }

    public List<PersonDto> search(String firstName, String lastName, String exactAge, String rangeAge) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(firstName, lastName, exactAge, rangeAge);
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
