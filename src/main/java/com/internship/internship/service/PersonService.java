package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.repository.PersonRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {

    private final PersonRepo personRepo;

    public PersonService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(Person person) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", person.getId());
        parameters.addValue("firstname", person.getFirstName());
        parameters.addValue("lastname", person.getLastName());
        parameters.addValue("age", person.getAge());
        return parameters;
    }

    public static MapSqlParameterSource getMapSqlParameterSource(SearchPerson parameters) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        Long id = parameters.getId() != null ? parameters.getId() : null;
        String firstName = parameters.getFirstName() != null ? parameters.getFirstName() : null;
        String lastName = parameters.getLastName() != null ? parameters.getLastName() : null;
        Integer exactAge = parameters.getExactAge() != null ? parameters.getExactAge() : null;
        Integer rangeAge = parameters.getRangeAge() != null ? parameters.getRangeAge() : null;

        mapSqlParameterSource.addValue("id", id);
        mapSqlParameterSource.addValue("firstName", firstName);
        mapSqlParameterSource.addValue("lastName", lastName);
        mapSqlParameterSource.addValue("exactAge", exactAge);
        mapSqlParameterSource.addValue("rangeAge", rangeAge);

        return mapSqlParameterSource;
    }

    public static Map<String, Object> getMapParamFromToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", "%" + token + "%");
        return params;
    }

    public Person getById(Long id) {
        return personRepo.getPersonById(id);
    }

    public List<Person> getAll() {
        List<Person> personList = personRepo.getAllPersons();

        for (Person person : personList) {
            person.setGroups(personRepo.getGroupsById(person.getId()));
        }
        return personList;
    }

    public Integer add(Person person) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        return personRepo.addPerson(parameters);
    }

    public Integer update(Person person) {
        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        return personRepo.updatePerson(parameters);
    }

    public Integer delete(Long id) {
        return personRepo.deletePerson(id);
    }

    public Integer deleteGroup(Long id, Long groupId) {
        return personRepo.deleteGroupFromPerson(id, groupId);
    }

    public Integer addGroup(Long id, Group group) {
        return personRepo.addGroupToPerson(id, group);
    }

    public List<Person> search(SearchPerson parameters) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(parameters);

        return personRepo.search(mapSqlParameterSource);
    }

    public List<Person> searchByTokenInName(String token) {
        return personRepo.searchByTokenInName(getMapParamFromToken(token));
    }
}
