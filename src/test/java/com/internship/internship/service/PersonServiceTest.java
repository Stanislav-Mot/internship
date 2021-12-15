package com.internship.internship.service;

import com.internship.internship.model.Person;
import com.internship.internship.repository.PersonRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class PersonServiceTest {

    @Mock
    private PersonRepo personRepo;
    @InjectMocks
    private PersonService personService;

    @Test
    public void getById() {
        Person person = new Person();
    }

    @Test
    void getAll() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person());
        given(personRepo.getAllPersons()).willReturn(persons);
        List<Person> expected = personService.getAll();
        assertEquals(expected, persons);
        verify(personRepo).getAllPersons();
    }
}
