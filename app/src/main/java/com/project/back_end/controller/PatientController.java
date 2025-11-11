package com.project.back_end.controller;

import com.project.back_end.model.Patient;
import com.project.back_end.service.PatientService;
import com.project.back_end.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Patient operations.
 * Provides CRUD endpoints for managing patients.
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get all patients.
     *
     * @return List of all patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get patient by ID.
     *
     * @param id the patient ID
     * @return Patient if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Optional<Patient> patient = patientService.getPatientById(id);
            if (patient.isPresent()) {
                return ResponseEntity.ok(patient.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Register new patient.
     *
     * @param patient the patient to register
     * @return Created patient
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody Patient patient) {
        try {
            // Check if username or email already exists
            if (patientService.existsByUsername(patient.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            if (patientService.existsByEmail(patient.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Create new patient (admin only).
     *
     * @param patient the patient to create
     * @param token the JWT token
     * @return Created patient
     */
    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient,
                                           @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate admin token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateTokenAndRole(jwtToken, "ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
            }

            // Check if username or email already exists
            if (patientService.existsByUsername(patient.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            if (patientService.existsByEmail(patient.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Update patient.
     *
     * @param id the patient ID
     * @param patient the updated patient data
     * @param token the JWT token
     * @return Updated patient
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id,
                                           @Valid @RequestBody Patient patient,
                                           @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            Optional<Patient> existingPatient = patientService.getPatientById(id);
            if (existingPatient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
            }

            patient.setId(id);
            Patient updatedPatient = patientService.savePatient(patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Delete patient (admin only).
     *
     * @param id the patient ID
     * @param token the JWT token
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id,
                                           @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate admin token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateTokenAndRole(jwtToken, "ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
            }

            if (patientService.getPatientById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
            }

            patientService.deletePatient(id);
            return ResponseEntity.ok("Patient deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Search patients by name.
     *
     * @param name the search term
     * @return List of matching patients
     */
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String name) {
        try {
            List<Patient> patients = patientService.searchByName(name);
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
