-- Smart Clinic Management System - Sample Data
-- This script inserts sample data for testing and demonstration

USE cms;

-- Insert Admin Data
INSERT INTO admin (username, password, full_name, email) VALUES
('admin1', 'admin123', 'John Admin', 'admin@smartclinic.com'),
('admin2', 'admin456', 'Sarah Admin', 'sarah.admin@smartclinic.com');

-- Insert Doctor Data
INSERT INTO doctor (name, specialty, email, phone, qualifications, experience_years, username, password, consultation_fee, is_active) VALUES
('Dr. Michael Smith', 'Cardiology', 'michael.smith@smartclinic.com', '1234567890', 'MD, FACC', 15, 'drsmith', 'doctor123', 150.00, 1),
('Dr. Emily Johnson', 'Pediatrics', 'emily.johnson@smartclinic.com', '1234567891', 'MD, FAAP', 10, 'drjohnson', 'doctor123', 100.00, 1),
('Dr. David Williams', 'Orthopedics', 'david.williams@smartclinic.com', '1234567892', 'MD, FAAOS', 12, 'drwilliams', 'doctor123', 120.00, 1),
('Dr. Sarah Brown', 'Dermatology', 'sarah.brown@smartclinic.com', '1234567893', 'MD, FAAD', 8, 'drbrown', 'doctor123', 110.00, 1),
('Dr. James Davis', 'Neurology', 'james.davis@smartclinic.com', '1234567894', 'MD, FAAN', 20, 'drdavis', 'doctor123', 180.00, 1),
('Dr. Lisa Miller', 'Gynecology', 'lisa.miller@smartclinic.com', '1234567895', 'MD, FACOG', 14, 'drmiller', 'doctor123', 130.00, 1),
('Dr. Robert Wilson', 'General Medicine', 'robert.wilson@smartclinic.com', '1234567896', 'MBBS, MD', 7, 'drwilson', 'doctor123', 80.00, 1),
('Dr. Jennifer Moore', 'Psychiatry', 'jennifer.moore@smartclinic.com', '1234567897', 'MD, Psychiatry', 11, 'drmoore', 'doctor123', 140.00, 1);

-- Insert Doctor Available Times
INSERT INTO doctor_available_times (doctor_id, time_slot) VALUES
(1, '09:00-10:00'), (1, '10:00-11:00'), (1, '14:00-15:00'), (1, '15:00-16:00'),
(2, '08:00-09:00'), (2, '09:00-10:00'), (2, '11:00-12:00'), (2, '13:00-14:00'),
(3, '10:00-11:00'), (3, '11:00-12:00'), (3, '15:00-16:00'), (3, '16:00-17:00'),
(4, '09:00-10:00'), (4, '10:00-11:00'), (4, '14:00-15:00'), (4, '15:00-16:00'),
(5, '08:00-09:00'), (5, '11:00-12:00'), (5, '14:00-15:00'), (5, '16:00-17:00'),
(6, '09:00-10:00'), (6, '10:00-11:00'), (6, '13:00-14:00'), (6, '14:00-15:00'),
(7, '08:00-09:00'), (7, '09:00-10:00'), (7, '10:00-11:00'), (7, '11:00-12:00'),
(8, '10:00-11:00'), (8, '14:00-15:00'), (8, '15:00-16:00'), (8, '16:00-17:00');

-- Insert Patient Data
INSERT INTO patient (name, email, phone, date_of_birth, gender, address, blood_group, medical_history, username, password, emergency_contact, is_active) VALUES
('Alice Anderson', 'alice.anderson@email.com', '9876543210', '1990-05-15', 'Female', '123 Main St, City', 'O+', 'No known allergies', 'alice', 'patient123', '9876543211', 1),
('Bob Baker', 'bob.baker@email.com', '9876543212', '1985-08-20', 'Male', '456 Oak Ave, City', 'A+', 'Diabetic', 'bob', 'patient123', '9876543213', 1),
('Carol Clark', 'carol.clark@email.com', '9876543214', '1992-03-10', 'Female', '789 Pine Rd, City', 'B+', 'Asthma', 'carol', 'patient123', '9876543215', 1),
('David Dixon', 'david.dixon@email.com', '9876543216', '1988-11-25', 'Male', '321 Elm St, City', 'AB+', 'Hypertension', 'david', 'patient123', '9876543217', 1),
('Emma Evans', 'emma.evans@email.com', '9876543218', '1995-07-30', 'Female', '654 Maple Dr, City', 'O-', 'No known issues', 'emma', 'patient123', '9876543219', 1),
('Frank Foster', 'frank.foster@email.com', '9876543220', '1980-02-14', 'Male', '987 Cedar Ln, City', 'A-', 'Heart disease', 'frank', 'patient123', '9876543221', 1),
('Grace Green', 'grace.green@email.com', '9876543222', '1993-09-05', 'Female', '147 Birch St, City', 'B-', 'Allergies to penicillin', 'grace', 'patient123', '9876543223', 1),
('Henry Harris', 'henry.harris@email.com', '9876543224', '1987-12-18', 'Male', '258 Spruce Ave, City', 'AB-', 'Back pain', 'henry', 'patient123', '9876543225', 1),
('Iris Irving', 'iris.irving@email.com', '9876543226', '1991-04-22', 'Female', '369 Willow Rd, City', 'O+', 'Migraine', 'iris', 'patient123', '9876543227', 1),
('Jack Johnson', 'jack.johnson@email.com', '9876543228', '1984-06-08', 'Male', '741 Ash Dr, City', 'A+', 'No known issues', 'jack', 'patient123', '9876543229', 1);

-- Insert Appointment Data
INSERT INTO appointment (doctor_id, patient_id, appointment_date_time, duration_minutes, status, reason, notes, created_at) VALUES
(1, 1, '2025-01-15 09:00:00', 30, 'COMPLETED', 'Chest pain checkup', 'ECG done, normal results', '2025-01-10 10:00:00'),
(1, 2, '2025-01-15 10:00:00', 30, 'COMPLETED', 'Follow-up consultation', 'Blood pressure stable', '2025-01-10 11:00:00'),
(2, 3, '2025-01-16 08:00:00', 30, 'COMPLETED', 'Child vaccination', 'Vaccines administered', '2025-01-11 09:00:00'),
(2, 5, '2025-01-16 09:00:00', 30, 'SCHEDULED', 'Regular checkup', 'First visit', '2025-01-11 10:00:00'),
(3, 4, '2025-01-17 10:00:00', 45, 'COMPLETED', 'Knee pain', 'X-ray ordered', '2025-01-12 08:00:00'),
(3, 8, '2025-01-17 11:00:00', 45, 'SCHEDULED', 'Back pain', 'MRI recommended', '2025-01-12 09:00:00'),
(4, 7, '2025-01-18 09:00:00', 30, 'COMPLETED', 'Skin rash', 'Prescribed ointment', '2025-01-13 10:00:00'),
(5, 6, '2025-01-18 14:00:00', 45, 'COMPLETED', 'Headache and dizziness', 'CT scan done', '2025-01-13 11:00:00'),
(6, 9, '2025-01-19 09:00:00', 30, 'SCHEDULED', 'Pregnancy checkup', 'Routine examination', '2025-01-14 08:00:00'),
(7, 10, '2025-01-19 08:00:00', 30, 'COMPLETED', 'Fever and cold', 'Rest recommended', '2025-01-14 09:00:00'),
(1, 3, '2025-02-10 14:00:00', 30, 'COMPLETED', 'Heart checkup', 'All clear', '2025-02-05 10:00:00'),
(2, 1, '2025-02-15 11:00:00', 30, 'COMPLETED', 'Child health', 'Healthy', '2025-02-10 11:00:00'),
(3, 2, '2025-02-20 15:00:00', 45, 'COMPLETED', 'Joint pain', 'Physical therapy advised', '2025-02-15 09:00:00'),
(4, 4, '2025-03-05 10:00:00', 30, 'SCHEDULED', 'Skin checkup', 'Follow-up needed', '2025-03-01 10:00:00'),
(5, 6, '2025-03-10 16:00:00', 45, 'SCHEDULED', 'Neurological exam', 'Assessment pending', '2025-03-05 11:00:00');
