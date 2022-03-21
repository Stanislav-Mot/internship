package com.internship.internship.repository;

import com.internship.internship.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query(value = "select * from task left join group_task gt on task.id = gt.task_id where group_id = ?", nativeQuery = true)
    List<Task> findByGroupsId(Long id);

    @Query(value = "select * from task left join group_task gt on task.id = gt.task_id " +
            "left join person_group pg on gt.group_id = pg.group_id where pg.person_id = ?", nativeQuery = true)
    List<Task> findByPersonsId(Long id);

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
