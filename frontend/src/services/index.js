import api from './api';

export const authService = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  refreshToken: (refreshToken) => api.post('/auth/refresh-token', { refreshToken }),
  changePassword: (data) => api.put('/auth/change-password', data),
  getProfile: () => api.get('/auth/profile'),
};

export const userService = {
  getAll: (params) => api.get('/users', { params }),
  getById: (id) => api.get(`/users/${id}`),
  create: (data) => api.post('/users', data),
  update: (id, data) => api.put(`/users/${id}`, data),
  toggleStatus: (id) => api.patch(`/users/${id}/status`),
  linkParentStudent: (data) => api.post('/users/link-parent-student', data),
  getParentsByStudent: (studentId) => api.get(`/users/parents/${studentId}`),
  getChildrenByParent: (parentId) => api.get(`/users/children/${parentId}`),
  getMyChildren: () => api.get('/users/my-children'),
};

export const classService = {
  getAll: (params) => api.get('/classes', { params }),
  getById: (id) => api.get(`/classes/${id}`),
  create: (data) => api.post('/classes', data),
  update: (id, data) => api.put(`/classes/${id}`, data),
  close: (id) => api.put(`/classes/${id}/close`),
  reopen: (id) => api.put(`/classes/${id}/reopen`),
  getMyClasses: () => api.get('/classes/my-classes'),
};

export const enrollmentService = {
  enroll: (data) => api.post('/enrollments', data),
  updateDiscount: (id, data) => api.put(`/enrollments/${id}/discount`, data),
  getByClass: (classId) => api.get(`/enrollments/class/${classId}`),
  getByStudent: (studentId) => api.get(`/enrollments/student/${studentId}`),
  getMyClasses: () => api.get('/enrollments/my-classes'),
  withdraw: (id) => api.put(`/enrollments/${id}/withdraw`),
};

export const attendanceService = {
  createSession: (classId, data) => api.post(`/attendance/sessions/${classId}`, data),
  getSessions: (classId) => api.get(`/attendance/sessions/${classId}`),
  markAttendance: (sessionId, data) => api.post(`/attendance/mark/${sessionId}`, data),
  getByClass: (classId) => api.get(`/attendance/class/${classId}`),
  getByStudent: (studentId) => api.get(`/attendance/student/${studentId}`),
  getMyAttendance: () => api.get('/attendance/my-attendance'),
};

export const paymentService = {
  recordPayment: (data) => api.post('/payments', data),
  getByEnrollment: (enrollmentId) => api.get(`/payments/enrollment/${enrollmentId}`),
  getBalance: (enrollmentId) => api.get(`/payments/balance/${enrollmentId}`),
  getFinanceReport: (params) => api.get('/payments/report', { params }),
  recordTeacherPayment: (data) => api.post('/payments/teacher', data),
  getTeacherPayments: (params) => api.get('/payments/teacher', { params }),
};

export const promotionService = {
  getActive: () => api.get('/promotions/active'),
  getAll: (params) => api.get('/promotions', { params }),
  getById: (id) => api.get(`/promotions/${id}`),
  create: (data) => api.post('/promotions', data),
  update: (id, data) => api.put(`/promotions/${id}`, data),
  delete: (id) => api.delete(`/promotions/${id}`),
  toggleActive: (id) => api.patch(`/promotions/${id}/toggle`),
};

export const messageService = {
  send: (data) => api.post('/messages/send', data),
  sendAbsenceReminder: (data) => api.post('/messages/absence-reminder', data),
  sendEmergency: (data) => api.post('/messages/emergency', data),
  getHistory: (params) => api.get('/messages/history', { params }),
};

export const statisticService = {
  getDashboard: () => api.get('/statistics/dashboard'),
  getStudentTrend: (params) => api.get('/statistics/student-trend', { params }),
  getTopClasses: (params) => api.get('/statistics/top-classes', { params }),
};
