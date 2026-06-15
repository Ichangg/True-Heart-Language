import { useState, useEffect } from "react";
import { paymentService } from "../../services";
import Modal from "../../components/Modal/Modal";
import Loading from "../../components/Loading/Loading";
import { formatCurrency } from "../../utils/formatCurrency";
import "./AdminPages.css";

const TeacherPayment = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [filters, setFilters] = useState({
    month: new Date().getMonth() + 1,
    year: new Date().getFullYear(),
  });
  const [formData, setFormData] = useState({
    teacher_id: "",
    amount: "",
    month: new Date().getMonth() + 1,
    year: new Date().getFullYear(),
    sessions_count: "",
    note: "",
  });

  useEffect(() => {
    loadPayments();
  }, [filters]);

  const loadPayments = async () => {
    try {
      const res = await paymentService.getTeacherPayments(filters);
      setPayments(res.data.data || []);
    } catch (err) {
      setPayments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await paymentService.recordTeacherPayment(formData);
      setShowModal(false);
      loadPayments();
    } catch (err) {
      alert(err.response?.data?.message || "Loi");
    }
  };

  const totalPaid = payments.reduce(
    (sum, p) => sum + parseFloat(p.amount || 0),
    0,
  );

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Lương giáo viên</h1>
          <p className="page-subtitle">
            Quản lý thanh toán lương cho giáo viên
          </p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          + Ghi nhận lương
        </button>
      </div>

      <div className="filter-bar">
        <div className="form-group">
          <label className="form-label">Tháng</label>
          <select
            className="form-select"
            value={filters.month}
            onChange={(e) =>
              setFilters((f) => ({ ...f, month: e.target.value }))
            }
          >
            {[...Array(12)].map((_, i) => (
              <option key={i + 1} value={i + 1}>
                Tháng {i + 1}
              </option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Năm</label>
          <select
            className="form-select"
            value={filters.year}
            onChange={(e) =>
              setFilters((f) => ({ ...f, year: e.target.value }))
            }
          >
            {[2024, 2023, 2022].map((y) => (
              <option key={y} value={y}>
                {y}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="stat-card blue" style={{ marginBottom: "24px" }}>
        <div className="stat-icon">💰</div>
        <div className="stat-info">
          <div className="stat-label">
            Tổng đã trả tháng {filters.month}/{filters.year}
          </div>
          <div className="stat-value">{formatCurrency(totalPaid)}</div>
        </div>
      </div>

      <div className="table-container">
        <table className="table">
          <thead>
            <tr>
              <th>Giáo viên</th>
              <th>Lớp</th>
              <th>Số buổi</th>
              <th>Số tiền</th>
              <th>Ngày trả</th>
              <th>Ghi chú</th>
            </tr>
          </thead>
          <tbody>
            {payments.length === 0 ? (
              <tr>
                <td
                  colSpan="6"
                  className="text-center text-muted"
                  style={{ padding: "40px" }}
                >
                  Chưa có dữ liệu
                </td>
              </tr>
            ) : (
              payments.map((p) => (
                <tr key={p.id}>
                  <td>{p.teacher?.full_name || "—"}</td>
                  <td>{p.class?.name || "—"}</td>
                  <td>{p.sessions_count || "—"}</td>
                  <td className="text-success font-semibold">
                    {formatCurrency(p.amount)}
                  </td>
                  <td>
                    {p.paid_at
                      ? new Date(p.paid_at).toLocaleDateString("vi-VN")
                      : "—"}
                  </td>
                  <td>{p.note || "—"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        title="Ghi nhan luong giao vien"
      >
        <form onSubmit={handleCreate}>
          <div className="form-group">
            <label className="form-label">ID Giáo viên</label>
            <input
              className="form-input"
              type="number"
              value={formData.teacher_id}
              onChange={(e) =>
                setFormData((f) => ({ ...f, teacher_id: e.target.value }))
              }
              required
            />
          </div>
          <div className="form-group" style={{ marginTop: "16px" }}>
            <label className="form-label">Số tiền (VND)</label>
            <input
              className="form-input"
              type="number"
              value={formData.amount}
              onChange={(e) =>
                setFormData((f) => ({ ...f, amount: e.target.value }))
              }
              required
            />
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Tháng</label>
              <input
                className="form-input"
                type="number"
                min="1"
                max="12"
                value={formData.month}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, month: e.target.value }))
                }
              />
            </div>
            <div className="form-group">
              <label className="form-label">Năm</label>
              <input
                className="form-input"
                type="number"
                value={formData.year}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, year: e.target.value }))
                }
              />
            </div>
          </div>
          <div className="form-group" style={{ marginTop: "16px" }}>
            <label className="form-label">Số buổi dạy</label>
            <input
              className="form-input"
              type="number"
              value={formData.sessions_count}
              onChange={(e) =>
                setFormData((f) => ({ ...f, sessions_count: e.target.value }))
              }
            />
          </div>
          <div className="form-group" style={{ marginTop: "16px" }}>
            <label className="form-label">Ghi chú</label>
            <textarea
              className="form-textarea"
              value={formData.note}
              onChange={(e) =>
                setFormData((f) => ({ ...f, note: e.target.value }))
              }
            />
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
              Lưu
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default TeacherPayment;
