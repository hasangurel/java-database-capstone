package com.project.back_end.controller;

import com.project.back_end.dto.AppointmentDto;
import com.project.back_end.model.Appointment;
import com.project.back_end.service.AppointmentService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Appointment operations.
 * Provides CRUD endpoints for managing appointments.
 */
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get all appointments.
     *
     * @param token the JWT token
     * @return List of all appointments
     */
    @GetMapping
    public ResponseEntity<?> getAllAppointments(
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

            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get appointment by ID.
     *
     * @param id the appointment ID
     * @param token the JWT token
     * @return Appointment if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id,
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

            Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
            if (appointment.isPresent()) {
                return ResponseEntity.ok(appointment.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Create new appointment.
     *
     * @param appointmentDto the appointment data
     * @param token the JWT token
     * @return Created appointment
     */
    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto,
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

            Appointment appointment = appointmentService.createAppointment(appointmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Update appointment.
     *
     * @param id the appointment ID
     * @param appointment the updated appointment data
     * @param token the JWT token
     * @return Updated appointment
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id,
                                                @Valid @RequestBody Appointment appointment,
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

            Optional<Appointment> existingAppointment = appointmentService.getAppointmentById(id);
            if (existingAppointment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
            }

            appointment.setId(id);
            Appointment updatedAppointment = appointmentService.saveAppointment(appointment);
            return ResponseEntity.ok(updatedAppointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Delete appointment.
     *
     * @param id the appointment ID
     * @param token the JWT token
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id,
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

            if (appointmentService.getAppointmentById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
            }

            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok("Appointment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get appointments by doctor ID.
     *
     * @param doctorId the doctor ID
     * @param token the JWT token
     * @return List of appointments
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctor(@PathVariable Long doctorId,
                                                      @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate doctor token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            List<AppointmentDto> appointments = appointmentService.getAppointmentsByDoctorId(doctorId)
                    .stream()
                    .map(appointmentService::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get appointments by patient ID.
     *
     * @param patientId the patient ID
     * @param token the JWT token
     * @return List of appointments
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentsByPatient(@PathVariable Long patientId,
                                                       @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate patient token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            String jwtToken = token.substring(7);
            if (!tokenService.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            List<AppointmentDto> appointments = appointmentService.getAppointmentsByPatientId(patientId)
                    .stream()
                    .map(appointmentService::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get appointments by status.
     *
     * @param status the status
     * @return List of appointments
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable String status) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
