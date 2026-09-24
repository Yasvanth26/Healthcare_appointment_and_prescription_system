// Healthcare Appointment System - Common Vanilla JS Utilities

// Helper to show inline alerts
function showAlert(elementId, message, type = 'danger') {
    const alertBox = document.getElementById(elementId);
    if (!alertBox) return;

    alertBox.className = `alert alert-${type}`;
    alertBox.textContent = message;
    alertBox.style.display = 'block';

    // Auto hide success alerts after 4 seconds
    if (type === 'success') {
        setTimeout(() => {
            alertBox.style.display = 'none';
        }, 4000);
    }
}

function hideAlert(elementId) {
    const alertBox = document.getElementById(elementId);
    if (alertBox) {
        alertBox.style.display = 'none';
    }
}

// Reusable fetch helper with error handling
async function apiRequest(url, options = {}) {
    const defaultHeaders = {
        'Content-Type': 'application/json'
    };

    options.headers = {
        ...defaultHeaders,
        ...(options.headers || {})
    };

    try {
        const response = await fetch(url, options);

        // Check if response has content
        const contentType = response.headers.get("content-type");
        let data = null;

        if (contentType && contentType.includes("application/json")) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        if (!response.ok) {
            // Handle error response messages cleanly
            let errorMessage = "Request failed with status: " + response.status;
            if (typeof data === 'string' && data.length > 0) {
                errorMessage = data;
            } else if (data && data.message) {
                errorMessage = data.message;
            }
            throw new Error(errorMessage);
        }

        return data;
    } catch (error) {
        console.error("API Error on " + url, error);
        throw error;
    }
}

// Generate Status Badge HTML
function getStatusBadge(status) {
    const s = (status || 'PENDING').toUpperCase();
    let badgeClass = 'badge-pending';

    if (s === 'CONFIRMED') badgeClass = 'badge-confirmed';
    else if (s === 'COMPLETED') badgeClass = 'badge-completed';
    else if (s === 'CANCELLED') badgeClass = 'badge-cancelled';

    return `<span class="badge ${badgeClass}">${s}</span>`;
}
