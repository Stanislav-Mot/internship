package com.internship.internship.service;

import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.Task;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.model.search.SearchTask;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private final Long CORRECT_ID = 999L;
    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepo personRepo;

    @Test
    void getById() {
        Person person = newPersonForTest();

        when(personRepo.getPersonById(person.getId())).thenReturn(person);

        Person personFromService = personService.getById(person.getId());

        assertEquals(personFromService, person);

        verify(personRepo, times(1)).getPersonById(person.getId());
    }

    @Test
    void getAll() {
        List<Person> list = new ArrayList<Person>();
        list.add(new Person(33L));
        list.add(new Person(44L));
        list.add(new Person(55L));

        when(personRepo.getAllPersons()).thenReturn(list);

        List<Person> personList = personService.getAll();

        assertEquals(3, personList.size());
        verify(personRepo, times(1)).getAllPersons();
    }

    @Test
    void add() {
        Person person = newPersonForTest();

        when(personRepo.addPerson(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = personService.add(person);

        assertEquals(1, result);

        verify(personRepo, times(1)).addPerson(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        Person person = newPersonForTest();

        when(personRepo.updatePerson(any(MapSqlParameterSource.class))).thenReturn(1);

        Integer result = personService.update(person);

        assertEquals(1, result);

        verify(personRepo, times(1)).updatePerson(any(MapSqlParameterSource.class));
    }

    @Test
    void delete() {
        Person person = newPersonForTest();

        when(personRepo.deletePerson(person.getId())).thenReturn(1);

        Integer result = personService.delete(person.getId());

        assertEquals(1, result);

        verify(personRepo, times(1)).deletePerson(person.getId());
    }

    @Test
    void deleteGroup() {
        Person person = newPersonForTest();
        Group group = newGroupForTest(person);

        when(personRepo.deleteGroupFromPerson(person.getId(), group.getId())).thenReturn(1);

        Integer result = personService.deleteGroup(person.getId(), group.getId());

        assertEquals(1, result);

        verify(personRepo, times(1)).deleteGroupFromPerson(person.getId(), group.getId());
    }

    @Test
    void addGroup() {
        Person person = newPersonForTest();
        Group group = newGroupForTest(person);

        when(personRepo.addGroupToPerson(person.getId(), group)).thenReturn(1);

        Integer result = personService.addGroup(person.getId(), group);

        assertEquals(1, result);

        verify(personRepo, times(1)).addGroupToPerson(person.getId(), group);
    }

    @Test
    void getMapSqlParameterSource() {
        Person person = newPersonForTest();

        MapSqlParameterSource parametersFromService = personService.getMapSqlParameterSource(person);
        MapSqlParameterSource parametersFromTest = new MapSqlParameterSource();

        parametersFromTest.addValue("id", person.getId());
        parametersFromTest.addValue("firstname", person.getFirstName());
        parametersFromTest.addValue("lastname", person.getLastName());
        parametersFromTest.addValue("age", person.getAge());

        assertEquals(parametersFromService.getValues(), parametersFromTest.getValues());
    }

    @Test
    void search() {
        SearchPerson parameters = new SearchPerson(CORRECT_ID, "Tester", null, null, null);
        Person person = newPersonForTest();
        List<Person> list = Collections.singletonList(person);

        when(personRepo.search(any(MapSqlParameterSource.class))).thenReturn(list);

        List<Person> personList = personService.search(parameters);

        assertEquals(1, personList.size());
        verify(personRepo, times(1)).search(any(MapSqlParameterSource.class));
    }

    private Group newGroupForTest(Person person) {
        return new Group(CORRECT_ID, "TesterGroup", null, person);
    }

    private Person newPersonForTest() {
        return new Person(CORRECT_ID, "Tester", "Rochester", 99, null);
    }
}