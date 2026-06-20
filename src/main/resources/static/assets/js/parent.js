document.addEventListener('DOMContentLoaded', () => {
  initNavigation();
  loadAttendanceData();

  // Handle Child Selection
  const selector = document.getElementById('childSelector');
  if (selector) {
    selector.addEventListener('change', (e) => {
      // Refresh data based on selected child
      loadAttendanceData(e.target.value);
    });
  }
});

function initNavigation() {
  const links = document.querySelectorAll('.sidebar-menu a');
  const sections = document.querySelectorAll('.page-section');
  const title = document.getElementById('topbar-title');

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
      
      links.forEach(l => l.classList.remove('active'));
      link.classList.add('active');

      const targetId = link.getAttribute('href').substring(1);
      sections.forEach(sec => {
        sec.classList.remove('active');
        if(sec.id === targetId) sec.classList.add('active');
      });

      title.innerText = link.innerText.split(' ').slice(1).join(' ');
      history.pushState(null, null, link.getAttribute('href'));
    });
  });
}

function loadAttendanceData(childId = "1") {
  const tbody = document.getElementById('parentAttendanceBody');
  if (!tbody) return;

  // Mock data for Child 1 (Nguyễn Văn A)
  const data1 = [
    { date: '20/06/2026', content: 'Unit 5: Animals and Habitats', eval: 'Cháu hiểu bài tốt, phát biểu to rõ.', status: 'present' },
    { date: '18/06/2026', content: 'Unit 4: Review', eval: '-', status: 'absent' },
    { date: '15/06/2026', content: 'Unit 4: My Body', eval: 'Cháu có tiến bộ so với tuần trước.', status: 'present' }
  ];

  // Mock data for Child 2 (Trần Thị B)
  const data2 = [
    { date: '20/06/2026', content: 'Luyện thi Flyers', eval: 'Làm bài nghe được 18/20 câu.', status: 'present' },
    { date: '18/06/2026', content: 'Reading Practice', eval: 'Cần cải thiện từ vựng.', status: 'late' }
  ];

  const data = childId === "1" ? data1 : data2;

  tbody.innerHTML = data.map(item => `
    <tr>
      <td>${item.date}</td>
      <td>${item.content}</td>
      <td>${item.eval}</td>
      <td>
        ${item.status === 'present' ? '<span class="badge active">Có mặt</span>' : ''}
        ${item.status === 'absent' ? '<span class="badge closed" style="background: rgba(248,81,73,0.15); color: var(--danger)">Vắng mặt</span>' : ''}
        ${item.status === 'late' ? '<span class="badge warning">Đi trễ</span>' : ''}
      </td>
    </tr>
  `).join('');
}
