package com.internship.internship.mapper;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.internship.internship.util.Helper.newPersonDtoForTest;
import static com.internship.internship.util.Helper.newPersonForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PersonDtoMapperTest {

    @Autowired
    private PersonDtoMapper personDtoMapper;

    @Test
    void convertToDto() {
        Person person = newPersonForTest();
        PersonDto personDto = personDtoMapper.convertToDto(person);

        assertEquals(person.getId(), personDto.getId());
        assertEquals(person.getFirstName(), personDto.getFirstName());
        assertEquals(person.getLastName(), personDto.getLastName());
        assertEquals(person.getBirthdate(), personDto.getBirthdate());
        assertEquals(person.getGroups(), personDto.getGroups());
    }

    @Test
    void convertToEntity() {
        PersonDto personDto = newPersonDtoForTest();
        Person person = personDtoMapper.convertToEntity(personDto);

        assertEquals(person.getId(), personDto.getId());
        assertEquals(person.getFirstName(), personDto.getFirstName());
        assertEquals(person.getLastName(), personDto.getLastName());
        assertEquals(person.getBirthdate(), personDto.getBirthdate());
        assertEquals(person.getGroups(), personDto.getGroups());
    }
}