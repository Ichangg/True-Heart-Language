import { useState, useEffect } from 'react';
import { enrollmentService, attendanceService } from '../../services';
import Loading from '../../components/Loading/Loading';
import useAuthStore from '../../store/authStore';

const StudentDashboard = () => {
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuthStore();

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const res = await enrollmentService.getMyClasses();
      setEnrollments(res.data.data || []);
    } catch (err) { setEnrollments([]); }
    finally { setLoading(false); }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Xin chao, {user?.full_name}!</h1>
          <p className="page-subtitle">Cac lop ban dang theo hoc</p>
        </div>
      </div>

      <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(3, 1fr)' }}>
        <div className="stat-card blue"><div className="stat-icon">📚</div><div className="stat-info"><div className="stat-label">Lop dang hoc</div><div className="stat-value">{enrollments.length}</div></div></div>
        <div className="stat-card green"><div className="stat-icon">✅</div><div className="stat-info"><div className="stat-label">Tong buoi da hoc</div><div className="stat-value">{enrollments.reduce((s, e) => s + (e.attended_sessions || 0), 0)}</div></div></div>
        <div className="stat-card red"><div className="stat-icon">❌</div><div className="stat-info"><div className="stat-label">Tong buoi vang</div><div className="stat-value">{enrollments.reduce((s, e) => s + (e.absent_sessions || 0), 0)}</div></div></div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))', gap: '20px' }}>
        {enrollments.map(e => (
          <div key={e.id} className="card">
            <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '16px' }}>{e.class?.name} ({e.class?.year})</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', fontSize: '0.875rem' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                <span>Giao vien:</span><span style={{ color: 'var(--text-primary)' }}>{e.class?.teacher?.full_name || '—'}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                <span>Tong buoi hoc:</span><span style={{ color: 'var(--text-primary)' }}>{e.total_sessions}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                <span>Da tham gia:</span><span className="text-success font-semibold">{e.attended_sessions}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                <span>Vang mat:</span><span className="text-danger font-semibold">{e.absent_sessions}</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      {enrollments.length === 0 && (
        <div className="empty-state"><div className="empty-icon">📚</div><p className="empty-text">Ban chua duoc dang ky lop nao</p></div>
      )}
    </div>
  );
};

export default StudentDashboard;
