package com.internship.internship.repository;

import com.internship.internship.dto.search.SearchPersonDto;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql("/schema-for-test.sql")
@Sql("/data-for-person-test.sql")
class PersonRepoTest {

    private final Long ID_FOR_GET = 1L;
    private final Long ID_FOR_UPDATE = 2L;
    private final Long ID_GROUP_FOR_DELETE = 3L;
    private final Integer COUNT_PERSONS = 4;
    @Autowired
    private PersonRepo personRepo;

    @Test
    void getPersonById() {
        Person person = personRepo.getPersonById(ID_FOR_GET);
        Assertions.assertThat(person).returns("GetTester", from(Person::getFirstName));
    }

    @Test
    void getAllPersons() {
        List<Person> persons = personRepo.getAllPersons();
        assertEquals(COUNT_PERSONS, persons.size());
    }

    @Test
    void updatePerson() {
        LocalDate localDate = LocalDate.of(2012, 12, 12);
        Person personForUpdate = new Person(ID_FOR_UPDATE, "firstNameUpdate", "lastNameUpdate", localDate, null, null);

        MapSqlParameterSource parameters = getMapSqlParameterSource(personForUpdate);
        Person person = personRepo.updatePerson(parameters);

        Assertions.assertThat(person).returns("firstNameUpdate", from(Person::getFirstName));
        Assertions.assertThat(person).returns("lastNameUpdate", from(Person::getLastName));
    }

    @Test
    void deleteGroupFromPerson() {
        personRepo.deleteGroupFromPerson(ID_FOR_GET, ID_GROUP_FOR_DELETE);
        List<Group> groups = personRepo.getGroupsById(ID_FOR_GET);
        Assertions.assertThat(groups).extracting(Group::getName).isNotIn("DeleteGroupTester");
    }

    @Test
    void getGroupsById() {
        List<Group> groups = personRepo.getGroupsById(ID_FOR_GET);
        Assertions.assertThat(groups).extracting(Group::getName).contains("testGroup");
    }

    private MapSqlParameterSource getMapSqlParameterSource(Person person) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", person.getId());
        parameters.addValue("first_name", person.getFirstName());
        parameters.addValue("last_name", person.getLastName());
        parameters.addValue("birthdate", person.getBirthdate());
        return parameters;
    }

    private Map<String, Object> getMapParamFromToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", "%" + token + "%");
        return params;
    }

    private MapSqlParameterSource getMapSqlParameterSource(SearchPersonDto searchPersonDto) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("firstName", searchPersonDto.getFirstName());
        mapSqlParameterSource.addValue("lastName", searchPersonDto.getLastName());
        mapSqlParameterSource.addValue("exactAge", searchPersonDto.getExactAge());
        mapSqlParameterSource.addValue("rangeAgeStart", searchPersonDto.getRangeAgeStart());
        mapSqlParameterSource.addValue("rangeAgeEnd", searchPersonDto.getRangeAgeEnd());
        return mapSqlParameterSource;
    }
}