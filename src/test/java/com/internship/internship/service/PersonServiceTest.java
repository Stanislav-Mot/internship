package com.internship.internship.service;

import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepo personRepo;

    @Test
    public void testCreateOrSavePerson()
    {
        Person person = new Person(15L);
        person.setFirstName("Tester");
        person.setLastName("Tester_ov");
        person.setAge(15);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", person.getId());
        parameters.addValue("firstname", person.getFirstName());
        parameters.addValue("lastname", person.getLastName());
        parameters.addValue("age", person.getAge());

        Mockito.doReturn(1).when(personRepo).addPerson(parameters);

        verify(personRepo, times(1)).addPerson(eq(parameters));
    }

    @Test
    void getById() {
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
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteGroup() {
    }

    @Test
    void addGroup() {
    }

    @Test
    void getMapSqlParameterSource() {
    }
}
