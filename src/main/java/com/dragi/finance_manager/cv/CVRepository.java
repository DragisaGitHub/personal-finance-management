package com.dragi.finance_manager.cv;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CVRepository extends JpaRepository<CV, Long> {
    Optional<CV> findByFullName(String fullName);
    void deleteByFullName(String fullName);
}