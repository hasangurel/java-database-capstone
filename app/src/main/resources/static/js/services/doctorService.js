// Doctor Service - API interactions for doctors

const API_BASE_URL = 'http://localhost:8080/api';

const doctorService = {
    async getAllDoctors() {
        try {
            const response = await fetch(`${API_BASE_URL}/doctors`);
            if (response.ok) {
                return await response.json();
            }
            throw new Error('Failed to fetch doctors');
        } catch (error) {
            console.error('Error fetching doctors:', error);
            return [];
        }
    },

    async getDoctorById(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/doctors/${id}`);
            if (response.ok) {
                return await response.json();
            }
            return null;
        } catch (error) {
            console.error('Error fetching doctor:', error);
            return null;
        }
    },

    async createDoctor(doctorData) {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${API_BASE_URL}/doctors`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(doctorData)
            });

            if (response.ok) {
                return await response.json();
            }
            const error = await response.text();
            throw new Error(error);
        } catch (error) {
            console.error('Error creating doctor:', error);
            throw error;
        }
    },

    async deleteDoctor(id) {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${API_BASE_URL}/doctors/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            return response.ok;
        } catch (error) {
            console.error('Error deleting doctor:', error);
            return false;
        }
    },

    async searchDoctors(name) {
        try {
            const response = await fetch(`${API_BASE_URL}/doctors/search?name=${encodeURIComponent(name)}`);
            if (response.ok) {
                return await response.json();
            }
            return [];
        } catch (error) {
            console.error('Error searching doctors:', error);
            return [];
        }
    },

    async filterDoctors(specialty, timeSlot) {
        try {
            let url = `${API_BASE_URL}/doctors`;
            if (specialty && timeSlot) {
                url = `${API_BASE_URL}/doctors/filter?specialty=${encodeURIComponent(specialty)}&timeSlot=${encodeURIComponent(timeSlot)}`;
            } else if (specialty) {
                url = `${API_BASE_URL}/doctors/specialty/${encodeURIComponent(specialty)}`;
            }

            const response = await fetch(url);
            if (response.ok) {
                return await response.json();
            }
            return [];
        } catch (error) {
            console.error('Error filtering doctors:', error);
            return [];
        }
    }
};
