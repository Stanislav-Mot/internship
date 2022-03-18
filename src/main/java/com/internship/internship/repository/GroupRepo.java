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

    @Query(value = "select * from groups g left join person_group pg on g.id = pg.group_id where person_id = ?", nativeQuery = true)
    List<Group> findByPersonId(Long id);

//    List<Group> findByAssignments(Assignment assignment);
}
