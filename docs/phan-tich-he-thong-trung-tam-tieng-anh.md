<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Phân tích Hệ thống – Trung tâm Tiếng Anh</title>
<style>
  @import url('https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&family=JetBrains+Mono:wght@400;600&display=swap');

  :root {
    --bg: #0d1117;
    --surface: #161b22;
    --surface2: #1c2330;
    --border: #30363d;
    --accent: #2f81f7;
    --accent2: #f78166;
    --accent3: #3fb950;
    --accent4: #d2a8ff;
    --accent5: #ffa657;
    --text: #e6edf3;
    --text-muted: #8b949e;
    --text-dim: #484f58;
    --fe: #2f81f7;
    --be: #3fb950;
    --db: #d2a8ff;
    --devops: #ffa657;
    --noti: #f78166;
  }

  * { margin: 0; padding: 0; box-sizing: border-box; }

  body {
    font-family: 'Be Vietnam Pro', sans-serif;
    background: var(--bg);
    color: var(--text);
    line-height: 1.7;
    font-size: 15px;
  }

  /* ── HERO ── */
  .hero {
    background: linear-gradient(135deg, #0d1117 0%, #1a1f2e 50%, #0d1117 100%);
    border-bottom: 1px solid var(--border);
    padding: 60px 40px 50px;
    position: relative;
    overflow: hidden;
  }
  .hero::before {
    content: '';
    position: absolute; inset: 0;
    background: radial-gradient(ellipse at 20% 50%, rgba(47,129,247,.12) 0%, transparent 60%),
                radial-gradient(ellipse at 80% 20%, rgba(63,185,80,.08) 0%, transparent 50%);
  }
  .hero-inner { position: relative; max-width: 960px; margin: 0 auto; }
  .hero-badge {
    display: inline-block;
    background: rgba(47,129,247,.15);
    border: 1px solid rgba(47,129,247,.4);
    color: var(--accent);
    font-size: 11px; font-weight: 700; letter-spacing: 2px;
    padding: 4px 14px; border-radius: 20px; margin-bottom: 20px;
    text-transform: uppercase;
  }
  .hero h1 {
    font-size: clamp(26px, 4vw, 42px);
    font-weight: 800; line-height: 1.2;
    margin-bottom: 12px;
  }
  .hero h1 span { color: var(--accent); }
  .hero p { color: var(--text-muted); font-size: 15px; max-width: 600px; }

  /* ── LAYOUT ── */
  .container { max-width: 1100px; margin: 0 auto; padding: 0 32px; }

  /* ── TOC ── */
  .toc {
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 12px;
    padding: 28px 32px;
    margin: 40px auto;
    max-width: 1100px;
    margin-left: 32px; margin-right: 32px;
  }
  .toc h2 { font-size: 13px; text-transform: uppercase; letter-spacing: 2px; color: var(--text-muted); margin-bottom: 16px; }
  .toc-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 8px; }
  .toc-item {
    display: flex; align-items: center; gap: 10px;
    padding: 8px 12px; border-radius: 8px;
    border: 1px solid transparent;
    cursor: pointer; text-decoration: none; color: var(--text-muted);
    font-size: 13px; transition: all .2s;
  }
  .toc-item:hover { background: var(--surface2); border-color: var(--border); color: var(--text); }
  .toc-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }

  /* ── SECTION ── */
  section { padding: 32px 0 16px; }
  .section-header {
    display: flex; align-items: center; gap: 14px;
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--border);
  }
  .section-icon {
    width: 42px; height: 42px; border-radius: 10px;
    display: flex; align-items: center; justify-content: center;
    font-size: 20px; flex-shrink: 0;
  }
  .section-header h2 { font-size: 20px; font-weight: 700; }
  .section-header p { font-size: 13px; color: var(--text-muted); margin-top: 2px; }

  /* ── CARDS GRID ── */
  .cards { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 16px; margin-bottom: 16px; }

  .card {
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 12px;
    padding: 20px 22px;
    position: relative;
    overflow: hidden;
  }
  .card::before {
    content: '';
    position: absolute; top: 0; left: 0; right: 0; height: 3px;
    border-radius: 12px 12px 0 0;
  }
  .card.fe::before { background: var(--fe); }
  .card.be::before { background: var(--be); }
  .card.db::before { background: var(--db); }
  .card.devops::before { background: var(--devops); }
  .card.noti::before { background: var(--noti); }
  .card.shared::before { background: linear-gradient(90deg, var(--fe), var(--be)); }

  .card-label {
    display: inline-flex; align-items: center; gap: 6px;
    font-size: 10px; font-weight: 700; letter-spacing: 1.5px;
    text-transform: uppercase; padding: 3px 10px; border-radius: 20px;
    margin-bottom: 12px;
  }
  .card-label.fe  { background: rgba(47,129,247,.15);  color: var(--fe); }
  .card-label.be  { background: rgba(63,185,80,.15);   color: var(--be); }
  .card-label.db  { background: rgba(210,168,255,.15); color: var(--db); }
  .card-label.devops { background: rgba(255,166,87,.15); color: var(--devops); }
  .card-label.noti { background: rgba(247,129,102,.15); color: var(--noti); }
  .card-label.shared { background: rgba(47,129,247,.1); color: var(--accent); }

  .card h3 { font-size: 14px; font-weight: 700; margin-bottom: 10px; }

  .card ul { list-style: none; }
  .card ul li {
    font-size: 13px; color: var(--text-muted);
    padding: 5px 0; border-bottom: 1px solid var(--border);
    display: flex; align-items: flex-start; gap: 8px;
  }
  .card ul li:last-child { border-bottom: none; }
  .card ul li::before { content: '›'; color: var(--accent); font-weight: 700; flex-shrink: 0; margin-top: 1px; }

  /* ── ROLE MATRIX ── */
  .matrix-wrap { overflow-x: auto; margin-bottom: 24px; }
  table {
    width: 100%; border-collapse: collapse; font-size: 13px;
    background: var(--surface); border-radius: 12px; overflow: hidden;
    border: 1px solid var(--border);
  }
  thead th {
    background: var(--surface2); padding: 12px 16px;
    text-align: left; font-weight: 700; color: var(--text-muted);
    font-size: 11px; letter-spacing: 1px; text-transform: uppercase;
    border-bottom: 1px solid var(--border);
  }
  tbody td { padding: 10px 16px; border-bottom: 1px solid var(--border); vertical-align: top; }
  tbody tr:last-child td { border-bottom: none; }
  tbody tr:hover td { background: rgba(255,255,255,.02); }
  .role-badge {
    display: inline-block; padding: 2px 10px; border-radius: 20px;
    font-size: 11px; font-weight: 700;
  }
  .r-admin  { background: rgba(255,166,87,.2);  color: var(--devops); }
  .r-teacher { background: rgba(63,185,80,.2);  color: var(--be); }
  .r-student { background: rgba(47,129,247,.2); color: var(--fe); }
  .r-parent { background: rgba(210,168,255,.2); color: var(--db); }

  .check { color: var(--accent3); }
  .cross { color: var(--accent2); opacity: .4; }

  /* ── DB SCHEMA ── */
  .schema-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 14px; }
  .schema-table {
    background: var(--surface); border: 1px solid var(--border);
    border-radius: 10px; overflow: hidden;
  }
  .schema-table .t-head {
    background: var(--surface2); padding: 10px 14px;
    font-size: 12px; font-weight: 700; color: var(--db);
    display: flex; align-items: center; gap: 8px;
    border-bottom: 1px solid var(--border);
  }
  .schema-table .t-head span { font-size: 14px; }
  .schema-table .t-body { padding: 10px 14px; }
  .field {
    font-family: 'JetBrains Mono', monospace;
    font-size: 11.5px; padding: 4px 0;
    border-bottom: 1px solid var(--border); color: var(--text-muted);
    display: flex; justify-content: space-between;
  }
  .field:last-child { border-bottom: none; }
  .field .fname { color: var(--text); }
  .field .ftype { color: var(--accent5); font-size: 10px; }
  .field.pk .fname { color: var(--accent4); }
  .field.fk .fname { color: var(--accent); }

  /* ── TECH STACK ── */
  .stack-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }
  .stack-card {
    background: var(--surface); border: 1px solid var(--border); border-radius: 10px;
    padding: 16px 18px;
  }
  .stack-card h4 { font-size: 12px; color: var(--text-muted); text-transform: uppercase; letter-spacing: 1px; margin-bottom: 10px; }
  .tech-pill {
    display: inline-block; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600;
    margin: 3px 3px 3px 0; border: 1px solid var(--border); color: var(--text);
  }

  /* ── WARN BOX ── */
  .warn {
    background: rgba(247,129,102,.08); border: 1px solid rgba(247,129,102,.3);
    border-left: 4px solid var(--noti); border-radius: 8px;
    padding: 14px 18px; margin: 16px 0; font-size: 13px;
  }
  .warn strong { color: var(--noti); }

  .info {
    background: rgba(47,129,247,.08); border: 1px solid rgba(47,129,247,.3);
    border-left: 4px solid var(--accent); border-radius: 8px;
    padding: 14px 18px; margin: 16px 0; font-size: 13px;
  }
  .info strong { color: var(--accent); }

  /* ── PHASE ── */
  .phases { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 14px; }
  .phase {
    background: var(--surface); border: 1px solid var(--border); border-radius: 10px; padding: 18px;
  }
  .phase-num {
    font-size: 11px; font-weight: 800; letter-spacing: 2px; text-transform: uppercase;
    color: var(--text-muted); margin-bottom: 10px;
  }
  .phase h4 { font-size: 14px; font-weight: 700; margin-bottom: 8px; }
  .phase ul { list-style: none; }
  .phase ul li { font-size: 12.5px; color: var(--text-muted); padding: 3px 0; display: flex; gap: 6px; }
  .phase ul li::before { content: '•'; flex-shrink: 0; }
  .phase-1 { border-top: 3px solid var(--accent3); }
  .phase-2 { border-top: 3px solid var(--accent); }
  .phase-3 { border-top: 3px solid var(--db); }
  .phase-4 { border-top: 3px solid var(--noti); }

  footer {
    text-align: center; padding: 40px; color: var(--text-dim); font-size: 12px;
    border-top: 1px solid var(--border); margin-top: 40px;
  }

  @media (max-width: 640px) {
    .hero { padding: 40px 20px; }
    .container, .toc { padding: 0 20px; margin-left: 16px; margin-right: 16px; }
    .cards, .schema-grid, .stack-grid, .phases { grid-template-columns: 1fr; }
  }
</style>
</head>
<body>

<!-- HERO -->
<div class="hero">
  <div class="hero-inner">
    <div class="hero-badge">Tài liệu phân tích hệ thống</div>
    <h1>Website Quản lý<br><span>Trung tâm Tiếng Anh</span></h1>
    <p>Phân tích toàn diện các vấn đề Frontend, Backend, Database, Thông báo và Triển khai cho hệ thống quản lý trung tâm.</p>
  </div>
</div>

<!-- TOC -->
<div class="toc">
  <h2>Mục lục</h2>
  <div class="toc-grid">
    <a class="toc-item" href="#roles"><span class="toc-dot" style="background:#ffa657"></span>1. Vai trò & Quyền hạn</a>
    <a class="toc-item" href="#db"><span class="toc-dot" style="background:#d2a8ff"></span>2. Thiết kế Database</a>
    <a class="toc-item" href="#be"><span class="toc-dot" style="background:#3fb950"></span>3. Backend – API & Logic</a>
    <a class="toc-item" href="#fe"><span class="toc-dot" style="background:#2f81f7"></span>4. Frontend – Giao diện</a>
    <a class="toc-item" href="#finance"><span class="toc-dot" style="background:#ffa657"></span>5. Hệ thống Học phí</a>
    <a class="toc-item" href="#noti"><span class="toc-dot" style="background:#f78166"></span>6. Thông báo Zalo/Facebook/SMS</a>
    <a class="toc-item" href="#admin-extra"><span class="toc-dot" style="background:#3fb950"></span>7. Admin – Thống kê & CMS</a>
    <a class="toc-item" href="#stack"><span class="toc-dot" style="background:#2f81f7"></span>8. Công nghệ Đề xuất</a>
    <a class="toc-item" href="#phases"><span class="toc-dot" style="background:#d2a8ff"></span>9. Lộ trình Phát triển</a>
  </div>
</div>

<div class="container">

<!-- ═══════════════════════════════════════════ 1. ROLES ══ -->
<section id="roles">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(255,166,87,.15)">👥</div>
    <div>
      <h2>1. Vai trò & Quyền hạn trong Hệ thống</h2>
      <p>4 role chính với quyền truy cập khác nhau</p>
    </div>
  </div>

  <div class="matrix-wrap">
    <table>
      <thead>
        <tr>
          <th>Chức năng</th>
          <th>Admin</th>
          <th>Giáo viên</th>
          <th>Học sinh</th>
          <th>Phụ huynh</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Mở/Đóng lớp học</td>
          <td><span class="check">✓ Toàn quyền</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
        <tr>
          <td>Quản lý giáo viên, học sinh</td>
          <td><span class="check">✓ Toàn quyền</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
        <tr>
          <td>Xem lớp đang dạy</td>
          <td><span class="check">✓</span></td>
          <td><span class="check">✓ Lớp của mình</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
        <tr>
          <td>Điểm danh học sinh</td>
          <td><span class="check">✓</span></td>
          <td><span class="check">✓ Lớp của mình</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
        <tr>
          <td>Xem lớp học, buổi học/nghỉ</td>
          <td><span class="check">✓</span></td>
          <td><span class="check">✓</span></td>
          <td><span class="check">✓ Của mình</span></td>
          <td><span class="check">✓ Của con</span></td>
        </tr>
        <tr>
          <td>Xem học phí, nộp tiền</td>
          <td><span class="check">✓ Toàn bộ</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="check">✓ Của con</span></td>
        </tr>
        <tr>
          <td>Thống kê tài chính</td>
          <td><span class="check">✓</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
        <tr>
          <td>Xem tên giáo viên dạy con</td>
          <td><span class="check">✓</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="check">✓ Tùy cấu hình</span></td>
        </tr>
        <tr>
          <td>Gửi thông báo Zalo/SMS</td>
          <td><span class="check">✓ Toàn quyền</span></td>
          <td><span class="check">✓ Lớp của mình</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
        <tr>
          <td>Quản lý trang chủ/CMS</td>
          <td><span class="check">✓</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
          <td><span class="cross">✗</span></td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="info">
    <strong>Lưu ý về Soft Delete:</strong> Hệ thống KHÔNG được xóa dữ liệu lớp học, học sinh đã đóng. Phải dùng cơ chế <code>status = 'active'|'closed'</code> và <code>deleted_at</code> (soft delete) để bảo toàn lịch sử.
  </div>
</section>

<!-- ═══════════════════════════════════════════ 2. DATABASE ══ -->
<section id="db">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(210,168,255,.15)">🗄️</div>
    <div>
      <h2>2. Thiết kế Database</h2>
      <p>Các bảng dữ liệu chính và quan hệ giữa chúng</p>
    </div>
  </div>

  <div class="schema-grid">

    <div class="schema-table">
      <div class="t-head"><span>👤</span> users</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field"><span class="fname">full_name</span><span class="ftype">VARCHAR</span></div>
        <div class="field"><span class="fname">email</span><span class="ftype">VARCHAR UNIQUE</span></div>
        <div class="field"><span class="fname">phone</span><span class="ftype">VARCHAR</span></div>
        <div class="field"><span class="fname">password_hash</span><span class="ftype">VARCHAR</span></div>
        <div class="field"><span class="fname">role</span><span class="ftype">ENUM(admin,teacher,student,parent)</span></div>
        <div class="field"><span class="fname">avatar_url</span><span class="ftype">VARCHAR</span></div>
        <div class="field"><span class="fname">zalo_id</span><span class="ftype">VARCHAR NULL</span></div>
        <div class="field"><span class="fname">facebook_id</span><span class="ftype">VARCHAR NULL</span></div>
        <div class="field"><span class="fname">is_active</span><span class="ftype">BOOLEAN</span></div>
        <div class="field"><span class="fname">deleted_at</span><span class="ftype">TIMESTAMP NULL</span></div>
        <div class="field"><span class="fname">created_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>🏫</span> class_groups (Khối/Lứa tuổi)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field"><span class="fname">name</span><span class="ftype">VARCHAR (VD: "Lớp 3")</span></div>
        <div class="field"><span class="fname">age_range</span><span class="ftype">VARCHAR</span></div>
        <div class="field"><span class="fname">description</span><span class="ftype">TEXT</span></div>
        <div class="field"><span class="fname">created_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>📚</span> classes (Lớp cụ thể)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">class_group_id</span><span class="ftype">FK → class_groups</span></div>
        <div class="field"><span class="fname">name</span><span class="ftype">VARCHAR (VD: "3.1")</span></div>
        <div class="field"><span class="fname">year</span><span class="ftype">YEAR (2018, 2019...)</span></div>
        <div class="field"><span class="fname">fee_per_session</span><span class="ftype">DECIMAL</span></div>
        <div class="field"><span class="fname">max_students</span><span class="ftype">INT</span></div>
        <div class="field"><span class="fname">schedule</span><span class="ftype">JSON (thứ, giờ)</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(active, closed)</span></div>
        <div class="field"><span class="fname">deleted_at</span><span class="ftype">TIMESTAMP NULL</span></div>
        <div class="field"><span class="fname">created_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>🔗</span> class_teachers</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">class_id</span><span class="ftype">FK → classes</span></div>
        <div class="field fk"><span class="fname">teacher_id</span><span class="ftype">FK → users</span></div>
        <div class="field"><span class="fname">is_primary</span><span class="ftype">BOOLEAN</span></div>
        <div class="field"><span class="fname">salary_per_session</span><span class="ftype">DECIMAL</span></div>
        <div class="field"><span class="fname">assigned_at</span><span class="ftype">TIMESTAMP</span></div>
        <div class="field"><span class="fname">removed_at</span><span class="ftype">TIMESTAMP NULL</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>👦</span> class_students (Enrollment)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">class_id</span><span class="ftype">FK → classes</span></div>
        <div class="field fk"><span class="fname">student_id</span><span class="ftype">FK → users</span></div>
        <div class="field"><span class="fname">discount_percent</span><span class="ftype">DECIMAL(5,2) DEFAULT 0</span></div>
        <div class="field"><span class="fname">custom_fee</span><span class="ftype">DECIMAL NULL (override)</span></div>
        <div class="field"><span class="fname">enrolled_at</span><span class="ftype">TIMESTAMP</span></div>
        <div class="field"><span class="fname">left_at</span><span class="ftype">TIMESTAMP NULL</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(active, left)</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>👨‍👩‍👦</span> parent_student</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">parent_id</span><span class="ftype">FK → users</span></div>
        <div class="field fk"><span class="fname">student_id</span><span class="ftype">FK → users</span></div>
        <div class="field"><span class="fname">relationship</span><span class="ftype">ENUM(father, mother, guardian)</span></div>
        <div class="field"><span class="fname">is_primary_contact</span><span class="ftype">BOOLEAN</span></div>
        <div class="field"><span class="fname">created_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>📅</span> sessions (Buổi học)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">class_id</span><span class="ftype">FK → classes</span></div>
        <div class="field fk"><span class="fname">teacher_id</span><span class="ftype">FK → users</span></div>
        <div class="field"><span class="fname">session_date</span><span class="ftype">DATE</span></div>
        <div class="field"><span class="fname">start_time</span><span class="ftype">TIME</span></div>
        <div class="field"><span class="fname">end_time</span><span class="ftype">TIME</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(scheduled, done, cancelled)</span></div>
        <div class="field"><span class="fname">note</span><span class="ftype">TEXT NULL</span></div>
        <div class="field"><span class="fname">created_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>✅</span> attendances (Điểm danh)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">session_id</span><span class="ftype">FK → sessions</span></div>
        <div class="field fk"><span class="fname">student_id</span><span class="ftype">FK → users</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(present, absent, late, excused)</span></div>
        <div class="field"><span class="fname">note</span><span class="ftype">TEXT NULL</span></div>
        <div class="field"><span class="fname">notified_parent</span><span class="ftype">BOOLEAN DEFAULT false</span></div>
        <div class="field"><span class="fname">marked_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>💰</span> fee_records (Học phí)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">student_id</span><span class="ftype">FK → users</span></div>
        <div class="field fk"><span class="fname">class_id</span><span class="ftype">FK → classes</span></div>
        <div class="field"><span class="fname">month</span><span class="ftype">DATE (YYYY-MM-01)</span></div>
        <div class="field"><span class="fname">sessions_attended</span><span class="ftype">INT</span></div>
        <div class="field"><span class="fname">fee_base</span><span class="ftype">DECIMAL (tính từ buổi)</span></div>
        <div class="field"><span class="fname">discount_amount</span><span class="ftype">DECIMAL</span></div>
        <div class="field"><span class="fname">fee_final</span><span class="ftype">DECIMAL (sau giảm)</span></div>
        <div class="field"><span class="fname">paid_amount</span><span class="ftype">DECIMAL DEFAULT 0</span></div>
        <div class="field"><span class="fname">debt_amount</span><span class="ftype">DECIMAL (generated column)</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(unpaid, partial, paid)</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>💳</span> payments (Lần đóng tiền)</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">fee_record_id</span><span class="ftype">FK → fee_records</span></div>
        <div class="field fk"><span class="fname">collected_by</span><span class="ftype">FK → users (admin)</span></div>
        <div class="field"><span class="fname">amount</span><span class="ftype">DECIMAL</span></div>
        <div class="field"><span class="fname">payment_method</span><span class="ftype">ENUM(cash, transfer)</span></div>
        <div class="field"><span class="fname">note</span><span class="ftype">TEXT NULL</span></div>
        <div class="field"><span class="fname">paid_at</span><span class="ftype">TIMESTAMP</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>💵</span> teacher_salaries</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">teacher_id</span><span class="ftype">FK → users</span></div>
        <div class="field fk"><span class="fname">class_id</span><span class="ftype">FK → classes</span></div>
        <div class="field"><span class="fname">month</span><span class="ftype">DATE</span></div>
        <div class="field"><span class="fname">sessions_taught</span><span class="ftype">INT</span></div>
        <div class="field"><span class="fname">total_salary</span><span class="ftype">DECIMAL</span></div>
        <div class="field"><span class="fname">paid_amount</span><span class="ftype">DECIMAL</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(pending, paid)</span></div>
        <div class="field"><span class="fname">paid_at</span><span class="ftype">TIMESTAMP NULL</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>📢</span> cms_banners</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field"><span class="fname">type</span><span class="ftype">ENUM(popup, slider, banner)</span></div>
        <div class="field"><span class="fname">title</span><span class="ftype">VARCHAR</span></div>
        <div class="field"><span class="fname">content</span><span class="ftype">TEXT (rich HTML)</span></div>
        <div class="field"><span class="fname">image_url</span><span class="ftype">VARCHAR NULL</span></div>
        <div class="field"><span class="fname">link_url</span><span class="ftype">VARCHAR NULL</span></div>
        <div class="field"><span class="fname">is_active</span><span class="ftype">BOOLEAN</span></div>
        <div class="field"><span class="fname">start_at</span><span class="ftype">TIMESTAMP NULL</span></div>
        <div class="field"><span class="fname">end_at</span><span class="ftype">TIMESTAMP NULL</span></div>
        <div class="field"><span class="fname">position</span><span class="ftype">INT (thứ tự hiển thị)</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>🔔</span> notification_logs</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">id</span><span class="ftype">BIGINT PK</span></div>
        <div class="field fk"><span class="fname">sent_by</span><span class="ftype">FK → users</span></div>
        <div class="field fk"><span class="fname">recipient_id</span><span class="ftype">FK → users</span></div>
        <div class="field"><span class="fname">channel</span><span class="ftype">ENUM(zalo, facebook, sms, inapp)</span></div>
        <div class="field"><span class="fname">type</span><span class="ftype">ENUM(attendance, fee, class_cancel…)</span></div>
        <div class="field"><span class="fname">message</span><span class="ftype">TEXT</span></div>
        <div class="field"><span class="fname">status</span><span class="ftype">ENUM(sent, failed, pending)</span></div>
        <div class="field"><span class="fname">sent_at</span><span class="ftype">TIMESTAMP</span></div>
        <div class="field"><span class="fname">error_msg</span><span class="ftype">TEXT NULL</span></div>
      </div>
    </div>

    <div class="schema-table">
      <div class="t-head"><span>⚙️</span> system_settings</div>
      <div class="t-body">
        <div class="field pk"><span class="fname">key</span><span class="ftype">VARCHAR PK</span></div>
        <div class="field"><span class="fname">value</span><span class="ftype">TEXT</span></div>
        <div class="field"><span class="fname">description</span><span class="ftype">TEXT</span></div>
        <div class="field"><span class="fname">updated_at</span><span class="ftype">TIMESTAMP</span></div>
        <div style="font-size:11px;color:var(--text-muted);margin-top:8px">VD: show_teacher_to_parent = true/false</div>
      </div>
    </div>

  </div>
</section>

<!-- ═══════════════════════════════════════════ 3. BACKEND ══ -->
<section id="be">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(63,185,80,.15)">⚙️</div>
    <div>
      <h2>3. Backend – API & Business Logic</h2>
      <p>Các nhóm API cần xây dựng và logic phức tạp</p>
    </div>
  </div>

  <div class="cards">

    <div class="card be">
      <div class="card-label be">BE – Auth</div>
      <h3>Xác thực & Phân quyền</h3>
      <ul>
        <li>Đăng nhập JWT (Access Token + Refresh Token)</li>
        <li>Middleware phân quyền theo role (RBAC)</li>
        <li>Quên mật khẩu qua email/OTP SMS</li>
        <li>Giới hạn rate limiting chống brute force</li>
        <li>Blacklist token khi đăng xuất</li>
        <li>Social login (Zalo, Facebook) tùy chọn</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – Class</div>
      <h3>Quản lý Lớp học</h3>
      <ul>
        <li>CRUD lớp học với ràng buộc năm học</li>
        <li>Đóng lớp: set status='closed', KHÔNG xóa</li>
        <li>Auto-generate tên lớp (3.1, 3.2, 3.3)</li>
        <li>Kiểm tra trùng tên lớp trong cùng năm</li>
        <li>API gán/xóa giáo viên khỏi lớp (ghi lịch sử)</li>
        <li>API gán/xóa học sinh khỏi lớp (ghi enrolled_at, left_at)</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – Session</div>
      <h3>Quản lý Buổi học</h3>
      <ul>
        <li>Tạo buổi học thủ công hoặc auto theo lịch</li>
        <li>Cập nhật trạng thái buổi (done/cancelled)</li>
        <li>Hủy buổi học → trigger thông báo phụ huynh</li>
        <li>Đếm số buổi đã học/nghỉ theo học sinh</li>
        <li>Tính lương giáo viên theo buổi đã dạy</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – Attendance</div>
      <h3>Điểm danh</h3>
      <ul>
        <li>Giáo viên điểm danh từng học sinh mỗi buổi</li>
        <li>Trạng thái: present / absent / late / excused</li>
        <li>Sau khi điểm danh → auto cập nhật fee_records</li>
        <li>Trigger gửi thông báo khi học sinh vắng</li>
        <li>Xem lịch sử điểm danh theo học sinh/lớp/tháng</li>
        <li>Export điểm danh ra Excel</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – Finance</div>
      <h3>Học phí & Thanh toán</h3>
      <ul>
        <li>Tính học phí: sessions × fee_per_session × (1 - discount%)</li>
        <li>Tạo fee_record tự động mỗi tháng (cron job)</li>
        <li>Ghi nhận thanh toán từng lần (partial payment)</li>
        <li>Tính tổng nợ chưa đóng nhiều tháng</li>
        <li>Thống kê theo tháng/quý/năm/khoảng thời gian</li>
        <li>Thống kê lương giáo viên đã/chưa trả</li>
        <li>Theo dõi học sinh tăng/giảm theo tháng</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – User Mgmt</div>
      <h3>Quản lý Người dùng</h3>
      <ul>
        <li>CRUD học sinh, giáo viên, phụ huynh</li>
        <li>Liên kết 1 phụ huynh → nhiều học sinh</li>
        <li>Liên kết 1 học sinh → nhiều phụ huynh</li>
        <li>Soft delete (không xóa thật)</li>
        <li>Upload avatar (S3 / local)</li>
        <li>Search/filter/sort/paginate</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – Settings</div>
      <h3>Cấu hình Hệ thống</h3>
      <ul>
        <li>Hiển thị/ẩn tên giáo viên với phụ huynh</li>
        <li>Cấu hình API keys Zalo/Facebook/SMS</li>
        <li>Cấu hình giờ học mặc định, phí mặc định</li>
        <li>Cấu hình template tin nhắn tự động</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">BE – CMS</div>
      <h3>CMS Trang chủ</h3>
      <ul>
        <li>CRUD banners/popups/sliders</li>
        <li>Upload ảnh, set thời gian hiển thị (start_at, end_at)</li>
        <li>API public lấy banners đang active</li>
        <li>Quản lý thứ tự hiển thị (drag & drop)</li>
      </ul>
    </div>

  </div>

  <div class="warn">
    <strong>⚠️ Logic phức tạp cần chú ý:</strong> Khi giáo viên điểm danh xong một buổi, hệ thống phải tự động: (1) cập nhật <code>fee_records</code> của tháng đó cho từng học sinh, (2) cập nhật <code>teacher_salaries</code>, (3) kiểm tra học sinh vắng → queue thông báo phụ huynh. Cần dùng <strong>Database Transaction</strong> để đảm bảo tính toàn vẹn dữ liệu.
  </div>
</section>

<!-- ═══════════════════════════════════════════ 4. FRONTEND ══ -->
<section id="fe">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(47,129,247,.15)">🖥️</div>
    <div>
      <h2>4. Frontend – Giao diện theo từng Role</h2>
      <p>Các màn hình và tính năng giao diện cần xây dựng</p>
    </div>
  </div>

  <div class="cards">

    <div class="card fe">
      <div class="card-label fe">FE – Public</div>
      <h3>Trang chủ (Public)</h3>
      <ul>
        <li>Slider/Banner quảng cáo (lấy từ CMS API)</li>
        <li>Popup thông báo mở lớp mới</li>
        <li>Thông tin trung tâm, liên hệ</li>
        <li>Form đăng ký tư vấn (gửi lead)</li>
        <li>Responsive mobile/tablet/desktop</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Admin</div>
      <h3>Admin Dashboard</h3>
      <ul>
        <li>Tổng quan: số lớp, HS, GV, doanh thu tháng</li>
        <li>Biểu đồ học sinh tăng/giảm theo tháng</li>
        <li>Biểu đồ doanh thu dự kiến vs đã thu</li>
        <li>Danh sách nợ học phí cần thu</li>
        <li>Quick actions: tạo lớp, thêm HS, gửi thông báo</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Admin</div>
      <h3>Admin – Quản lý Lớp học</h3>
      <ul>
        <li>Danh sách lớp: filter theo năm, khối, trạng thái</li>
        <li>Form tạo lớp mới (chọn khối, năm, lịch học, học phí)</li>
        <li>Chi tiết lớp: danh sách HS, GV, buổi học</li>
        <li>Nút Đóng lớp (confirm dialog)</li>
        <li>Gán/xóa giáo viên, học sinh</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Admin</div>
      <h3>Admin – Quản lý Học sinh & Phụ huynh</h3>
      <ul>
        <li>Danh sách học sinh, search, filter, pagination</li>
        <li>Form tạo/sửa hồ sơ học sinh</li>
        <li>Liên kết phụ huynh với học sinh</li>
        <li>Cài đặt % giảm giá riêng cho từng HS</li>
        <li>Xem lịch sử học phí của HS</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Admin</div>
      <h3>Admin – Tài chính</h3>
      <ul>
        <li>Bảng học phí: filter theo tháng/lớp/trạng thái</li>
        <li>Nhập số tiền đã thu từng học sinh</li>
        <li>Thống kê: doanh thu dự kiến/thực thu/lương GV</li>
        <li>Bộ lọc thời gian: tháng, quý, năm, tuỳ chọn</li>
        <li>Export báo cáo Excel/PDF</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Teacher</div>
      <h3>Teacher Dashboard</h3>
      <ul>
        <li>Danh sách lớp đang dạy, số buổi mỗi lớp</li>
        <li>Lịch dạy trong tuần (calendar view)</li>
        <li>Form điểm danh: chọn buổi → tick từng HS</li>
        <li>Xem lịch sử điểm danh đã làm</li>
        <li>Nút gửi thông báo nghỉ học cho cả lớp</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Student</div>
      <h3>Student Dashboard</h3>
      <ul>
        <li>Thông tin lớp đang học</li>
        <li>Số buổi đã học / đã nghỉ trong tháng</li>
        <li>Lịch sử điểm danh (xem buổi nào vắng)</li>
        <li>Thông tin giáo viên (nếu admin cho phép)</li>
      </ul>
    </div>

    <div class="card fe">
      <div class="card-label fe">FE – Parent</div>
      <h3>Parent Dashboard</h3>
      <ul>
        <li>Thông tin con đang học lớp nào</li>
        <li>Xem tên giáo viên (tùy cấu hình admin)</li>
        <li>Số buổi học/nghỉ, chi tiết buổi nào nghỉ</li>
        <li>Học phí: số tiền phải đóng, đã giảm, còn nợ</li>
        <li>Tổng nợ nhiều tháng chưa đóng</li>
        <li>Lịch sử thanh toán</li>
      </ul>
    </div>

  </div>
</section>

<!-- ═══════════════════════════════════════════ 5. FINANCE ══ -->
<section id="finance">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(255,166,87,.15)">💰</div>
    <div>
      <h2>5. Hệ thống Học phí – Logic Chi tiết</h2>
      <p>Công thức tính và các trường hợp đặc biệt</p>
    </div>
  </div>

  <div class="cards">
    <div class="card shared">
      <div class="card-label shared">Logic</div>
      <h3>Công thức Tính Học phí</h3>
      <ul>
        <li><strong>fee_base</strong> = sessions_attended × fee_per_session (của lớp)</li>
        <li><strong>discount_amount</strong> = fee_base × (discount_percent / 100)</li>
        <li><strong>fee_final</strong> = fee_base − discount_amount</li>
        <li><strong>debt</strong> = fee_final − paid_amount (tổng nhiều tháng)</li>
        <li>Custom_fee: nếu có → thay thế hoàn toàn fee_per_session</li>
        <li>Phụ huynh thấy: Học phí gốc, Giảm X%, Còn phải đóng</li>
      </ul>
    </div>

    <div class="card shared">
      <div class="card-label shared">Cron Job</div>
      <h3>Auto-generate Fee Records</h3>
      <ul>
        <li>Cuối tháng: tự tạo fee_record cho mỗi HS trong lớp active</li>
        <li>Dựa trên attendance đã điểm danh của tháng đó</li>
        <li>Nếu HS chưa có attendance → sessions = 0 (cảnh báo)</li>
        <li>Admin có thể xem "dự kiến" (chưa hết tháng)</li>
        <li>Gửi thông báo nhắc đóng tiền đầu tháng sau</li>
      </ul>
    </div>

    <div class="card shared">
      <div class="card-label shared">Thống kê</div>
      <h3>Báo cáo Tài chính Admin</h3>
      <ul>
        <li>Doanh thu dự kiến: tổng fee_final của kỳ</li>
        <li>Đã thu thực tế: tổng paid_amount của kỳ</li>
        <li>Tổng nợ tích lũy: tổng debt chưa đóng</li>
        <li>Lương giáo viên: sessions_taught × salary_per_session</li>
        <li>Lọc: tháng / quý / năm / khoảng ngày tùy chọn</li>
        <li>Chart: so sánh dự kiến vs thực thu theo tháng</li>
      </ul>
    </div>
  </div>
</section>

<!-- ═══════════════════════════════════════════ 6. NOTIFICATION ══ -->
<section id="noti">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(247,129,102,.15)">🔔</div>
    <div>
      <h2>6. Hệ thống Thông báo – Zalo / Facebook / SMS</h2>
      <p>Phần phức tạp nhất về tích hợp bên thứ 3</p>
    </div>
  </div>

  <div class="warn">
    <strong>⚠️ Quan trọng về Zalo OA API:</strong> Để gửi tin nhắn Zalo cho phụ huynh, phụ huynh phải <strong>follow Zalo OA</strong> của trung tâm trước. Đây là yêu cầu bắt buộc của Zalo. Cần đăng ký tài khoản Zalo Official Account và tích hợp Zalo OA API.
  </div>

  <div class="cards">

    <div class="card noti">
      <div class="card-label noti">Zalo OA</div>
      <h3>Zalo Official Account</h3>
      <ul>
        <li>Đăng ký Zalo OA → lấy Access Token</li>
        <li>Phụ huynh follow OA → lưu zalo_id vào DB</li>
        <li>Liên kết tài khoản: phụ huynh đăng nhập → bind Zalo</li>
        <li>API gửi tin nhắn: POST /v3/oa/message/cs</li>
        <li>Template tin nhắn: vắng học, nhắc học phí, nghỉ lớp</li>
        <li>Gửi hàng loạt: queue message cho cả lớp</li>
        <li>Zalo Mini App (tùy chọn nâng cao)</li>
      </ul>
    </div>

    <div class="card noti">
      <div class="card-label noti">Facebook</div>
      <h3>Facebook Messenger</h3>
      <ul>
        <li>Tạo Facebook Page + đăng ký Messenger API</li>
        <li>Phụ huynh nhắn tin Page → lưu PSID (Page-Scoped ID)</li>
        <li>Gửi tin nhắn qua Send API</li>
        <li>Dùng Message Tags cho thông báo quan trọng</li>
        <li>24h rule: chỉ có thể reply trong 24h, dùng tags để gửi ngoài window</li>
        <li>Template: text + quick replies</li>
      </ul>
    </div>

    <div class="card noti">
      <div class="card-label noti">SMS</div>
      <h3>Tin nhắn SMS (Khẩn cấp)</h3>
      <ul>
        <li>Dùng khi Zalo/Facebook không khả dụng</li>
        <li>Nhà cung cấp: Esms.vn, Vietguys, SpeedSMS (Việt Nam)</li>
        <li>OTP SMS cho đăng nhập/reset password</li>
        <li>SMS hàng loạt: thông báo nghỉ học khẩn cấp</li>
        <li>Cần đăng ký Brandname với nhà mạng</li>
        <li>Chi phí: ~500-700đ/SMS tùy nhà cung cấp</li>
      </ul>
    </div>

    <div class="card noti">
      <div class="card-label noti">Queue</div>
      <h3>Message Queue System</h3>
      <ul>
        <li>Dùng Bull Queue (Redis) hoặc tương tự</li>
        <li>Gửi hàng loạt không block main thread</li>
        <li>Retry tự động khi gửi thất bại</li>
        <li>Log kết quả vào notification_logs</li>
        <li>Dashboard theo dõi trạng thái gửi</li>
        <li>Rate limiting theo từng nền tảng</li>
      </ul>
    </div>

    <div class="card noti">
      <div class="card-label noti">Auto Triggers</div>
      <h3>Thông báo Tự động</h3>
      <ul>
        <li><strong>Sau điểm danh:</strong> gửi ngay khi HS vắng</li>
        <li><strong>Đầu tháng:</strong> nhắc học phí tháng trước chưa đóng</li>
        <li><strong>Khi hủy buổi:</strong> gửi cả lớp biết</li>
        <li><strong>Mở lớp mới:</strong> gửi danh sách đăng ký chờ</li>
        <li>Cron job: chạy lúc 8h sáng mỗi ngày</li>
      </ul>
    </div>

    <div class="card noti">
      <div class="card-label noti">Manual Send</div>
      <h3>Gửi thủ công từ Admin/GV</h3>
      <ul>
        <li>Chọn đối tượng: 1 PH / cả lớp / nhiều lớp</li>
        <li>Chọn kênh: Zalo / Facebook / SMS / All</li>
        <li>Soạn nội dung tự do hoặc dùng template</li>
        <li>Preview trước khi gửi</li>
        <li>Xem lịch sử đã gửi, tỉ lệ thành công</li>
      </ul>
    </div>

  </div>
</section>

<!-- ═══════════════════════════════════════════ 7. ADMIN EXTRA ══ -->
<section id="admin-extra">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(63,185,80,.15)">📊</div>
    <div>
      <h2>7. Admin – Thống kê & CMS Nâng cao</h2>
      <p>Các tính năng quản trị và báo cáo</p>
    </div>
  </div>

  <div class="cards">

    <div class="card be">
      <div class="card-label be">Thống kê</div>
      <h3>Biểu đồ Tăng/Giảm Học sinh</h3>
      <ul>
        <li>Đếm enrollment mới theo tháng (enrolled_at)</li>
        <li>Đếm học sinh rời khỏi theo tháng (left_at)</li>
        <li>Net growth = mới − rời</li>
        <li>Biểu đồ Line Chart theo 12 tháng / năm học</li>
        <li>Breakdown theo từng khối lớp</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">CMS</div>
      <h3>CMS Quảng cáo Trang chủ</h3>
      <ul>
        <li>WYSIWYG editor soạn nội dung popup/banner</li>
        <li>Upload nhiều ảnh, sắp xếp thứ tự slider</li>
        <li>Hẹn giờ hiển thị: bắt đầu → kết thúc</li>
        <li>Preview trực tiếp trước khi publish</li>
        <li>Deactivate tạm thời không cần xóa</li>
      </ul>
    </div>

    <div class="card be">
      <div class="card-label be">Report</div>
      <h3>Export Báo cáo</h3>
      <ul>
        <li>Export học phí tháng → Excel (XLSX)</li>
        <li>Export danh sách điểm danh → Excel</li>
        <li>Báo cáo lương giáo viên → PDF</li>
        <li>Sử dụng: exceljs, PDFKit hoặc similar</li>
      </ul>
    </div>

  </div>
</section>

<!-- ═══════════════════════════════════════════ 8. STACK ══ -->
<section id="stack">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(47,129,247,.15)">🛠️</div>
    <div>
      <h2>8. Công nghệ Đề xuất</h2>
      <p>Stack kỹ thuật phù hợp cho quy mô trung tâm nhỏ-vừa</p>
    </div>
  </div>

  <div class="stack-grid">
    <div class="stack-card">
      <h4>Frontend</h4>
      <span class="tech-pill">React / Next.js</span>
      <span class="tech-pill">TailwindCSS</span>
      <span class="tech-pill">Zustand / Redux</span>
      <span class="tech-pill">React Query</span>
      <span class="tech-pill">Chart.js / Recharts</span>
      <span class="tech-pill">React Hook Form</span>
    </div>
    <div class="stack-card">
      <h4>Backend</h4>
      <span class="tech-pill">Node.js + Express</span>
      <span class="tech-pill">hoặc NestJS</span>
      <span class="tech-pill">hoặc Laravel (PHP)</span>
      <span class="tech-pill">JWT Auth</span>
      <span class="tech-pill">Multer (file upload)</span>
    </div>
    <div class="stack-card">
      <h4>Database</h4>
      <span class="tech-pill">MySQL / PostgreSQL</span>
      <span class="tech-pill">Prisma / Sequelize</span>
      <span class="tech-pill">Redis (cache + queue)</span>
    </div>
    <div class="stack-card">
      <h4>Notification</h4>
      <span class="tech-pill">Zalo OA API</span>
      <span class="tech-pill">Facebook Send API</span>
      <span class="tech-pill">eSMS.vn / SpeedSMS</span>
      <span class="tech-pill">Bull Queue (Redis)</span>
      <span class="tech-pill">Node-cron</span>
    </div>
    <div class="stack-card">
      <h4>DevOps & Hosting</h4>
      <span class="tech-pill">VPS (DigitalOcean/Vultr)</span>
      <span class="tech-pill">Nginx reverse proxy</span>
      <span class="tech-pill">PM2</span>
      <span class="tech-pill">Let's Encrypt SSL</span>
      <span class="tech-pill">Docker (tùy chọn)</span>
    </div>
    <div class="stack-card">
      <h4>Storage & Email</h4>
      <span class="tech-pill">Cloudinary / S3</span>
      <span class="tech-pill">Nodemailer</span>
      <span class="tech-pill">Gmail SMTP / Mailgun</span>
    </div>
  </div>
</section>

<!-- ═══════════════════════════════════════════ 9. PHASES ══ -->
<section id="phases">
  <div class="section-header">
    <div class="section-icon" style="background:rgba(210,168,255,.15)">🗓️</div>
    <div>
      <h2>9. Lộ trình Phát triển Đề xuất</h2>
      <p>Chia thành 4 giai đoạn từ MVP đến hoàn chỉnh</p>
    </div>
  </div>

  <div class="phases">
    <div class="phase phase-1">
      <div class="phase-num">Giai đoạn 1 · MVP · ~6-8 tuần</div>
      <h4>Lõi hệ thống</h4>
      <ul>
        <li>Auth & phân quyền 4 role</li>
        <li>Quản lý lớp, GV, HS, PH</li>
        <li>Điểm danh cơ bản</li>
        <li>Dashboard từng role</li>
        <li>Trang chủ tĩnh</li>
      </ul>
    </div>
    <div class="phase phase-2">
      <div class="phase-num">Giai đoạn 2 · Finance · ~4-6 tuần</div>
      <h4>Học phí & Báo cáo</h4>
      <ul>
        <li>Hệ thống học phí, giảm giá</li>
        <li>Ghi nhận thanh toán</li>
        <li>Thống kê tài chính</li>
        <li>Biểu đồ tăng/giảm HS</li>
        <li>Export Excel/PDF</li>
      </ul>
    </div>
    <div class="phase phase-3">
      <div class="phase-num">Giai đoạn 3 · CMS & Noti · ~4-5 tuần</div>
      <h4>CMS + Thông báo</h4>
      <ul>
        <li>CMS banner/popup trang chủ</li>
        <li>Tích hợp Zalo OA API</li>
        <li>Tích hợp SMS</li>
        <li>Message Queue</li>
        <li>Template tin nhắn</li>
      </ul>
    </div>
    <div class="phase phase-4">
      <div class="phase-num">Giai đoạn 4 · Advanced · ~3-4 tuần</div>
      <h4>Nâng cao & Tối ưu</h4>
      <ul>
        <li>Facebook Messenger API</li>
        <li>Thông báo tự động (cron)</li>
        <li>Gửi hàng loạt thông minh</li>
        <li>Performance & Security audit</li>
        <li>Mobile-responsive hoàn chỉnh</li>
      </ul>
    </div>
  </div>

  <div class="info" style="margin-top:24px">
    <strong>💡 Gợi ý:</strong> Nên bắt đầu với Giai đoạn 1 + 2 để có hệ thống chạy được ngay. Giai đoạn 3 (Zalo/SMS) có thể song song khi Giai đoạn 2 đang test, vì việc đăng ký Zalo OA mất thời gian duyệt (~1-2 tuần từ phía Zalo).
  </div>
</section>

</div>

<footer>
  Tài liệu phân tích hệ thống – Website Quản lý Trung tâm Tiếng Anh · Tháng 5/2026
</footer>

</body>
</html>
