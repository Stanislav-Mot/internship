package com.internship.internship.repository;

import com.internship.internship.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {

    @Query(value = "select * from groups g left join assignment a on g.id = a.group_id where a.person_id = ?", nativeQuery = true)
    List<Group> findByPersonId(Long id);

    @Query(value = "select * from groups", nativeQuery = true)
    List<Group> findAllWithoutConstraint();
}
