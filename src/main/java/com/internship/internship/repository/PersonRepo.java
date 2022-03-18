package com.internship.internship.repository;

import com.internship.internship.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {

    @Query(value = "SELECT * FROM person WHERE LOWER(CONCAT(first_name, ' ' , last_name)) LIKE LOWER(:token)", nativeQuery = true)
    List<Person> searchByTokenInName(String token);

    @Query(value = "SELECT * FROM person WHERE (:firstName::VARCHAR IS NULL OR person.first_name = :firstName) " +
            "AND (:lastName::VARCHAR IS NULL OR person.last_name = :lastName) " +

            "AND (:exactAge::INT8 IS NULL OR " +
            "extract(YEAR FROM NOW()::date) - extract(YEAR FROM person.birthdate) = :exactAge) " +

            "AND (:rangeAgeStart::INT8 IS NULL AND :rangeAgeEnd::INT8 IS NULL OR " +
            "extract(YEAR FROM NOW()::date) - extract(YEAR FROM person.birthdate) " +
            "BETWEEN :rangeAgeStart AND :rangeAgeEnd)", nativeQuery = true)
    List<Person> searchByToken(
            String firstName,
            String lastName,
            Integer exactAge,
            Integer rangeAgeStart,
            Integer rangeAgeEnd);

    @Modifying
    @Query(value = "DELETE from task where " +
            "id in (Select t.id from task t JOIN person_group pg ON t.id_group = pg.id_group where pg.id_person = :clientId) " +
            "and (:taskProgress::INT8 IS NULL OR task.progress = :taskProgress)", nativeQuery = true)
    Integer clearTasks(
            @Param("clientId") Long clientId,
            @Param("taskProgress") Long taskProgress
    );

    @Modifying
    @Query(value = "UPDATE task SET progress = 0 where id in " +
            "(Select t.id from task t JOIN person_group pg ON t.id_group = pg.id_group where pg.id_person = ?)", nativeQuery = true)
    Integer resetProgress(Long clientId);
}
