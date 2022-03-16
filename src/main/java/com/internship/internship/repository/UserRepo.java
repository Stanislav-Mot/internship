package com.internship.internship.repository;

import com.internship.internship.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "insert into user_role (user_id, role) values (:id, :role) ON CONFLICT(user_id, role) DO NOTHING", nativeQuery = true)
    Integer addRole(Long id, String role);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_role where user_id = :id and role = :role", nativeQuery = true)
    Integer deleteRole(Long id, String role);
}
