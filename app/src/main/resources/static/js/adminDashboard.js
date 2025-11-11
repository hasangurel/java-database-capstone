// Admin Dashboard JavaScript

let currentDoctors = [];

document.addEventListener('DOMContentLoaded', function() {
    // Check authentication
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    if (!token || role !== 'ADMIN') {
        window.location.href = '/';
        return;
    }

    initializeDashboard();
    setupEventListeners();
});

async function initializeDashboard() {
    await loadDoctors();
    await loadStatistics();
}

function setupEventListeners() {
    // Sidebar navigation
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            const section = this.dataset.section;
            switchSection(section);
        });
    });

    // Add doctor button
    document.getElementById('addDoctorBtn').addEventListener('click', function() {
        openModal('addDoctorModal');
    });

    // Add doctor form
    document.getElementById('addDoctorForm').addEventListener('submit', handleAddDoctor);

    // Search and filters
    document.getElementById('doctorSearch').addEventListener('input', filterDoctors);
    document.getElementById('specialtyFilter').addEventListener('change', filterDoctors);
    document.getElementById('timeFilter').addEventListener('change', filterDoctors);
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
        case 'patients':
            loadPatients();
            break;
        case 'appointments':
            loadAppointments();
            break;
        case 'reports':
            loadStatistics();
            break;
    }
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

    container.innerHTML = doctors.map(doctor => createDoctorCard(doctor, true, false)).join('');
}

async function handleAddDoctor(e) {
    e.preventDefault();

    const doctorData = {
        name: document.getElementById('doctorName').value,
        specialty: document.getElementById('doctorSpecialty').value,
        email: document.getElementById('doctorEmail').value,
        phone: document.getElementById('doctorPhone').value,
        username: document.getElementById('doctorUsername').value,
        password: document.getElementById('doctorPassword').value,
        qualifications: document.getElementById('doctorQualifications').value,
        experienceYears: parseInt(document.getElementById('doctorExperience').value) || 0,
        consultationFee: parseFloat(document.getElementById('doctorFee').value) || 0,
        availableTimes: [],
        isActive: true
    };

    try {
        await doctorService.createDoctor(doctorData);
        closeModal('addDoctorModal');
        document.getElementById('addDoctorForm').reset();
        await loadDoctors();
        alert('Doctor added successfully!');
    } catch (error) {
        alert('Error adding doctor: ' + error.message);
    }
}

async function deleteDoctor(id) {
    if (confirm('Are you sure you want to delete this doctor?')) {
        try {
            const success = await doctorService.deleteDoctor(id);
            if (success) {
                await loadDoctors();
                alert('Doctor deleted successfully!');
            } else {
                alert('Failed to delete doctor');
            }
        } catch (error) {
            alert('Error deleting doctor: ' + error.message);
        }
    }
}

async function filterDoctors() {
    const searchTerm = document.getElementById('doctorSearch').value.toLowerCase();
    const specialty = document.getElementById('specialtyFilter').value;
    const timeSlot = document.getElementById('timeFilter').value;

    let filtered = currentDoctors;

    if (searchTerm) {
        filtered = filtered.filter(d => d.name.toLowerCase().includes(searchTerm));
    }

    if (specialty) {
        filtered = filtered.filter(d => d.specialty === specialty);
    }

    if (timeSlot) {
        filtered = filtered.filter(d => d.availableTimes && d.availableTimes.includes(timeSlot));
    }

    displayDoctors(filtered);
}

async function loadPatients() {
    try {
        const patients = await patientService.getAllPatients();
        displayPatients(patients);
    } catch (error) {
        console.error('Error loading patients:', error);
    }
}

function displayPatients(patients) {
    const tbody = document.getElementById('patientsTableBody');
    if (patients.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">No patients found.</td></tr>';
        return;
    }

    tbody.innerHTML = patients.map(patient => `
        <tr>
            <td>${patient.id}</td>
            <td>${patient.name}</td>
            <td>${patient.email}</td>
            <td>${patient.phone || 'N/A'}</td>
            <td>${patient.bloodGroup || 'N/A'}</td>
            <td>
                <button class="btn-secondary" onclick="viewPatient(${patient.id})">View</button>
            </td>
        </tr>
    `).join('');
}

async function loadAppointments() {
    // Simplified - would need admin endpoint
    const tbody = document.getElementById('appointmentsTableBody');
    tbody.innerHTML = '<tr><td colspan="6">Loading appointments...</td></tr>';
}

async function loadStatistics() {
    try {
        const doctors = await doctorService.getAllDoctors();
        const patients = await patientService.getAllPatients();

        document.getElementById('totalDoctors').textContent = doctors.length;
        document.getElementById('totalPatients').textContent = patients.length;
        document.getElementById('totalAppointments').textContent = '0';
        document.getElementById('completedAppointments').textContent = '0';
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}
