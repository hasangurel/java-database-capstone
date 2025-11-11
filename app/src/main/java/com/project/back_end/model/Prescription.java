package com.project.back_end.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Prescription document stored in MongoDB.
 * Contains flexible, unstructured data about medications prescribed to patients.
 */
@Document(collection = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

    @Id
    @JsonProperty("id")
    private String id;

    @NotNull(message = "Appointment ID is required")
    @Field("appointment_id")
    @JsonProperty("appointmentId")
    private Long appointmentId;

    @NotNull(message = "Patient ID is required")
    @Field("patient_id")
    @JsonProperty("patientId")
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    @Field("patient_name")
    @JsonProperty("patientName")
    private String patientName;

    @NotNull(message = "Doctor ID is required")
    @Field("doctor_id")
    @JsonProperty("doctorId")
    private Long doctorId;

    @NotBlank(message = "Doctor name is required")
    @Field("doctor_name")
    @JsonProperty("doctorName")
    private String doctorName;

    @Field("prescription_date")
    @JsonProperty("prescriptionDate")
    private LocalDateTime prescriptionDate = LocalDateTime.now();

    @Field("diagnosis")
    @JsonProperty("diagnosis")
    private String diagnosis;

    @Field("medications")
    @JsonProperty("medications")
    private List<Medication> medications = new ArrayList<>();

    @Field("instructions")
    @JsonProperty("instructions")
    private String instructions;

    @Field("follow_up_date")
    @JsonProperty("followUpDate")
    private LocalDateTime followUpDate;

    @Field("notes")
    @JsonProperty("notes")
    private String notes;

    /**
     * Inner class representing a medication item in the prescription.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Medication {
        @JsonProperty("name")
        private String name;

        @JsonProperty("dosage")
        private String dosage;

        @JsonProperty("frequency")
        private String frequency;

        @JsonProperty("duration")
        private String duration;

        @JsonProperty("instructions")
        private String instructions;
    }
}
