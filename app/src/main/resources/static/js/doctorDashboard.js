// Doctor Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Check authentication
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const userId = localStorage.getItem('userId');

    if (!token || role !== 'DOCTOR') {
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
        case 'appointments':
            loadAppointments();
            break;
        case 'prescriptions':
            loadPrescriptions();
            break;
        case 'profile':
            loadProfile();
            break;
    }
}

async function initializeDashboard() {
    await loadAppointments();
}

async function loadAppointments() {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`http://localhost:8080/api/appointments/doctor/${userId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const appointments = await response.json();
            displayAppointments(appointments);
        }
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('appointmentsList');

    if (appointments.length === 0) {
        container.innerHTML = '<p>No appointments found.</p>';
        return;
    }

    container.innerHTML = appointments.map(apt => `
        <div class="appointment-card ${apt.status.toLowerCase()}">
            <div class="patient-name">${apt.patientName}</div>
            <div class="datetime">${new Date(apt.appointmentDateTime).toLocaleString()}</div>
            <div class="status ${apt.status.toLowerCase()}">${apt.status}</div>
            <div class="reason">${apt.reason || 'No reason specified'}</div>
        </div>
    `).join('');
}

async function loadPrescriptions() {
    const container = document.getElementById('prescriptionsTableBody');
    container.innerHTML = '<tr><td colspan="5">Loading prescriptions...</td></tr>';
}

async function loadProfile() {
    const userId = localStorage.getItem('userId');

    try {
        const doctor = await doctorService.getDoctorById(userId);
        if (doctor) {
            displayProfile(doctor);
        }
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

function displayProfile(doctor) {
    const container = document.getElementById('doctorProfile');
    container.innerHTML = `
        <div class="profile-header">
            <div class="profile-info">
                <h2>${doctor.name}</h2>
                <div class="specialty">${doctor.specialty}</div>
            </div>
        </div>
        <div class="profile-details">
            <div class="detail-item">
                <label>Email</label>
                <div class="value">${doctor.email || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Phone</label>
                <div class="value">${doctor.phone || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Experience</label>
                <div class="value">${doctor.experienceYears || 0} years</div>
            </div>
            <div class="detail-item">
                <label>Qualifications</label>
                <div class="value">${doctor.qualifications || 'N/A'}</div>
            </div>
            <div class="detail-item">
                <label>Consultation Fee</label>
                <div class="value">$${doctor.consultationFee || 0}</div>
            </div>
        </div>
    `;
}
