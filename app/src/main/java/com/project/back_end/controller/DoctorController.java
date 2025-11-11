package com.project.back_end.controller;

import com.project.back_end.model.Doctor;
import com.project.back_end.service.DoctorService;
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
 * REST Controller for Doctor operations.
 * Provides CRUD endpoints for managing doctors.
 */
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get all doctors.
     *
     * @return List of all doctors
     */
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get doctor by ID.
     *
     * @param id the doctor ID
     * @return Doctor if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            Optional<Doctor> doctor = doctorService.getDoctorById(id);
            if (doctor.isPresent()) {
                return ResponseEntity.ok(doctor.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Create new doctor (admin only).
     *
     * @param doctor the doctor to create
     * @param token the JWT token
     * @return Created doctor
     */
    @PostMapping
    public ResponseEntity<?> createDoctor(@Valid @RequestBody Doctor doctor,
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

            // Check if username already exists
            if (doctorService.existsByUsername(doctor.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            Doctor savedDoctor = doctorService.saveDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Update doctor.
     *
     * @param id the doctor ID
     * @param doctor the updated doctor data
     * @param token the JWT token
     * @return Updated doctor
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id,
                                          @Valid @RequestBody Doctor doctor,
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

            Optional<Doctor> existingDoctor = doctorService.getDoctorById(id);
            if (existingDoctor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            doctor.setId(id);
            Doctor updatedDoctor = doctorService.saveDoctor(doctor);
            return ResponseEntity.ok(updatedDoctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Delete doctor (admin only).
     *
     * @param id the doctor ID
     * @param token the JWT token
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id,
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

            if (doctorService.getDoctorById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Doctor deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Search doctors by name.
     *
     * @param name the search term
     * @return List of matching doctors
     */
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam String name) {
        try {
            List<Doctor> doctors = doctorService.searchByName(name);
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get doctors by specialty.
     *
     * @param specialty the specialty
     * @return List of doctors
     */
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialty(@PathVariable String specialty) {
        try {
            List<Doctor> doctors = doctorService.findBySpecialty(specialty);
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get doctors by specialty and available time.
     *
     * @param specialty the specialty
     * @param timeSlot the time slot
     * @return List of doctors
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialtyAndTime(
            @RequestParam String specialty,
            @RequestParam String timeSlot) {
        try {
            List<Doctor> doctors = doctorService.findBySpecialtyAndTime(specialty, timeSlot);
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
