package com.internship.internship.repository;

import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.model.Group;
import com.internship.internship.model.Person;
import com.internship.internship.model.search.SearchPerson;
import com.internship.internship.service.PersonService;
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

import java.util.List;

import static com.internship.internship.service.PersonService.getMapSqlParameterSource;
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
    private final Long ID_PERSON_FOR_DELETE_GROUP = 3L;
    private final Long ID_GROUP_FOR_DELETE = 3L;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private GroupRepo groupRepo;

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
        Person personForUpdate = new Person(ID_FOR_UPDATE, "firstNameUpdate", "lastNameUpdate", 34, null);

        MapSqlParameterSource parameters = getMapSqlParameterSource(personForUpdate);

        Integer answer = personRepo.updatePerson(parameters);

        assertEquals(1, answer);

        Person person = personRepo.getPersonById(ID_FOR_UPDATE);

        Assertions.assertThat(person).returns("firstNameUpdate", from(Person::getFirstName));
        Assertions.assertThat(person).returns("lastNameUpdate", from(Person::getLastName));
        Assertions.assertThat(person).returns(34, from(Person::getAge));
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
        Person person = personRepo.getPersonById(ID_FOR_GET);
        Group group = new Group(9999L, "testGroup", null, person);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", group.getId());
        parameters.addValue("name", group.getName());

        groupRepo.addGroup(parameters);

        Integer answer = personRepo.addGroupToPerson(person.getId(), group);
        assertEquals(1, answer);

        Iterable<Group> groups = personRepo.getGroupsById(ID_FOR_GET);

        Assertions.assertThat(groups).extracting(Group::getName).contains("testGroup");
    }

    @Test
    void deleteGroupFromPerson() {

        Integer answer = personRepo.deleteGroupFromPerson(ID_PERSON_FOR_DELETE_GROUP, ID_GROUP_FOR_DELETE);

        assertEquals(1, answer);

        List<Group> groups = personRepo.getGroupsById(ID_GROUP_FOR_DELETE);

        Assertions.assertThat(groups).extracting(Group::getName).isNotIn("DeleteGroupTester");
    }

    @Test
    void getGroupsById() {
        List<Group> groups = personRepo.getGroupsById(ID_FOR_GET);

        Assertions.assertThat(groups).extracting(Group::getName).contains("testGroup");
    }

    @Test
    void search() {
        Person person_one = new Person(23L, "SearchTest", null, 19, null);
        Person person_two = new Person(24L, "TestSearch", null, 29, null);
        Person person_three = new Person(25L, "SearchTest", null, 39, null);

        personRepo.addPerson(getMapSqlParameterSource(person_one));
        personRepo.addPerson(getMapSqlParameterSource(person_two));
        personRepo.addPerson(getMapSqlParameterSource(person_three));

        List<Person> personList = personRepo.search(getMapSqlParameterSource(new SearchPerson("SearchTest", null, 39, null)));

        assertEquals(1, personList.size());

        personList = personRepo.search(getMapSqlParameterSource(new SearchPerson(null, null, 29, 39)));

        assertEquals(2, personList.size());
    }

    @Test
    void searchByTokenInName() {
        Person personForSearchByToken = newPersonForTest();
        personForSearchByToken.setFirstName("VladIsLove");
        personRepo.addPerson(getMapSqlParameterSource(personForSearchByToken));

        List<Person> personList = personRepo.searchByTokenInName(PersonService.getMapParamFromToken("IsLo"));

        assertEquals(1, personList.size());
    }
}