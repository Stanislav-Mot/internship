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

    @Query(value = "select id, name from groups", nativeQuery = true)
    List<GroupView> findAllWithoutConstraint();

    @Query(value = "select * from groups where id in :ids", nativeQuery = true)
    List<GroupView> findAllWithoutConstraintByIds(List<Long> ids);

    @Query(value = "WITH RECURSIVE r AS (" +
            "" +
            "    SELECT a.id, a.children_id" +
            "    FROM assignment as a" +
            "    where a.group_id = ?" +
            "      and a.children_id is not null" +

            "    UNION" +

            "    SELECT a2.id, a2.children_id" +
            "    FROM assignment as a2" +
            "             JOIN r" +
            "                  ON a2.group_id = r.children_id" +
            "    where a2.children_id is not null)" +
            "" +
            "select children_id from r;", nativeQuery = true)
    List<Long> getNextGeneration(Long groupId);

    interface GroupView {
        Long getId();

        String getName();
    }
}
