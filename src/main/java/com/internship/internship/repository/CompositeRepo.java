package com.internship.internship.repository;

import com.internship.internship.model.Composite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompositeRepo extends JpaRepository<Composite, Long> {
}
