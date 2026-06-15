import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { classService } from '../../services';
import Loading from '../../components/Loading/Loading';
import useAuthStore from '../../store/authStore';

const TeacherDashboard = () => {
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuthStore();
  const navigate = useNavigate();

  useEffect(() => { loadClasses(); }, []);

  const loadClasses = async () => {
    try {
      const res = await classService.getMyClasses();
      setClasses(res.data.data || []);
    } catch (err) { setClasses([]); }
    finally { setLoading(false); }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Xin chao, {user?.full_name}!</h1>
          <p className="page-subtitle">Cac lop ban dang giang day</p>
        </div>
      </div>

      <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(3, 1fr)' }}>
        <div className="stat-card blue">
          <div className="stat-icon">📚</div>
          <div className="stat-info">
            <div className="stat-label">Tong lop day</div>
            <div className="stat-value">{classes.length}</div>
          </div>
        </div>
        <div className="stat-card green">
          <div className="stat-icon">👨‍🎓</div>
          <div className="stat-info">
            <div className="stat-label">Tong hoc sinh</div>
            <div className="stat-value">{classes.reduce((s, c) => s + (c.student_count || 0), 0)}</div>
          </div>
        </div>
        <div className="stat-card orange">
          <div className="stat-icon">📋</div>
          <div className="stat-info">
            <div className="stat-label">Tong buoi da day</div>
            <div className="stat-value">{classes.reduce((s, c) => s + (c.completed_sessions || 0), 0)}</div>
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))', gap: '20px' }}>
        {classes.map(cls => (
          <div key={cls.id} className="card" style={{ cursor: 'pointer' }} onClick={() => navigate(`/teacher/classes/${cls.id}`)}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>{cls.name}</h3>
              <span className={`badge ${cls.status === 'active' ? 'badge-green' : 'badge-gray'}`}>{cls.status === 'active' ? 'Hoat dong' : 'Da dong'}</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Nam hoc:</span><span style={{ color: 'var(--text-primary)' }}>{cls.year}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Hoc sinh:</span><span style={{ color: 'var(--text-primary)' }}>{cls.student_count}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>Buoi da day:</span><span style={{ color: 'var(--text-primary)' }}>{cls.completed_sessions}/{cls.total_sessions}</span>
              </div>
              {cls.schedule_info && (
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>Lich hoc:</span><span style={{ color: 'var(--text-primary)' }}>{cls.schedule_info}</span>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      {classes.length === 0 && (
        <div className="empty-state">
          <div className="empty-icon">📚</div>
          <p className="empty-text">Ban chua duoc phan cong lop nao</p>
        </div>
      )}
    </div>
  );
};

export default TeacherDashboard;
