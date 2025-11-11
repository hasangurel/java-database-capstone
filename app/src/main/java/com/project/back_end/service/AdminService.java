package com.project.back_end.service;

import com.project.back_end.model.Admin;
import com.project.back_end.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Admin-related business logic.
 * Handles CRUD operations and admin-specific queries.
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Get all admins.
     *
     * @return List of all admins
     */
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    /**
     * Get admin by ID.
     *
     * @param id the admin ID
     * @return Optional containing the admin if found
     */
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * Get admin by username.
     *
     * @param username the username
     * @return Optional containing the admin if found
     */
    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /**
     * Save or update admin.
     *
     * @param admin the admin to save
     * @return saved admin
     */
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    /**
     * Delete admin by ID.
     *
     * @param id the admin ID
     */
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    /**
     * Authenticate admin.
     *
     * @param username the username
     * @param password the password
     * @return Optional containing the admin if authenticated
     */
    public Optional<Admin> authenticate(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            return admin;
        }
        return Optional.empty();
    }

    /**
     * Check if admin exists by username.
     *
     * @param username the username
     * @return true if exists
     */
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }
}
