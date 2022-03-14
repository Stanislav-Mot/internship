package com.internship.internship.repository;

import com.internship.internship.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {

    @Query(value = "SELECT * FROM person WHERE LOWER(CONCAT(first_name, ' ' , last_name)) LIKE LOWER(:token)", nativeQuery = true)
    List<Person> searchByTokenInName(String token);

    @Query(value = "SELECT * FROM person WHERE (:first_name::VARCHAR IS NULL OR person.first_name = :first_name) " +
            "AND (:lastName::VARCHAR IS NULL OR person.last_name = :lastName) " +

            "AND (:exactAge::INT8 IS NULL OR " +
            "extract(YEAR FROM NOW()::date) - extract(YEAR FROM person.birthdate) = :exactAge) " +

            "AND (:rangeAgeStart::INT8 IS NULL AND :rangeAgeEnd::INT8 IS NULL OR " +
            "extract(YEAR FROM NOW()::date) - extract(YEAR FROM person.birthdate) " +
            "BETWEEN :rangeAgeStart AND :rangeAgeEnd)", nativeQuery = true)
    List<Person> searchByToken(
            @Param("first_name") String first_name,
            @Param("lastName") String lastName,
            @Param("exactAge") Integer exactAge,
            @Param("rangeAgeStart") Integer rangeAgeStart,
            @Param("rangeAgeEnd") Integer rangeAgeEnd
    );

    @Query(value = "INSERT INTO person_group (id_person, id_group) VALUES (?, ?) ON CONFLICT DO NOTHING", nativeQuery = true)
    Person addGroupToPerson(Long personId, Long groupId);

    @Query(value = "DELETE FROM person_group WHERE id_person = ? AND id_group = ?", nativeQuery = true)
    Integer deleteGroupFromPerson(Long personId, Long groupId);

    @Query(value = "DELETE from task where " +
            "id in (Select t.id from task t JOIN person_group pg ON t.id_group = pg.id_group where pg.id_person = :clientId) " +
            "and (:taskProgress::INT8 IS NULL OR task.progress = :taskProgress)", nativeQuery = true)
    Integer clearTasks(
            @Param("clientId") Long clientId,
            @Param("taskProgress") Long taskProgress
            );

    @Query(value = "UPDATE task SET progress = 0 where id in " +
            "(Select t.id from task t JOIN person_group pg ON t.id_group = pg.id_group where pg.id_person = ?)", nativeQuery = true)
    Integer resetProgress(Long clientId);
}
