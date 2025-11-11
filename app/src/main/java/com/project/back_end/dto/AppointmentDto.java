package com.project.back_end.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating and updating appointments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long id;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDateTime;

    private Integer durationMinutes = 30;

    private String status = "SCHEDULED";

    private String reason;

    private String notes;

    // For response with additional information
    private String doctorName;
    private String patientName;
    private String doctorSpecialty;
}
