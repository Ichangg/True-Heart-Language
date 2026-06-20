document.addEventListener('DOMContentLoaded', () => {
  initNavigation();
  loadHistoryMockData();
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

function loadHistoryMockData() {
  const tbody = document.getElementById('historyTableBody');
  if (!tbody) return;

  const data = [
    { date: '20/06/2026', content: 'Unit 5: Animals and Habitats', eval: 'Học sinh tích cực phát biểu.', status: 'present' },
    { date: '18/06/2026', content: 'Unit 4: Review', eval: '-', status: 'absent' },
    { date: '15/06/2026', content: 'Unit 4: My Body', eval: 'Làm bài tập đầy đủ', status: 'present' },
    { date: '13/06/2026', content: 'Unit 3: Colors', eval: 'Phát âm tốt', status: 'present' },
    { date: '11/06/2026', content: 'Unit 2: Family', eval: 'Đi trễ 15 phút', status: 'late' },
  ];

  tbody.innerHTML = data.map(item => `
    <tr>
      <td>${item.date}</td>
      <td>${item.content}</td>
      <td>${item.eval}</td>
      <td>
        ${item.status === 'present' ? '<span class="badge active">Có mặt</span>' : ''}
        ${item.status === 'absent' ? '<span class="badge closed">Vắng mặt</span>' : ''}
        ${item.status === 'late' ? '<span class="badge warning">Đi trễ</span>' : ''}
      </td>
    </tr>
  `).join('');
}
