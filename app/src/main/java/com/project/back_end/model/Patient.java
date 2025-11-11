package com.project.back_end.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Patient entity representing patients in the clinic.
 * Patients can book appointments and have medical history.
 */
@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Patient name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    @JsonProperty("name")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true, length = 100)
    @JsonProperty("email")
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    @Column(length = 15)
    @JsonProperty("phone")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth")
    @JsonProperty("dateOfBirth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    @JsonProperty("gender")
    private String gender;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("address")
    private String address;

    @Column(name = "blood_group", length = 5)
    @JsonProperty("bloodGroup")
    private String bloodGroup;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("medicalHistory")
    private String medicalHistory;

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

    @Column(name = "emergency_contact", length = 15)
    @JsonProperty("emergencyContact")
    private String emergencyContact;

    @Column(name = "is_active")
    @JsonProperty("isActive")
    private Boolean isActive = true;
}
