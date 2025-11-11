-- Smart Clinic Management System - Stored Procedures
-- This script creates stored procedures for reporting and analytics

USE cms;

DELIMITER //

-- 1. Get Daily Appointment Report by Doctor
-- Returns appointments for a specific doctor on a specific date
DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor//
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN doctorId BIGINT,
    IN reportDate DATE
)
BEGIN
    SELECT
        a.id AS appointment_id,
        d.name AS doctor_name,
        p.name AS patient_name,
        a.appointment_date_time,
        a.duration_minutes,
        a.status,
        a.reason
    FROM appointment a
    INNER JOIN doctor d ON a.doctor_id = d.id
    INNER JOIN patient p ON a.patient_id = p.id
    WHERE a.doctor_id = doctorId
    AND DATE(a.appointment_date_time) = reportDate
    ORDER BY a.appointment_date_time;
END//

-- 2. Get Doctor with Most Patients by Month
-- Returns the doctor who saw the most patients in a given month
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth//
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN inputYear INT,
    IN inputMonth INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialty,
        COUNT(DISTINCT a.patient_id) AS unique_patients,
        COUNT(a.id) AS total_appointments
    FROM doctor d
    INNER JOIN appointment a ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_date_time) = inputYear
    AND MONTH(a.appointment_date_time) = inputMonth
    GROUP BY d.id, d.name, d.specialty
    ORDER BY unique_patients DESC, total_appointments DESC
    LIMIT 1;
END//

-- 3. Get Doctor with Most Patients by Year
-- Returns the doctor who saw the most patients in a given year
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear//
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN inputYear INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialty,
        COUNT(DISTINCT a.patient_id) AS unique_patients,
        COUNT(a.id) AS total_appointments
    FROM doctor d
    INNER JOIN appointment a ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_date_time) = inputYear
    GROUP BY d.id, d.name, d.specialty
    ORDER BY unique_patients DESC, total_appointments DESC
    LIMIT 1;
END//

-- 4. Get Monthly Revenue Report
-- Returns monthly revenue based on completed appointments
DROP PROCEDURE IF EXISTS GetMonthlyRevenueReport//
CREATE PROCEDURE GetMonthlyRevenueReport(
    IN inputYear INT,
    IN inputMonth INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialty,
        COUNT(a.id) AS completed_appointments,
        d.consultation_fee,
        (COUNT(a.id) * d.consultation_fee) AS total_revenue
    FROM doctor d
    INNER JOIN appointment a ON d.id = a.doctor_id
    WHERE a.status = 'COMPLETED'
    AND YEAR(a.appointment_date_time) = inputYear
    AND MONTH(a.appointment_date_time) = inputMonth
    GROUP BY d.id, d.name, d.specialty, d.consultation_fee
    ORDER BY total_revenue DESC;
END//

-- 5. Get Patient Appointment History
-- Returns full appointment history for a specific patient
DROP PROCEDURE IF EXISTS GetPatientAppointmentHistory//
CREATE PROCEDURE GetPatientAppointmentHistory(
    IN patientId BIGINT
)
BEGIN
    SELECT
        a.id AS appointment_id,
        d.name AS doctor_name,
        d.specialty,
        a.appointment_date_time,
        a.duration_minutes,
        a.status,
        a.reason,
        a.notes
    FROM appointment a
    INNER JOIN doctor d ON a.doctor_id = d.id
    WHERE a.patient_id = patientId
    ORDER BY a.appointment_date_time DESC;
END//

DELIMITER ;

-- Test the stored procedures with sample data
-- Uncomment to test:

-- Test 1: Daily report for doctor 1 on 2025-01-15
-- CALL GetDailyAppointmentReportByDoctor(1, '2025-01-15');

-- Test 2: Doctor with most patients in January 2025
-- CALL GetDoctorWithMostPatientsByMonth(2025, 1);

-- Test 3: Doctor with most patients in 2025
-- CALL GetDoctorWithMostPatientsByYear(2025);

-- Test 4: Monthly revenue for January 2025
-- CALL GetMonthlyRevenueReport(2025, 1);

-- Test 5: Appointment history for patient 1
-- CALL GetPatientAppointmentHistory(1);
