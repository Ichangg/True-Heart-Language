document.addEventListener('DOMContentLoaded', () => {
  initNavigation();
  loadAttendanceMockData();
});

function initNavigation() {
  const links = document.querySelectorAll('.sidebar-menu a');
  const sections = document.querySelectorAll('.page-section');
  const title = document.getElementById('topbar-title');

  // Simple hash router for navigating to attendance from class card
  window.addEventListener('hashchange', () => {
    const hash = window.location.hash;
    if (hash) {
      links.forEach(l => {
        if (l.getAttribute('href') === hash) l.click();
      });
    }
  });

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
      
      // Update hash without scrolling
      history.pushState(null, null, link.getAttribute('href'));
    });
  });
}

function loadAttendanceMockData() {
  const container = document.getElementById('attendanceList');
  if (!container) return;

  const students = [
    { id: 1, name: 'Nguyễn Văn A', email: 'hs.a@gmail.com' },
    { id: 2, name: 'Trần Thị B', email: 'hs.b@gmail.com' },
    { id: 3, name: 'Lê Hoàng C', email: 'hs.c@gmail.com' },
    { id: 4, name: 'Phạm Minh D', email: 'hs.d@gmail.com' },
  ];

  container.innerHTML = students.map(s => `
    <div class="attendance-row">
      <div class="user-cell">
        <img src="https://ui-avatars.com/api/?name=${s.name.replace(/ /g, '+')}&background=random" class="avatar" style="width:36px;height:36px">
        <div class="user-info">
          <div class="name">${s.name}</div>
          <div class="email">Mã HS: SV00${s.id}</div>
        </div>
      </div>
      <div class="attendance-actions">
        <button class="btn-attendance present active" onclick="toggleAttendance(this, 'present')" title="Có mặt">✓</button>
        <button class="btn-attendance absent" onclick="toggleAttendance(this, 'absent')" title="Vắng mặt">✗</button>
        <button class="btn-attendance late" onclick="toggleAttendance(this, 'late')" title="Đi trễ">⌚</button>
      </div>
    </div>
  `).join('');
}

// Global function for inline onclick
window.toggleAttendance = function(btn, status) {
  // Find all buttons in the same row
  const row = btn.closest('.attendance-actions');
  const btns = row.querySelectorAll('.btn-attendance');
  
  // Remove active from all
  btns.forEach(b => b.classList.remove('active'));
  
  // Add active to clicked
  btn.classList.add('active');
}
