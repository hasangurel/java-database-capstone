package com.project.back_end.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor entity representing medical practitioners in the clinic.
 * Doctors have schedules, specialties, and can be booked for appointments.
 */
@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Specialty is required")
    @Column(nullable = false, length = 100)
    @JsonProperty("specialty")
    private String specialty;

    @Email(message = "Email should be valid")
    @Column(unique = true, length = 100)
    @JsonProperty("email")
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    @Column(length = 15)
    @JsonProperty("phone")
    private String phone;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("qualifications")
    private String qualifications;

    @Column(name = "experience_years")
    @JsonProperty("experienceYears")
    private Integer experienceYears;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    @JsonProperty("username")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    @JsonProperty("password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_available_times", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "time_slot")
    @JsonProperty("availableTimes")
    private List<String> availableTimes = new ArrayList<>();

    @Column(name = "consultation_fee")
    @JsonProperty("consultationFee")
    private Double consultationFee;

    @Column(name = "is_active")
    @JsonProperty("isActive")
    private Boolean isActive = true;
}
