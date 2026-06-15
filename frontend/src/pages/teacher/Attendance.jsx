import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { classService, attendanceService, enrollmentService } from '../../services';
import Modal from '../../components/Modal/Modal';
import Loading from '../../components/Loading/Loading';
import useAuthStore from '../../store/authStore';
import { ATTENDANCE_LABELS } from '../../utils/constants';

const Attendance = () => {
  const { classId } = useParams();
  const navigate = useNavigate();
  const [classes, setClasses] = useState([]);
  const [classData, setClassData] = useState(null);
  const [sessions, setSessions] = useState([]);
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showSessionModal, setShowSessionModal] = useState(false);
  const [showAttendanceModal, setShowAttendanceModal] = useState(false);
  const [selectedSession, setSelectedSession] = useState(null);
  const [sessionForm, setSessionForm] = useState({ session_date: '', topic: '' });
  const [attendanceData, setAttendanceData] = useState([]);
  const { user } = useAuthStore();

  useEffect(() => {
    if (classId) {
      loadData();
    } else {
      loadClasses();
    }
  }, [classId]);

  const loadClasses = async () => {
    try {
      setLoading(true);
      const res = await classService.getMyClasses();
      setClasses(res.data.data || []);
    } catch (err) {
      console.error(err);
      setClasses([]);
    } finally {
      setLoading(false);
    }
  };

  const loadData = async () => {
    try {
      const [classRes, sessionRes, enrollRes] = await Promise.all([
        classService.getById(classId),
        attendanceService.getSessions(classId),
        enrollmentService.getByClass(classId),
      ]);
      setClassData(classRes.data.data);
      setSessions(sessionRes.data.data || []);
      setEnrollments(enrollRes.data.data || []);
    } catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  const handleCreateSession = async (e) => {
    e.preventDefault();
    try {
      await attendanceService.createSession(classId, sessionForm);
      setShowSessionModal(false);
      setSessionForm({ session_date: '', topic: '' });
      loadData();
    } catch (err) { alert(err.response?.data?.message || 'Loi'); }
  };

  const openAttendance = (session) => {
    setSelectedSession(session);
    setAttendanceData(enrollments.map(e => ({
      enrollment_id: e.id,
      student_name: e.student?.full_name,
      status: 'present',
      note: '',
    })));
    setShowAttendanceModal(true);
  };

  const handleMarkAttendance = async () => {
    try {
      await attendanceService.markAttendance(selectedSession.id, { attendanceData });
      setShowAttendanceModal(false);
      loadData();
      alert('Diem danh thanh cong!');
    } catch (err) { alert(err.response?.data?.message || 'Loi'); }
  };

  if (loading) return <Loading />;

  if (!classId) {
    return (
      <div>
        <div className="page-header">
          <div>
            <h1 className="page-title">Điểm danh lớp học</h1>
            <p className="page-subtitle">Chọn một lớp học để tiến hành điểm danh</p>
          </div>
        </div>
        
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px', marginTop: '24px' }}>
          {classes.map(cls => (
            <div key={cls.id} className="card" style={{ cursor: 'pointer' }} onClick={() => navigate(`/teacher/classes/${cls.id}`)}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>{cls.name}</h3>
                <span className={`badge ${cls.status === 'active' ? 'badge-green' : 'badge-gray'}`}>{cls.status === 'active' ? 'Hoạt động' : 'Đã đóng'}</span>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>Năm học:</span><span style={{ color: 'var(--text-primary)' }}>{cls.year}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>Học sinh:</span><span style={{ color: 'var(--text-primary)' }}>{cls.student_count}</span>
                </div>
                {cls.schedule_info && (
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>Lịch học:</span><span style={{ color: 'var(--text-primary)' }}>{cls.schedule_info}</span>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>

        {classes.length === 0 && (
          <div className="empty-state">
            <div className="empty-icon">📋</div>
            <p className="empty-text">Bạn chưa được phân công lớp nào</p>
          </div>
        )}
      </div>
    );
  }

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Diem danh - {classData?.name}</h1>
          <p className="page-subtitle">{classData?.schedule_info} | {classData?.year}</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowSessionModal(true)}>+ Tao buoi hoc</button>
      </div>

      <div className="table-container">
        <table className="table">
          <thead><tr><th>Buoi</th><th>Ngay</th><th>Noi dung</th><th>Trang thai</th><th>Thao tac</th></tr></thead>
          <tbody>
            {sessions.map(s => (
              <tr key={s.id}>
                <td>Buoi {s.session_number}</td>
                <td>{new Date(s.session_date).toLocaleDateString('vi-VN')}</td>
                <td>{s.topic || '—'}</td>
                <td><span className={`badge ${s.status === 'completed' ? 'badge-green' : s.status === 'cancelled' ? 'badge-red' : 'badge-orange'}`}>{s.status === 'completed' ? 'Hoan thanh' : s.status === 'cancelled' ? 'Huy' : 'Chua diem danh'}</span></td>
                <td>{s.status !== 'completed' && <button className="btn btn-sm btn-primary" onClick={() => openAttendance(s)}>Diem danh</button>}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Create Session Modal */}
      <Modal isOpen={showSessionModal} onClose={() => setShowSessionModal(false)} title="Tao buoi hoc moi">
        <form onSubmit={handleCreateSession}>
          <div className="form-group"><label className="form-label">Ngay hoc</label><input type="date" className="form-input" value={sessionForm.session_date} onChange={(e) => setSessionForm(f => ({ ...f, session_date: e.target.value }))} required /></div>
          <div className="form-group" style={{marginTop:'16px'}}><label className="form-label">Noi dung bai hoc</label><input className="form-input" value={sessionForm.topic} onChange={(e) => setSessionForm(f => ({ ...f, topic: e.target.value }))} /></div>
          <div className="modal-footer" style={{marginTop:'24px',padding:0,border:'none'}}><button type="button" className="btn btn-ghost" onClick={() => setShowSessionModal(false)}>Huy</button><button type="submit" className="btn btn-primary">Tao</button></div>
        </form>
      </Modal>

      {/* Attendance Modal */}
      <Modal isOpen={showAttendanceModal} onClose={() => setShowAttendanceModal(false)} title={`Diem danh - Buoi ${selectedSession?.session_number}`} size="lg">
        <div className="table-container" style={{ border: 'none' }}>
          <table className="table">
            <thead><tr><th>Hoc sinh</th><th>Trang thai</th><th>Ghi chu</th></tr></thead>
            <tbody>
              {attendanceData.map((a, idx) => (
                <tr key={a.enrollment_id}>
                  <td><strong>{a.student_name}</strong></td>
                  <td>
                    <select className="form-select" value={a.status} onChange={(e) => {
                      const updated = [...attendanceData];
                      updated[idx].status = e.target.value;
                      setAttendanceData(updated);
                    }}>
                      <option value="present">Co mat</option><option value="absent">Vang</option><option value="late">Di muon</option><option value="excused">Co phep</option>
                    </select>
                  </td>
                  <td><input className="form-input" placeholder="Ghi chu..." value={a.note} onChange={(e) => { const u = [...attendanceData]; u[idx].note = e.target.value; setAttendanceData(u); }} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="modal-footer" style={{marginTop:'16px',padding:0,border:'none'}}><button className="btn btn-ghost" onClick={() => setShowAttendanceModal(false)}>Huy</button><button className="btn btn-primary" onClick={handleMarkAttendance}>Luu diem danh</button></div>
      </Modal>
    </div>
  );
};

export default Attendance;
