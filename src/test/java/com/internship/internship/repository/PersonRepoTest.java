package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PersonRepoTest {

    @Autowired
    PersonRepo personRepo;

    @Test
    public void testCreateReadDeletePerson() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", 999L);
        parameters.addValue("firstname", "Tester");
        parameters.addValue("lastname", "Tester_ov");
        parameters.addValue("age", 54);

        personRepo.addPerson(parameters);

        Iterable<Person> persons = personRepo.getAllPersons();
        Assertions.assertThat(persons).extracting(Person::getFirstName).contains("Tester");

        personRepo.deletePerson(999L);
        Assertions.assertThatThrownBy(() ->
                personRepo.getPersonById(999L)
            ).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void getPersonById() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", 999L);
        parameters.addValue("firstname", "Tester");
        parameters.addValue("lastname", "Tester_ov");
        parameters.addValue("age", 54);


    }
}