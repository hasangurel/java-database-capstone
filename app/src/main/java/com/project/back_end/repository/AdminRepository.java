package com.project.back_end.repository;

import com.project.back_end.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Admin entity.
 * Provides database access methods for admin operations.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Find admin by username.
     *
     * @param username the username
     * @return Optional containing the admin if found
     */
    Optional<Admin> findByUsername(String username);

    /**
     * Check if admin exists by username.
     *
     * @param username the username
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);
}
