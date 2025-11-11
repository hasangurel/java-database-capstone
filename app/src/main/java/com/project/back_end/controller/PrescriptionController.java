package com.project.back_end.controller;

import com.project.back_end.model.Prescription;
import com.project.back_end.service.PrescriptionService;
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
 * REST Controller for Prescription operations.
 * Provides CRUD endpoints for managing prescriptions (MongoDB).
 */
@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get all prescriptions (admin only).
     *
     * @param token the JWT token
     * @return List of all prescriptions
     */
    @GetMapping
    public ResponseEntity<?> getAllPrescriptions(
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

            List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get prescription by ID.
     *
     * @param id the prescription ID
     * @param token the JWT token
     * @return Prescription if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable String id,
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

            Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
            if (prescription.isPresent()) {
                return ResponseEntity.ok(prescription.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Create new prescription (doctor only).
     *
     * @param prescription the prescription data
     * @param token the JWT token
     * @return Created prescription
     */
    @PostMapping
    public ResponseEntity<?> createPrescription(@Valid @RequestBody Prescription prescription,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate doctor token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateTokenAndRole(jwtToken, "DOCTOR")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Doctor access required");
            }

            Prescription savedPrescription = prescriptionService.savePrescription(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPrescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Update prescription (doctor only).
     *
     * @param id the prescription ID
     * @param prescription the updated prescription data
     * @param token the JWT token
     * @return Updated prescription
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable String id,
                                                 @Valid @RequestBody Prescription prescription,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate doctor token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateTokenAndRole(jwtToken, "DOCTOR")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Doctor access required");
            }

            Optional<Prescription> existingPrescription = prescriptionService.getPrescriptionById(id);
            if (existingPrescription.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found");
            }

            prescription.setId(id);
            Prescription updatedPrescription = prescriptionService.savePrescription(prescription);
            return ResponseEntity.ok(updatedPrescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Delete prescription (doctor only).
     *
     * @param id the prescription ID
     * @param token the JWT token
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable String id,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate doctor token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateTokenAndRole(jwtToken, "DOCTOR")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Doctor access required");
            }

            if (prescriptionService.getPrescriptionById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found");
            }

            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok("Prescription deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get prescriptions by patient ID.
     *
     * @param patientId the patient ID
     * @param token the JWT token
     * @return List of prescriptions
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPrescriptionsByPatientId(@PathVariable Long patientId,
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

            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get prescriptions by doctor ID.
     *
     * @param doctorId the doctor ID
     * @param token the JWT token
     * @return List of prescriptions
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getPrescriptionsByDoctorId(@PathVariable Long doctorId,
                                                         @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate doctor token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateTokenAndRole(jwtToken, "DOCTOR")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Doctor access required");
            }

            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get prescriptions by appointment ID.
     *
     * @param appointmentId the appointment ID
     * @return List of prescriptions
     */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByAppointmentId(@PathVariable Long appointmentId) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByAppointmentId(appointmentId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search prescriptions by patient name.
     *
     * @param patientName the patient name
     * @return List of prescriptions
     */
    @GetMapping("/search")
    public ResponseEntity<List<Prescription>> searchPrescriptions(@RequestParam String patientName) {
        try {
            List<Prescription> prescriptions = prescriptionService.searchByPatientName(patientName);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
