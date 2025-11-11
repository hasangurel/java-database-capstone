// Header Component

function createHeader() {
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role');

    const headerHTML = `
        <div class="app-header">
            <h1>Smart Clinic Management System</h1>
            <div class="user-info">
                <span>Welcome, <strong>${username}</strong> (${role})</span>
                <button class="btn-logout" onclick="logout()">Logout</button>
            </div>
        </div>
    `;

    const headerElement = document.getElementById('header');
    if (headerElement) {
        headerElement.innerHTML = headerHTML;
    }
}

function logout() {
    localStorage.clear();
    window.location.href = '/';
}

// Initialize header on page load
document.addEventListener('DOMContentLoaded', createHeader);
