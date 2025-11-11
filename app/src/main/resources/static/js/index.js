// Login page JavaScript

const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('error-message');

    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        errorMessage.textContent = '';

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;

        if (!role) {
            errorMessage.textContent = 'Please select a role';
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: username,
                    password: password,
                    role: role
                })
            });

            if (response.ok) {
                const data = await response.json();

                // Store token and user info in localStorage
                localStorage.setItem('token', data.token);
                localStorage.setItem('role', data.role);
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('username', data.username);

                // Redirect to appropriate dashboard
                switch(data.role.toUpperCase()) {
                    case 'ADMIN':
                        window.location.href = `/adminDashboard/${data.token}`;
                        break;
                    case 'DOCTOR':
                        window.location.href = `/doctorDashboard/${data.token}`;
                        break;
                    case 'PATIENT':
                        window.location.href = `/patientDashboard/${data.token}`;
                        break;
                    default:
                        errorMessage.textContent = 'Invalid role';
                }
            } else {
                const error = await response.text();
                errorMessage.textContent = error || 'Login failed. Please check your credentials.';
            }
        } catch (error) {
            console.error('Login error:', error);
            errorMessage.textContent = 'An error occurred. Please try again.';
        }
    });
});
