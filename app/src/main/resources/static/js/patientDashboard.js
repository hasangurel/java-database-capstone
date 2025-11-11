// Patient Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Check authentication
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    if (!token || role !== 'PATIENT') {
        window.location.href = '/';
        return;
    }

    initializeDashboard();
    setupEventListeners();
});

function setupEventListeners() {
    // Sidebar navigation
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            const section = this.dataset.section;
            switchSection(section);
        });
    });

    // Search and filters
    document.getElementById('doctorSearch').addEventListener('input', filterDoctors);
    document.getElementById('specialtyFilter').addEventListener('change', filterDoctors);

    // Book appointment form
    document.getElementById('bookAppointmentForm').addEventListener('submit', handleBookAppointment);
}

function switchSection(sectionName) {
    // Update navigation
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    document.querySelector(`[data-section="${sectionName}"]`).classList.add('active');

    // Update sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(`${sectionName}-section`).classList.add('active');

    // Load section data
    switch(sectionName) {
        case 'doctors':
            loadDoctors();
            break;
        case 'appointments':
            loadMyAppointments();
            break;
        case 'prescriptions':
            loadMyPrescriptions();
            break;
        case 'profile':
            loadProfile();
            break;
    }
}

let currentDoctors = [];

async function initializeDashboard() {
    await loadDoctors();
}

async function loadDoctors() {
    try {
        currentDoctors = await doctorService.getAllDoctors();
        displayDoctors(currentDoctors);
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

function displayDoctors(doctors) {
    const container = document.getElementById('doctorsList');

    if (doctors.length === 0) {
        container.innerHTML = '<p>No doctors found.</p>';
        return;
    }

    container.innerHTML = doctors.map(doctor => createDoctorCard(doctor, false, true)).join('');
}

async function filterDoctors() {
    const searchTerm = document.getElementById('doctorSearch').value.toLowerCase();
    const specialty = document.getElementById('specialtyFilter').value;

    let filtered = currentDoctors;

    if (searchTerm) {
        filtered = filtered.filter(d => d.name.toLowerCase().includes(searchTerm));
    }

    if (specialty) {
        filtered = filtered.filter(d => d.specialty === specialty);
    }

    displayDoctors(filtered);
}

function bookAppointment(doctorId, doctorName) {
    document.getElementById('selectedDoctorId').value = doctorId;
    document.getElementById('selectedDoctorName').value = doctorName;
    openModal('bookAppointmentModal');
}

async function handleBookAppointment(e) {
    e.preventDefault();

    const patientId = localStorage.getItem('userId');
    const doctorId = document.getElementById('selectedDoctorId').value;
    const dateTime = document.getElementById('appointmentDateTime').value;
    const reason = document.getElementById('reason').value;

    const appointmentData = {
        doctorId: parseInt(doctorId),
        patientId: parseInt(patientId),
        appointmentDateTime: dateTime,
        durationMinutes: 30,
        status: 'SCHEDULED',
        reason: reason
    };

    try {
        await patientService.createAppointment(appointmentData);
        closeModal('bookAppointmentModal');
        document.getElementById('bookAppointmentForm').reset();
        alert('Appointment booked successfully!');
        switchSection('appointments');
    } catch (error) {
        alert('Error booking appointment: ' + error.message);
    }
}

async function loadMyAppointments() {
    const patientId = localStorage.getItem('userId');

    try {
        const appointments = await patientService.getAppointmentsByPatient(patientId);
        displayMyAppointments(appointments);
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

function displayMyAppointments(appointments) {
    const container = document.getElementById('appointmentsList');

    if (appointments.length === 0) {
        container.innerHTML = '<p>No appointments found.</p>';
        return;
    }

    container.innerHTML = appointments.map(apt => `
        <div class="appointment-card">
            <div class="doctor-name">${apt.doctorName}</div>
            <div class="specialty">${apt.doctorSpecialty}</div>
            <div class="datetime">${new Date(apt.appointmentDateTime).toLocaleString()}</div>
            <div class="status ${apt.status.toLowerCase()}">${apt.status}</div>
            ${apt.reason ? `<div class="reason">Reason: ${apt.reason}</div>` : ''}
        </div>
    `).join('');
}

async function loadMyPrescriptions() {
    const patientId = localStorage.getItem('userId');

    try {
        const prescriptions = await patientService.getPrescriptionsByPatient(patientId);
        displayPrescriptions(prescriptions);
    } catch (error) {
        console.error('Error loading prescriptions:', error);
    }
}

function displayPrescriptions(prescriptions) {
    const tbody = document.getElementById('prescriptionsTableBody');

    if (prescriptions.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">No prescriptions found.</td></tr>';
        return;
    }

    tbody.innerHTML = prescriptions.map(presc => `
        <tr>
            <td>${new Date(presc.prescriptionDate).toLocaleDateString()}</td>
            <td>${presc.doctorName}</td>
            <td>${presc.diagnosis}</td>
            <td>${presc.medications ? presc.medications.length : 0} medications</td>
            <td><button class="btn-secondary" onclick="viewPrescription('${presc.id}')">View</button></td>
        </tr>
    `).join('');
}

async function loadProfile() {
    const patientId = localStorage.getItem('userId');

    try {
        const patient = await patientService.getPatientById(patientId);
        if (patient) {
            displayProfile(patient);
        }
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

function displayProfile(patient) {
    const container = document.getElementById('patientProfile');
    container.innerHTML = `
        <h2>${patient.name}</h2>
        <div class="profile-details">
            <div class="detail-item">
                <label>Email</label>
                <div class="value">${patient.email || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Phone</label>
                <div class="value">${patient.phone || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Date of Birth</label>
                <div class="value">${patient.dateOfBirth || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Gender</label>
                <div class="value">${patient.gender || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Blood Group</label>
                <div class="value">${patient.bloodGroup || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Address</label>
                <div class="value">${patient.address || 'N/A'}</div>
            </div>
        </div>
    `;
}
