// Doctor Card Component

function createDoctorCard(doctor, isAdmin = false, isPatient = false) {
    const availableTimes = doctor.availableTimes ? doctor.availableTimes.join(', ') : 'Not available';

    let actionsHTML = '';
    if (isAdmin) {
        actionsHTML = `
            <div class="actions">
                <button class="btn-danger" onclick="deleteDoctor(${doctor.id})">Delete</button>
            </div>
        `;
    } else if (isPatient) {
        actionsHTML = `
            <div class="actions">
                <button class="btn-book" onclick="bookAppointment(${doctor.id}, '${doctor.name}')">Book Appointment</button>
            </div>
        `;
    }

    return `
        <div class="doctor-card">
            <h3>${doctor.name}</h3>
            <div class="specialty">${doctor.specialty}</div>
            <div class="info-row">
                <span class="label">Email:</span> ${doctor.email || 'N/A'}
            </div>
            <div class="info-row">
                <span class="label">Phone:</span> ${doctor.phone || 'N/A'}
            </div>
            <div class="info-row">
                <span class="label">Experience:</span> ${doctor.experienceYears || 0} years
            </div>
            <div class="info-row">
                <span class="label">Fee:</span> $${doctor.consultationFee || 0}
            </div>
            <div class="info-row">
                <span class="label">Available:</span> ${availableTimes}
            </div>
            ${actionsHTML}
        </div>
    `;
}
