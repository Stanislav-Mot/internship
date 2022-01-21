package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.mapper.PersonDtoMapper;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.internship.internship.util.Helper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepo personRepo;
    @Mock
    private PersonDtoMapper mapper;

    @Test
    void getById() {
        Person person = newPersonForTest();
        PersonDto personDto = newPersonDtoForTest();

        when(personRepo.getPersonById(person.getId())).thenReturn(person);
        when(mapper.convertToDto(person)).thenReturn(personDto);

        PersonDto personFromService = personService.getById(person.getId());

        assertEquals(personFromService, personDto);

        verify(personRepo, times(1)).getPersonById(person.getId());
    }

    @Test
    void getAll() {
        List<Person> list = new ArrayList<Person>();
        list.add(new Person(33L));
        list.add(new Person(44L));
        list.add(new Person(55L));

        when(personRepo.getAllPersons()).thenReturn(list);

        List<PersonDto> personList = personService.getAll();

        assertEquals(3, personList.size());
        verify(personRepo, times(1)).getAllPersons();
    }

    @Test
    void add() {
        PersonDto personDto = newPersonDtoForTest();
        Person person = newPersonForTest();

//        when(personRepo.addPerson(any(MapSqlParameterSource.class))).thenReturn(1);
        when(mapper.convertToEntity(personDto)).thenReturn(person);

//        Integer result = personService.add(personDto);

//        assertEquals(1, result);

//        verify(personRepo, times(1)).addPerson(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        PersonDto personDto = newPersonDtoForTest();
        Person person = newPersonForTest();

//        when(personRepo.updatePerson(any(MapSqlParameterSource.class))).thenReturn(1);
        when(mapper.convertToEntity(personDto)).thenReturn(person);

//        Integer result = personService.update(personDto);

//        assertEquals(1, result);

        verify(personRepo, times(1)).updatePerson(any(MapSqlParameterSource.class));
    }

    @Test
    void delete() {
        Person person = newPersonForTest();

        when(personRepo.deletePerson(person.getId())).thenReturn(1);

//        Integer result = personService.delete(person.getId());

//        assertEquals(1, result);

        verify(personRepo, times(1)).deletePerson(person.getId());
    }

    @Test
    void deleteGroup() {
        Person person = newPersonForTest();
        Group group = newGroupForTest(person);

        when(personRepo.deleteGroupFromPerson(person.getId(), group.getId())).thenReturn(1);

//        Integer result = personService.deleteGroup(person.getId(), group.getId());

//        assertEquals(1, result);

        verify(personRepo, times(1)).deleteGroupFromPerson(person.getId(), group.getId());
    }

    @Test
    void addGroup() {
        PersonDto personDto = newPersonDtoForTest();
        Group group = newGroupForTest();

//        when(personRepo.addGroupToPerson(personDto.getId(), group.getId())).thenReturn(1);

//        Integer result = personService.addGroup(personDto.getId(), group.getId());

//        assertEquals(1, result);

        verify(personRepo, times(1)).addGroupToPerson(personDto.getId(), group.getId());
    }

    @Test
    void getMapSqlParameterSource() {
        Person person = newPersonForTest();

        MapSqlParameterSource parametersFromService = PersonService.getMapSqlParameterSource(person);
        MapSqlParameterSource parametersFromTest = new MapSqlParameterSource();

        parametersFromTest.addValue("id", person.getId());
        parametersFromTest.addValue("firstname", person.getFirstName());
        parametersFromTest.addValue("lastname", person.getLastName());
        parametersFromTest.addValue("age", person.getBirthdate());

        assertEquals(parametersFromService.getValues(), parametersFromTest.getValues());
    }

    @Test
    void search() {
//        SearchPerson parameters = new SearchPerson("Tester", null, null, null);
        Person person = newPersonForTest();
        List<Person> list = Collections.singletonList(person);

        when(personRepo.search(any(MapSqlParameterSource.class))).thenReturn(list);

//        List<PersonDto> personList = personService.search(parameters);

//        assertEquals(1, personList.size());
        verify(personRepo, times(1)).search(any(MapSqlParameterSource.class));
    }

    @Test
    void searchByTokenInName() {
        Person person = newPersonForTest();
        List<Person> list = Collections.singletonList(person);

        when(personRepo.searchByTokenInName(any(Map.class))).thenReturn(list);

        List<PersonDto> personList = personService.searchByTokenInName(person.getFirstName());

        assertEquals(1, personList.size());
        verify(personRepo, times(1)).searchByTokenInName(any(Map.class));
    }
}
