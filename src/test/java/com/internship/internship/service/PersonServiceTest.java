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
    PersonService service;

    @Mock
    PersonRepo dao;

    @Test
    public void testGetAllPersons()
    {
        List<Person> list = new ArrayList<Person>();
        Person empOne = new Person(33L);
        Person empTwo = new Person(44L);
        Person empThree = new Person(55L);

        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);

        Mockito.doReturn(list).when(dao).getAllPersons();
        when(dao.getAllPersons()).thenReturn(list);

        List<Person> empList = service.getAll();

        assertEquals(3, empList.size());
        verify(dao, times(1)).getAllPersons();
    }

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

        service.add(person);

        verify(dao, times(1)).addPerson(eq(parameters));
    }
}
