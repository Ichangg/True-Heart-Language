// import { api } from './api.js';
// import { ui } from './ui.js';

document.addEventListener('DOMContentLoaded', () => {
  initNavigation();
  initCharts();
  loadMockData();
});

function initNavigation() {
  const links = document.querySelectorAll('.sidebar-menu a');
  const sections = document.querySelectorAll('.page-section');
  const title = document.getElementById('topbar-title');

  links.forEach(link => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      
      // Update active link
      links.forEach(l => l.classList.remove('active'));
      link.classList.add('active');

      // Update active section
      const targetId = link.getAttribute('href').substring(1);
      sections.forEach(sec => {
        sec.classList.remove('active');
        if(sec.id === targetId) sec.classList.add('active');
      });

      // Update Title
      title.innerText = link.innerText.split(' ').slice(1).join(' ');
    });
  });
}

// Global Modal Functions
window.openModal = function(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add('show');
}

// Global function for wizard steps
window.nextWizardStep = function(stepNum) {
  const steps = document.querySelectorAll('.wizard-step');
  const panels = document.querySelectorAll('.wizard-panel');
  
  steps.forEach((s, index) => {
    if (index < stepNum) {
      s.classList.add('active');
    } else {
      s.classList.remove('active');
    }
  });

  panels.forEach(p => p.classList.remove('active'));
  const target = document.getElementById('step' + stepNum);
  if (target) target.classList.add('active');
}

window.closeModal = function(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove('show');
}

// Close modal when clicking outside
window.onclick = function(event) {
  if (event.target.classList.contains('modal-overlay')) {
    event.target.classList.remove('show');
  }
}

function initCharts() {
  // Chart.js Configuration
  Chart.defaults.color = '#8b949e';
  Chart.defaults.font.family = "'Be Vietnam Pro', sans-serif";

  // Finance Chart
  const ctxFinance = document.getElementById('financeChart');
  if (ctxFinance) {
    new Chart(ctxFinance, {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          label: 'Thực thu',
          data: [45, 52, 58, 49, 65, 80],
          borderColor: '#2f81f7',
          backgroundColor: 'rgba(47,129,247,0.1)',
          borderWidth: 2,
          fill: true,
          tension: 0.4
        },
        {
          label: 'Dự kiến',
          data: [50, 55, 60, 55, 70, 85],
          borderColor: '#8b949e',
          borderDash: [5, 5],
          borderWidth: 2,
          fill: false,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom' }
        },
        scales: {
          y: { grid: { color: '#30363d' }, beginAtZero: true },
          x: { grid: { display: false } }
        }
      }
    });
  }

  // Fee Status Chart
  const ctxFee = document.getElementById('feeStatusChart');
  if (ctxFee) {
    new Chart(ctxFee, {
      type: 'doughnut',
      data: {
        labels: ['Đã thu', 'Còn nợ'],
        datasets: [{
          data: [75, 25],
          backgroundColor: ['#3fb950', '#f85149'],
          borderWidth: 0,
          hoverOffset: 4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '70%',
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
  }
}

function loadMockData() {
  // Mock Classes
  const classTbody = document.getElementById('classTableBody');
  if (classTbody) {
    classTbody.innerHTML = `
      <tr>
        <td><strong>3.1</strong></td>
        <td>Lớp 3 / 2026</td>
        <td>20/20</td>
        <td>150,000 đ</td>
        <td><span class="badge active">Active</span></td>
        <td>
          <button class="btn btn-outline btn-sm">Sửa</button>
          <button class="btn btn-outline btn-sm" style="color:var(--danger);border-color:var(--danger)">Đóng</button>
        </td>
      </tr>
      <tr>
        <td><strong>4.2</strong></td>
        <td>Lớp 4 / 2026</td>
        <td>15/20</td>
        <td>150,000 đ</td>
        <td><span class="badge active">Active</span></td>
        <td>
          <button class="btn btn-outline btn-sm">Sửa</button>
          <button class="btn btn-outline btn-sm" style="color:var(--danger);border-color:var(--danger)">Đóng</button>
        </td>
      </tr>
      <tr>
        <td><strong>1.1</strong></td>
        <td>Lớp 1 / 2025</td>
        <td>18/20</td>
        <td>120,000 đ</td>
        <td><span class="badge closed">Closed</span></td>
        <td>
          <button class="btn btn-outline btn-sm" disabled>Đã đóng</button>
        </td>
      </tr>
    `;
  }

  // Mock Students
  const studentTbody = document.getElementById('studentTableBody');
  if (studentTbody) {
    studentTbody.innerHTML = `
      <tr>
        <td>
          <div class="user-cell">
            <img src="https://ui-avatars.com/api/?name=Nguyen+Van+A&background=random" class="avatar" style="width:30px;height:30px">
            <div class="user-info">
              <div class="name">Nguyễn Văn A</div>
              <div class="email">hs.a@gmail.com</div>
            </div>
          </div>
        </td>
        <td>3.1</td>
        <td>Nguyễn Văn B (Bố)</td>
        <td><span class="badge active">Đang học</span></td>
        <td><button class="btn btn-outline btn-sm">Chi tiết</button></td>
      </tr>
      <tr>
        <td>
          <div class="user-cell">
            <img src="https://ui-avatars.com/api/?name=Tran+Thi+B&background=random" class="avatar" style="width:30px;height:30px">
            <div class="user-info">
              <div class="name">Trần Thị B</div>
              <div class="email">hs.b@gmail.com</div>
            </div>
          </div>
        </td>
        <td>4.2</td>
        <td>Trần Văn C (Bố)</td>
        <td><span class="badge active">Đang học</span></td>
        <td><button class="btn btn-outline btn-sm">Chi tiết</button></td>
      </tr>
    `;
  }

  // Mock Teachers
  const teacherTbody = document.getElementById('teacherTableBody');
  if (teacherTbody) {
    teacherTbody.innerHTML = `
      <tr>
        <td>
          <div class="user-cell">
            <img src="https://ui-avatars.com/api/?name=Le+Van+Giao&background=2f81f7&color=fff" class="avatar" style="width:30px;height:30px">
            <div class="user-info">
              <div class="name">Lê Văn Giao</div>
              <div class="email">gv.giao@trungtam.com</div>
            </div>
          </div>
        </td>
        <td>3.1, 4.2</td>
        <td>0901234567</td>
        <td><span class="badge active">Active</span></td>
        <td><button class="btn btn-outline btn-sm">Hồ sơ</button></td>
      </tr>
    `;
  }

  // Mock Fees
  const feeTbody = document.getElementById('feeTableBody');
  if (feeTbody) {
    feeTbody.innerHTML = `
      <tr>
        <td>Nguyễn Văn A</td>
        <td>3.1</td>
        <td>1,500,000 đ</td>
        <td>1,500,000 đ</td>
        <td>0 đ</td>
        <td><span class="badge active">Hoàn thành</span></td>
        <td><button class="btn btn-outline btn-sm" disabled>Đã thu</button></td>
      </tr>
      <tr>
        <td>Trần Thị B</td>
        <td>4.2</td>
        <td>1,200,000 đ</td>
        <td>500,000 đ</td>
        <td style="color:var(--danger);font-weight:bold">700,000 đ</td>
        <td><span class="badge warning">Thiếu nợ</span></td>
        <td><button class="btn btn-primary btn-sm">Thu tiếp</button></td>
      </tr>
    `;
  }
}
