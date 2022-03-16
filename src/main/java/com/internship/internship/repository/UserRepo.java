package com.internship.internship.repository;

import com.internship.internship.model.Role;
import com.internship.internship.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Transactional

    @Modifying
    @Query(value = "insert into user_role (user_id, role) values (?, ?) ON CONFLICT(user_id, role) DO NOTHING", nativeQuery = true)
    Integer addRole(Long id,  Set<Role> roles);

//    @Transactional

//    @Modifying
//    @Query(value = "DELETE FROM user_role where user_id = ? and role in :name ?", nativeQuery = true)
//    Integer deleteUserRo(Long id, Set<Role> name);
}
