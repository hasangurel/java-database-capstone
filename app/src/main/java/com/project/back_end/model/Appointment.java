package com.project.back_end.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Appointment entity representing scheduled appointments between doctors and patients.
 * Links a patient with a doctor for a specific date and time.
 */
@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonProperty("doctor")
    private Doctor doctor;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonProperty("patient")
    private Patient patient;

    @NotNull(message = "Appointment date and time is required")
    @Column(name = "appointment_date_time", nullable = false)
    @JsonProperty("appointmentDateTime")
    private LocalDateTime appointmentDateTime;

    @Column(name = "duration_minutes")
    @JsonProperty("durationMinutes")
    private Integer durationMinutes = 30;

    @Column(length = 20)
    @JsonProperty("status")
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED

    @Column(columnDefinition = "TEXT")
    @JsonProperty("reason")
    private String reason;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("notes")
    private String notes;

    @Column(name = "created_at")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Helper method to calculate the end time of the appointment.
     *
     * @return LocalDateTime representing the end time
     */
    public LocalDateTime getEndTime() {
        if (appointmentDateTime != null && durationMinutes != null) {
            return appointmentDateTime.plusMinutes(durationMinutes);
        }
        return null;
    }
}
