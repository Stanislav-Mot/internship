package com.internship.internship.repository;

import com.internship.internship.dto.search.SearchPersonDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.internship.internship.util.Helper.newPersonForTest;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql("/schema-for-test.sql")
@Sql("/data-for-person-test.sql")
class PersonRepoTest {

    private final Long ID_FOR_GET = 1L;
    private final Long ID_FOR_UPDATE = 2L;
    private final Long ID_FOR_DELETE = 4L;
    private final Long ID_GROUP_FOR_ADD = 2L;
    private final Long ID_GROUP_FOR_DELETE = 3L;
    @Autowired
    private PersonRepo personRepo;

    private Integer COUNT_PERSONS = 4;

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
    void addPerson() {
        MapSqlParameterSource parameters = getMapSqlParameterSource(newPersonForTest());

        personRepo.addPerson(parameters);
        Iterable<Person> persons = personRepo.getAllPersons();

        Assertions.assertThat(persons).extracting(Person::getFirstName).contains("Tester");

        COUNT_PERSONS += 1;
    }

    @Test
    void updatePerson() {
        LocalDate localDate = LocalDate.of(2012, 12, 12);
        Person personForUpdate = new Person(ID_FOR_UPDATE, "firstNameUpdate", "lastNameUpdate", localDate, null);

        MapSqlParameterSource parameters = getMapSqlParameterSource(personForUpdate);
        Person person = personRepo.updatePerson(parameters);

        Assertions.assertThat(person).returns("firstNameUpdate", from(Person::getFirstName));
        Assertions.assertThat(person).returns("lastNameUpdate", from(Person::getLastName));
    }

    @Test
    void deletePerson() {
        Integer answer = personRepo.deletePerson(ID_FOR_DELETE);

        assertEquals(1, answer);

        Assertions.assertThatThrownBy(() -> personRepo.getPersonById(ID_FOR_DELETE))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void addGroupToPerson() {
        personRepo.addGroupToPerson(ID_FOR_GET, ID_GROUP_FOR_ADD);

        Iterable<Group> groups = personRepo.getGroupsById(ID_FOR_GET);

        Assertions.assertThat(groups).extracting(Group::getName).contains("secondGroup");
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

    @Test
    void search() {
        Person person_one = new Person(null, "SearchTest", "SearchTest", LocalDate.MIN, null);
        Person person_two = new Person(null, "TestSearch", "SearchTest", LocalDate.MIN, null);
        Person person_three = new Person(null, "TestSearch", "SearchTest", LocalDate.MAX, null);

        personRepo.addPerson(getMapSqlParameterSource(person_one));
        personRepo.addPerson(getMapSqlParameterSource(person_two));
        personRepo.addPerson(getMapSqlParameterSource(person_three));

        List<Person> personList = personRepo.search(getMapSqlParameterSource(new SearchPersonDto("SearchTest", null, null, null, null)));

        assertEquals(1, personList.size());

        personList = personRepo.search(getMapSqlParameterSource(new SearchPersonDto(null, null, null, 29, 39)));

        assertEquals(4, personList.size());
    }

    @Test
    void searchByTokenInName() {
        Person personForSearchByToken = newPersonForTest();
        personForSearchByToken.setFirstName("VladIsLove");
        personRepo.addPerson(getMapSqlParameterSource(personForSearchByToken));

        List<Person> personList = personRepo.searchByTokenInName(getMapParamFromToken("IsLo"));

        assertEquals(1, personList.size());
    }

    private MapSqlParameterSource getMapSqlParameterSource(Person person) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("id", person.getId());
        parameters.addValue("firstname", person.getFirstName());
        parameters.addValue("lastname", person.getLastName());
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