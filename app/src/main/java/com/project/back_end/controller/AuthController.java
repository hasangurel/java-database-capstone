package com.project.back_end.controller;

import com.project.back_end.dto.LoginRequest;
import com.project.back_end.dto.LoginResponse;
import com.project.back_end.model.Admin;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
import com.project.back_end.service.AdminService;
import com.project.back_end.service.DoctorService;
import com.project.back_end.service.PatientService;
import com.project.back_end.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST Controller for authentication operations.
 * Handles login requests for admins, doctors, and patients.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private TokenService tokenService;

    /**
     * Login endpoint for all user types.
     *
     * @param loginRequest the login credentials
     * @return LoginResponse with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String role = loginRequest.getRole().toUpperCase();

        try {
            switch (role) {
                case "ADMIN":
                    Optional<Admin> admin = adminService.authenticate(username, password);
                    if (admin.isPresent()) {
                        String token = tokenService.generateToken(admin.get().getId(), username, "ADMIN");
                        return ResponseEntity.ok(new LoginResponse(token, "ADMIN", admin.get().getId(), username));
                    }
                    break;

                case "DOCTOR":
                    Optional<Doctor> doctor = doctorService.authenticate(username, password);
                    if (doctor.isPresent()) {
                        String token = tokenService.generateToken(doctor.get().getId(), username, "DOCTOR");
                        return ResponseEntity.ok(new LoginResponse(token, "DOCTOR", doctor.get().getId(), username));
                    }
                    break;

                case "PATIENT":
                    Optional<Patient> patient = patientService.authenticate(username, password);
                    if (patient.isPresent()) {
                        String token = tokenService.generateToken(patient.get().getId(), username, "PATIENT");
                        return ResponseEntity.ok(new LoginResponse(token, "PATIENT", patient.get().getId(), username));
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().body("Invalid role: " + role);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login error: " + e.getMessage());
        }
    }
}
