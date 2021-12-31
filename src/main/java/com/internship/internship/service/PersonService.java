package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.PersonDto;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.mapper.PersonMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.repository.PersonRepo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDtoMapper mapper;
    private final GroupDtoMapper groupDtoMapper;

    public PersonService(PersonRepo personRepo, PersonDtoMapper mapper, GroupDtoMapper groupDtoMapper) {
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.groupDtoMapper = groupDtoMapper;
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

        mapSqlParameterSource.addValue("firstName", parameters.getFirstName());
        mapSqlParameterSource.addValue("lastName", parameters.getLastName());
        mapSqlParameterSource.addValue("exactAge", parameters.getExactAge());
        mapSqlParameterSource.addValue("rangeAge", parameters.getRangeAge());

        return mapSqlParameterSource;
    }

    public static Map<String, Object> getMapParamFromToken(String token) {
        Map<String, Object> params = new HashMap<>();
        System.out.println(token);
        params.put("token", "%" + token + "%");
        return params;
    }

    public PersonDto getById(Long id) {
        Person person = personRepo.getPersonById(id);
        if(person != null)
            return mapper.convertToDto(person);
        else
            return null;
    }

    public List<PersonDto> getAll() {
        List<Person> list = personRepo.getAllPersons();
        return getPersonDtos(list);
    }

    public Integer add(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        return personRepo.addPerson(parameters);
    }

    public Integer update(PersonDto personDto) {
        Person person = mapper.convertToEntity(personDto);

        MapSqlParameterSource parameters = getMapSqlParameterSource(person);

        return personRepo.updatePerson(parameters);
    }

    public Integer delete(Long id) {
        return personRepo.deletePerson(id);
    }

    public Integer deleteGroup(Long id, Long groupId) {
        return personRepo.deleteGroupFromPerson(id, groupId);
    }

    public Integer addGroup(Long id, GroupDto groupDto) {
        Group group = groupDtoMapper.convertToEntity(groupDto);
        return personRepo.addGroupToPerson(id, group);
    }

    public List<PersonDto> search(SearchPerson parameters) {
        MapSqlParameterSource mapSqlParameterSource = getMapSqlParameterSource(parameters);
        List<Person> list = personRepo.search(mapSqlParameterSource);
        return getPersonDtos(list);
    }

    private List<PersonDto> getPersonDtos(List<Person> list) {
        if (list != null) {
            List<PersonDto> dtoList = new ArrayList<>();

            for (Person person: list){
                dtoList.add(mapper.convertToDto(person));
            }
            return dtoList;
        }
        return null;
    }

    public List<PersonDto> searchByTokenInName(String token) {

        List<Person> list = personRepo.searchByTokenInName(getMapParamFromToken(token));
        return getPersonDtos(list);
    }
}
