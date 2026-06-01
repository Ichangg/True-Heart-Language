import { api } from './api.js';
import { ui } from './ui.js';

document.addEventListener('DOMContentLoaded', () => {
  // Check auth
  // if (!localStorage.getItem('token')) {
  //   window.location.href = 'login.html';
  //   return;
  // }

  loadDashboardStats();
  loadClasses();
  loadTeachers();
  loadStudents();
  
  // Event listeners for actions
  const addClassBtn = document.getElementById('addClassBtn');
  if(addClassBtn) {
    addClassBtn.addEventListener('click', () => {
      alert('Implement Modal to add class using api.createClass()');
    });
  }
});

async function loadDashboardStats() {
  try {
    const d = new Date();
    const month = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`;
    const stats = await api.getDashboardStats(month);
    
    // In a real scenario, this returns a proper JSON object. 
    // Wait, the API returns { "expectedRevenue": ..., "teacherSalaries": ..., "studentCount": ... }
    
    document.getElementById('revenueMonth').innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(stats.actualRevenue || 0);
  } catch (error) {
    console.error('Failed to load stats', error);
  }
}

async function loadClasses() {
  ui.showLoading('classTableBody');
  try {
    const classes = await api.getClasses();
    const tbody = document.getElementById('classTableBody');
    tbody.innerHTML = '';
    
    if (classes && classes.length > 0) {
      document.getElementById('totalClasses').innerText = classes.length;
      classes.forEach(c => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${c.year}</td>
          <td>Khối ${c.classGroupId}</td>
          <td>${c.name}</td>
          <td>-</td>
          <td>-</td>
          <td>
            <button class="cta-btn" style="padding:4px 8px; font-size:12px;">Edit</button>
            <button class="cta-btn" style="padding:4px 8px; font-size:12px; background: #e74c3c" onclick="generateFees(${c.id})">Gen Fees</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
    } else {
      tbody.innerHTML = '<tr><td colspan="6" style="text-align:center">No classes found</td></tr>';
    }
  } catch (error) {
    ui.showToast('Failed to load classes', 'error');
  }
}

async function loadTeachers() {
  ui.showLoading('teacherTableBody');
  try {
    const teachers = await api.getTeachers();
    const tbody = document.getElementById('teacherTableBody');
    tbody.innerHTML = '';
    
    if (teachers && teachers.length > 0) {
      document.getElementById('totalTeachers').innerText = teachers.length;
      teachers.forEach(t => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${t.fullName}</td>
          <td>${t.email}</td>
          <td>-</td>
          <td><button class="cta-btn" style="padding:4px 8px; font-size:12px;">View</button></td>
        `;
        tbody.appendChild(tr);
      });
    } else {
      tbody.innerHTML = '<tr><td colspan="4" style="text-align:center">No teachers found</td></tr>';
    }
  } catch (error) {
    // API might not exist yet or throws error
    const tbody = document.getElementById('teacherTableBody');
    if(tbody) tbody.innerHTML = '<tr><td colspan="4" style="text-align:center">Failed to load or API missing</td></tr>';
  }
}

async function loadStudents() {
  ui.showLoading('studentTableBody');
  try {
    const students = await api.getStudents();
    const tbody = document.getElementById('studentTableBody');
    tbody.innerHTML = '';
    
    if (students && students.length > 0) {
        document.getElementById('totalStudents').innerText = students.length;
        students.forEach(s => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${s.fullName}</td>
            <td>-</td>
            <td>-</td>
            <td>-</td>
            <td><button class="cta-btn" style="padding:4px 8px; font-size:12px;">View</button></td>
          `;
          tbody.appendChild(tr);
        });
    } else {
        document.getElementById('totalStudents').innerText = 0;
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center">No students found</td></tr>';
    }
  } catch (err) {
    const tbody = document.getElementById('studentTableBody');
    if(tbody) tbody.innerHTML = '<tr><td colspan="5" style="text-align:center">Failed to load students</td></tr>';
  }
}

// Global functions for inline onclick handlers
window.generateFees = async function(classId) {
  try {
    const d = new Date();
    const month = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-01`;
    await api.generateFees(classId, month);
    ui.showToast('Fees generated successfully', 'success');
  } catch (err) {
    ui.showToast('Failed to generate fees: ' + err.message, 'error');
  }
}
