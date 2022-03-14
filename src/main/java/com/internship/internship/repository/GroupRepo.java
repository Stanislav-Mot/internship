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


    List<Group> findByPersonsId(Long id);

    @Query(value = "INSERT into group_task (id_group, id_task) values (?, ?)", nativeQuery = true)
    void addTaskToGroup(Long id, Long taskId);

    @Query(value = "delete from group_task WHERE id_task = ? AND id_group = ?", nativeQuery = true)
    Integer deleteTaskFromGroup(Long taskId, Long groupId);

    @Query(value = "UPDATE groups SET id_parent = ? WHERE id = ?;", nativeQuery = true)
    Integer addGroupToGroup(Long id, Long idGroup);

    @Query(value = "UPDATE groups SET id_parent = NULL WHERE id_parent = ? AND id = ?", nativeQuery = true)
    Integer deleteGroupFromGroup(Long id, Long idGroup);

    List<Group> findByGroupId(Long id);
}
