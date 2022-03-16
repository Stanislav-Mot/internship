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
//    List<Task> findByGroupsId(Long id);

//    List<Task> findByPersonsId(Long id);

    @Modifying
    @Query(value = "UPDATE task SET  progress = ? WHERE id = ? AND start_time IS NOT NULL", nativeQuery = true)
    Integer updateProgress(Long id, Integer progress);

    @Modifying
    @Query(value = "UPDATE task SET spent_time = EXTRACT(MINUTES FROM NOW() - start_time) WHERE id = ?", nativeQuery = true)
    Integer setSpentTime(Long id);

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

    @Transactional
    @Modifying
    @Query(value = "UPDATE task SET start_time = NOW()::timestamp(0) WHERE id = ?", nativeQuery = true)
    void setStartTime(Long id);
}
