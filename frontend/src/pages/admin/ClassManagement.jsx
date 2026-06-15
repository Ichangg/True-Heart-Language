import { useState, useEffect } from "react";
import { classService } from "../../services";
import Modal from "../../components/Modal/Modal";
import Loading from "../../components/Loading/Loading";
import { formatCurrency } from "../../utils/formatCurrency";
import "./AdminPages.css";

const ClassManagement = () => {
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [filters, setFilters] = useState({
    year: new Date().getFullYear(),
    status: "",
  });
  const [formData, setFormData] = useState({
    age_group: "",
    year: new Date().getFullYear(),
    fee_per_session: "",
    sessions_per_month: 8,
    schedule_info: "",
    max_students: 20,
    teacher_id: "",
  });
  const [pagination, setPagination] = useState({ page: 1, total: 0 });

  useEffect(() => {
    loadClasses();
  }, [filters, pagination.page]);

  const loadClasses = async () => {
    try {
      const res = await classService.getAll({
        ...filters,
        page: pagination.page,
        limit: 10,
      });
      setClasses(res.data.data || []);
      setPagination((prev) => ({
        ...prev,
        total: res.data.pagination?.total || 0,
      }));
    } catch (err) {
      setClasses([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await classService.create(formData);
      setShowModal(false);
      setFormData({
        age_group: "",
        year: new Date().getFullYear(),
        fee_per_session: "",
        sessions_per_month: 8,
        schedule_info: "",
        max_students: 20,
        teacher_id: "",
      });
      loadClasses();
    } catch (err) {
      alert(err.response?.data?.message || "Lỗi tạo lớp");
    }
  };

  const handleCloseClass = async (id) => {
    if (!confirm("Bạn có chắc muốn đóng lớp này? Dữ liệu sẽ được giữ lại."))
      return;
    try {
      await classService.close(id);
      loadClasses();
    } catch (err) {
      alert(err.response?.data?.message || "Lỗi");
    }
  };

  const handleReopenClass = async (id) => {
    try {
      await classService.reopen(id);
      loadClasses();
    } catch (err) {
      alert(err.response?.data?.message || "Lỗi");
    }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Quản lý lớp học</h1>
          <p className="page-subtitle">Tạo, quản lý và đóng lớp học theo năm</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          + Tạo lớp mới
        </button>
      </div>

      {/* Filters */}
      <div className="filter-bar">
        <div className="form-group">
          <label className="form-label">Nam</label>
          <select
            className="form-select"
            value={filters.year}
            onChange={(e) =>
              setFilters((f) => ({ ...f, year: e.target.value }))
            }
          >
            {[2024, 2023, 2022, 2021, 2020, 2019, 2018].map((y) => (
              <option key={y} value={y}>
                {y}
              </option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Trạng thái</label>
          <select
            className="form-select"
            value={filters.status}
            onChange={(e) =>
              setFilters((f) => ({ ...f, status: e.target.value }))
            }
          >
            <option value="">Tất cả</option>
            <option value="active">Đang hoạt động</option>
            <option value="closed">Đã đóng</option>
          </select>
        </div>
      </div>

      {/* Table */}
      <div className="table-container">
        <table className="table">
          <thead>
            <tr>
              <th>Tên lớp</th>
              <th>Lứa tuổi</th>
              <th>Năm</th>
              <th>Giáo viên</th>
              <th>Sĩ số</th>
              <th>Học phí/buổi</th>
              <th>Lịch học</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {classes.length === 0 ? (
              <tr>
                <td
                  colSpan="9"
                  className="text-center text-muted"
                  style={{ padding: "40px" }}
                >
                  Chưa có lớp nào
                </td>
              </tr>
            ) : (
              classes.map((cls) => (
                <tr key={cls.id}>
                  <td>
                    <strong>{cls.name}</strong>
                  </td>
                  <td>Lớp {cls.age_group}</td>
                  <td>{cls.year}</td>
                  <td>{cls.teacher?.full_name || "—"}</td>
                  <td>
                    {cls.student_count}/{cls.max_students}
                  </td>
                  <td>{formatCurrency(cls.fee_per_session)}</td>
                  <td>{cls.schedule_info || "—"}</td>
                  <td>
                    <span
                      className={`badge ${cls.status === "active" ? "badge-green" : "badge-gray"}`}
                    >
                      {cls.status === "active" ? "Hoạt động" : "Đã đóng"}
                    </span>
                  </td>
                  <td>
                    <div className="class-actions">
                      {cls.status === "active" ? (
                        <button
                          className="btn btn-sm btn-ghost"
                          onClick={() => handleCloseClass(cls.id)}
                        >
                          Đóng lớp
                        </button>
                      ) : (
                        <button
                          className="btn btn-sm btn-success"
                          onClick={() => handleReopenClass(cls.id)}
                        >
                          Mở lại
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Create Modal */}
      <Modal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        title="Tao lop moi"
        size="lg"
      >
        <form onSubmit={handleCreate}>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Lứa tuổi (cấp lớp)</label>
              <input
                type="number"
                className="form-input"
                placeholder="VD: 3, 5, 7"
                value={formData.age_group}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, age_group: e.target.value }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Năm học</label>
              <input
                type="number"
                className="form-input"
                value={formData.year}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, year: e.target.value }))
                }
                required
              />
            </div>
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Học phí/buổi (VND)</label>
              <input
                type="number"
                className="form-input"
                placeholder="150000"
                value={formData.fee_per_session}
                onChange={(e) =>
                  setFormData((f) => ({
                    ...f,
                    fee_per_session: e.target.value,
                  }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Số buổi/tháng</label>
              <input
                type="number"
                className="form-input"
                value={formData.sessions_per_month}
                onChange={(e) =>
                  setFormData((f) => ({
                    ...f,
                    sessions_per_month: e.target.value,
                  }))
                }
              />
            </div>
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Sĩ số tối đa</label>
              <input
                type="number"
                className="form-input"
                value={formData.max_students}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, max_students: e.target.value }))
                }
              />
            </div>
            <div className="form-group">
              <label className="form-label">Lịch học</label>
              <input
                type="text"
                className="form-input"
                placeholder="Thứ 3 & 5, 17:30-19:00"
                value={formData.schedule_info}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, schedule_info: e.target.value }))
                }
              />
            </div>
          </div>
          <div
            className="modal-footer"
            style={{ marginTop: "24px", padding: 0, border: "none" }}
          >
            <button
              type="button"
              className="btn btn-ghost"
              onClick={() => setShowModal(false)}
            >
              Hủy
            </button>
            <button type="submit" className="btn btn-primary">
              Tạo lớp
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default ClassManagement;
