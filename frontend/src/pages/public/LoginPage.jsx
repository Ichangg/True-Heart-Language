import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuthStore from '../../store/authStore';
import './LoginPage.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuthStore();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const user = await login(username, password);
      const paths = { admin: '/admin', teacher: '/teacher', student: '/student', parent: '/parent' };
      navigate(paths[user.role] || '/');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-bg">
        <div className="login-bg-circle c1"></div>
        <div className="login-bg-circle c2"></div>
        <div className="login-bg-circle c3"></div>
      </div>

      <div className="login-container animate-fadeInScale">
        <div className="login-header">
          <div className="login-logo">🎓</div>
          <h1 className="login-title">English Center</h1>
          <p className="login-subtitle">Dang nhap vao he thong quan ly</p>
        </div>

        <form onSubmit={handleSubmit} className="login-form">
          {error && (
            <div className="login-error">
              <span>⚠</span> {error}
            </div>
          )}

          <div className="form-group">
            <label className="form-label" htmlFor="login-username">Ten dang nhap</label>
            <input
              id="login-username"
              type="text"
              className="form-input"
              placeholder="Nhap ten dang nhap..."
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              autoFocus
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="login-password">Mat khau</label>
            <input
              id="login-password"
              type="password"
              className="form-input"
              placeholder="Nhap mat khau..."
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary login-btn"
            disabled={loading}
          >
            {loading ? 'Dang xu ly...' : 'Dang nhap'}
          </button>
        </form>

        <div className="login-footer">
          <p>Tai khoan demo:</p>
          <div className="demo-accounts">
            <span className="demo-badge">admin / 123456</span>
            <span className="demo-badge">teacher1 / 123456</span>
            <span className="demo-badge">student1 / 123456</span>
            <span className="demo-badge">parent1 / 123456</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
