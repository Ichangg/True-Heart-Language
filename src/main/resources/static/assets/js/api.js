/* assets/js/api.js */

// Simple API wrapper for the English Center backend.
// Assumes backend provides JWT token on login and expects it in the Authorization header.

const API_BASE = '/api'; // Adjust if context path differs.

function getToken() {
  return localStorage.getItem('token');
}

function setToken(token) {
  localStorage.setItem('token', token);
}

function clearToken() {
  localStorage.removeItem('token');
}

async function request(endpoint, method = 'GET', data = null) {
  const headers = {
    'Content-Type': 'application/json',
  };
  const token = getToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  const options = {
    method,
    headers,
  };
  if (data) {
    options.body = JSON.stringify(data);
  }
  const response = await fetch(`${API_BASE}${endpoint}`, options);
  if (!response.ok) {
    const err = await response.text();
    throw new Error(`API error ${response.status}: ${err}`);
  }
  // If no content, return null
  if (response.status === 204) return null;
  return response.json();
}

// Exported functions for specific resources (examples)
export const api = {
  login: (email, password) => request('/auth/login', 'POST', { email, password }),
  getClasses: () => request('/classes'),
  createClass: (classData) => request('/classes', 'POST', classData),
  getTeachers: () => request('/users/role/teacher'),
  getStudents: () => request('/users/role/student'),
  getFees: () => request('/fee-records'),
  getDashboardStats: (month) => request(`/statistics/financial?month=${month}`),
  recordPayment: (paymentData) => request('/payments', 'POST', paymentData),
  markAttendance: (attendanceData) => request('/attendances/batch', 'POST', attendanceData),
  generateFees: (classId, month) => request(`/fee-records/generate/${classId}?month=${month}`, 'POST')
};

export { getToken, setToken, clearToken };
