// Patient Service - API interactions for patients

const API_BASE_URL = 'http://localhost:8080/api';

const patientService = {
    async getAllPatients() {
        try {
            const response = await fetch(`${API_BASE_URL}/patients`);
            if (response.ok) {
                return await response.json();
            }
            return [];
        } catch (error) {
            console.error('Error fetching patients:', error);
            return [];
        }
    },

    async getPatientById(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/patients/${id}`);
            if (response.ok) {
                return await response.json();
            }
            return null;
        } catch (error) {
            console.error('Error fetching patient:', error);
            return null;
        }
    },

    async getAppointmentsByPatient(patientId) {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${API_BASE_URL}/appointments/patient/${patientId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                return await response.json();
            }
            return [];
        } catch (error) {
            console.error('Error fetching appointments:', error);
            return [];
        }
    },

    async createAppointment(appointmentData) {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${API_BASE_URL}/appointments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(appointmentData)
            });

            if (response.ok) {
                return await response.json();
            }
            const error = await response.text();
            throw new Error(error);
        } catch (error) {
            console.error('Error creating appointment:', error);
            throw error;
        }
    },

    async getPrescriptionsByPatient(patientId) {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${API_BASE_URL}/prescriptions/patient/${patientId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                return await response.json();
            }
            return [];
        } catch (error) {
            console.error('Error fetching prescriptions:', error);
            return [];
        }
    }
};
