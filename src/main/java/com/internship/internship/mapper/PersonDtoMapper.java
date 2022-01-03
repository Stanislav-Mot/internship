package com.internship.internship.mapper;

import com.internship.internship.dto.PersonDto;
import com.internship.internship.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonDtoMapper {

    private final ModelMapper modelMapper;

    public PersonDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PersonDto convertToDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }

    public Person convertToEntity(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }
}
