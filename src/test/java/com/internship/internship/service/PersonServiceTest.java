package com.internship.internship.service;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.dto.search.SearchPersonDto;
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
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

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
        PersonDto personDto = new PersonDto(1L);
        Person person = new Person(1L);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        when(mapper.convertToEntity(personDto)).thenReturn(person);
        when(personRepo.addPerson(any(SqlParameterSource.class))).thenReturn(keyHolder);
        when(mapper.getDtoFromHolder(any(KeyHolder.class))).thenReturn(personDto);

        PersonDto result = personService.add(personDto);

        assertEquals(personDto.getId(), result.getId());

        verify(personRepo, times(1)).addPerson(any(MapSqlParameterSource.class));
    }

    @Test
    void update() {
        PersonDto personDto = newPersonDtoForTest();
        Person person = newPersonForTest();

        when(mapper.convertToEntity(personDto)).thenReturn(person);
        when(personRepo.updatePerson(any(MapSqlParameterSource.class))).thenReturn(person);
        when(mapper.convertToDto(person)).thenReturn(personDto);

        PersonDto result = personService.update(personDto);

        assertEquals(personDto.getId(), result.getId());

        verify(personRepo, times(1)).updatePerson(any(MapSqlParameterSource.class));
    }

    @Test
    void delete() {
        Person person = newPersonForTest();

        when(personRepo.deletePerson(person.getId())).thenReturn(1);

        personService.delete(person.getId());

        verify(personRepo, times(1)).deletePerson(person.getId());
    }

    @Test
    void deleteGroup() {
        Person person = newPersonForTest();
        Group group = newGroupForTest();

        when(personRepo.deleteGroupFromPerson(person.getId(), group.getId())).thenReturn(1);

        personService.deleteGroup(person.getId(), group.getId());

        verify(personRepo, times(1)).deleteGroupFromPerson(person.getId(), group.getId());
    }

    @Test
    void addGroup() {
        PersonDto personDto = newPersonDtoForTest();
        Person person = newPersonForTest();
        Group group = newGroupForTest();

        when(personRepo.addGroupToPerson(personDto.getId(), group.getId())).thenReturn(person);
        when(mapper.convertToDto(any(Person.class))).thenReturn(personDto);
        PersonDto result = personService.addGroup(personDto.getId(), group.getId());

        assertEquals(personDto.getId(), result.getId());

        verify(personRepo, times(1)).addGroupToPerson(personDto.getId(), group.getId());
    }


    @Test
    void search() {
        SearchPersonDto searchPersonDto = new SearchPersonDto("", "", 1, 1, 1);
        Person person = newPersonForTest();
        List<Person> list = Collections.singletonList(person);

        when(personRepo.search(any(MapSqlParameterSource.class))).thenReturn(list);

        List<PersonDto> personList = personService.search(searchPersonDto);

        assertEquals(1, personList.size());
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
