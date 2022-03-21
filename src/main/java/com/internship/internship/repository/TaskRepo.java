package com.internship.internship.repository;

import com.internship.internship.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query(value = "select * from task", nativeQuery = true)
    List<Task> findAllWithoutConstraint();

    @Query(value = "select * from task left join assignment a on task.id = a.task_id where a.group_id = ?", nativeQuery = true)
    List<Task> findByGroupsId(Long id);

    @Modifying
    @Query(value = "update task set progress = 0 where id in :ids", nativeQuery = true)
    Integer resetProgresses(List<Long> ids);

    @Query(value = "WITH RECURSIVE r AS (" +

            " SELECT a.id, a.children_id" +
            " FROM assignment as a" +
            " where a.group_id = :id and a.children_id is not null" +
            " UNION" +
            " SELECT a2.id, a2.children_id" +
            " FROM assignment as a2" +
            " JOIN r" +
            " ON a2.group_id = r.children_id" +
            " where a2.children_id is not null) " +

            " select distinct(task.*)  from task join assignment a on task.id = a.task_id" +
            " join r on a.group_id in (r.children_id, :id); ", nativeQuery = true)
    List<Task> findByGroupId(Long id);

    @Query(value = "select group_id from assignment where person_id = ?", nativeQuery = true)
    List<Long> findGroupIdByPersonId(Long id);

    @Query(value = "SELECT * FROM task WHERE (cast(:name AS VARCHAR) IS NULL OR task.name = :name) " +

            "AND (cast(:fromStartTime AS TIMESTAMP(0)) IS NULL AND cast(:toStartTime AS TIMESTAMP(0)) IS NULL " +
            "OR task.start_time BETWEEN :fromStartTime::TIMESTAMP and :toStartTime::TIMESTAMP) " +

            "AND (cast(:fromProgress AS INT8) IS NULL AND cast(:toProgress AS INT8) IS NULL " +
            "OR progress BETWEEN :fromProgress AND :toProgress);", nativeQuery = true)
    List<Task> searchCustom(
            @Param("name") String name,
            @Param("fromStartTime") String fromStartTime,
            @Param("toStartTime") String toStartTime,
            @Param("fromProgress") Integer fromProgress,
            @Param("toProgress") Integer toProgress
    );
}
