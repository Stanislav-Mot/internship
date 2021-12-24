package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.repository.PersonRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepo personRepo;

    public PersonService(PersonRepo personRepo) {
        this.personRepo = personRepo;
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

    public MapSqlParameterSource getMapSqlParameterSource(Person person) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", person.getId());
        parameters.addValue("firstname", person.getFirstName());
        parameters.addValue("lastname", person.getLastName());
        parameters.addValue("age", person.getAge());
        return parameters;
    }

    private MapSqlParameterSource getMapSqlParameterSource(SearchPerson parameters) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        String str =  parameters.getLastName();

        mapSqlParameterSource.addValue("id", parameters.getId());
        if(parameters.getFirstName() != null){
            mapSqlParameterSource.addValue("firstName", parameters.getFirstName());
        }else {
            mapSqlParameterSource.addValue("firstName", null, 0);
        }

        if(parameters.getLastName() != null){
            mapSqlParameterSource.addValue("lastName", parameters.getLastName());
        }else {
            mapSqlParameterSource.addValue("lastName", null, 0);
        }

        if(parameters.getExactAge() != null){
            mapSqlParameterSource.addValue("exactAge", parameters.getExactAge());
        }else {
            mapSqlParameterSource.addValue("exactAge", null, 0);
        }

        if(parameters.getRangeAge() != null){
            mapSqlParameterSource.addValue("rangeAge", parameters.getRangeAge());
        }else {
            mapSqlParameterSource.addValue("rangeAge", null, 0);
        }

        return mapSqlParameterSource;
    }
}
