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
    @Query(value = "DELETE from task where id in :ids " +
            "and (:taskProgress::INT8 IS NULL OR task.progress = :taskProgress)", nativeQuery = true)
    Integer clearTasks(@Param("ids") List<Long> ids, @Param("taskProgress") Long taskProgress);
}
