import { useState, useEffect } from "react";
import { statisticService, paymentService } from "../../services";
import { formatCurrency } from "../../utils/formatCurrency";
import Loading from "../../components/Loading/Loading";
import "./AdminPages.css";

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const res = await statisticService.getDashboard();
      setStats(res.data.data);
    } catch (err) {
      // Fallback demo data
      setStats({
        total_students: 48,
        total_teachers: 5,
        total_parents: 32,
        active_classes: 8,
        closed_classes: 3,
        monthly_revenue: 45600000,
        monthly_teacher_cost: 18000000,
        monthly_profit: 27600000,
      });
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;

  return (
    <div className="admin-dashboard">
      <div className="page-header">
        <div>
          <h1 className="page-title">Tổng quan</h1>
          <p className="page-subtitle">
            Xin chào! Đây là tổng quan trung tâm của bạn.
          </p>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="stats-grid">
        <div className="stat-card blue">
          <div className="stat-icon">👨‍🎓</div>
          <div className="stat-info">
            <div className="stat-label">Học sinh</div>
            <div className="stat-value">{stats?.total_students || 0}</div>
          </div>
        </div>

        <div className="stat-card green">
          <div className="stat-icon">👨‍🏫</div>
          <div className="stat-info">
            <div className="stat-label">Giáo viên</div>
            <div className="stat-value">{stats?.total_teachers || 0}</div>
          </div>
        </div>

        <div className="stat-card orange">
          <div className="stat-icon">📚</div>
          <div className="stat-info">
            <div className="stat-label">Lớp đang hoạt động</div>
            <div className="stat-value">{stats?.active_classes || 0}</div>
          </div>
        </div>

        <div className="stat-card red">
          <div className="stat-icon">👨‍👩‍👧</div>
          <div className="stat-info">
            <div className="stat-label">Phụ huynh</div>
            <div className="stat-value">{stats?.total_parents || 0}</div>
          </div>
        </div>
      </div>

      {/* Financial Summary */}
      <div className="dashboard-grid">
        <div className="card finance-card">
          <div className="card-header">
            <h3 className="card-title">Tài chính tháng này</h3>
          </div>
          <div className="finance-items">
            <div className="finance-item">
              <span className="finance-label">Doanh thu</span>
              <span className="finance-value text-success">
                {formatCurrency(stats?.monthly_revenue)}
              </span>
            </div>
            <div className="finance-item">
              <span className="finance-label">Chi phí giáo viên</span>
              <span className="finance-value text-danger">
                {formatCurrency(stats?.monthly_teacher_cost)}
              </span>
            </div>
            <div className="finance-divider"></div>
            <div className="finance-item">
              <span className="finance-label font-bold">Lợi nhuận</span>
              <span className="finance-value font-bold text-accent">
                {formatCurrency(stats?.monthly_profit)}
              </span>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-header">
            <h3 className="card-title">Thông tin nhanh</h3>
          </div>
          <div className="quick-info-list">
            <div className="quick-info-item">
              <span className="badge badge-green">Hoat động</span>
              <span>{stats?.active_classes} lớp</span>
            </div>
            <div className="quick-info-item">
              <span className="badge badge-gray">Đã đóng</span>
              <span>{stats?.closed_classes} lớp</span>
            </div>
            <div className="quick-info-item">
              <span className="badge badge-blue">Tổng</span>
              <span>
                {(stats?.active_classes || 0) + (stats?.closed_classes || 0)}{" "}
                lớp
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
