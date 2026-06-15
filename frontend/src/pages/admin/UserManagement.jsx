import { useState, useEffect } from "react";
import { userService } from "../../services";
import Modal from "../../components/Modal/Modal";
import Loading from "../../components/Loading/Loading";
import { ROLE_LABELS } from "../../utils/constants";
import "./AdminPages.css";

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showLinkModal, setShowLinkModal] = useState(false);
  const [filters, setFilters] = useState({ role: "", search: "" });
  const [formData, setFormData] = useState({
    username: "",
    password: "123456",
    full_name: "",
    email: "",
    phone: "",
    role: "student",
    address: "",
  });
  const [linkData, setLinkData] = useState({
    parent_id: "",
    student_id: "",
    relationship: "",
  });
  const [pagination, setPagination] = useState({ page: 1, total: 0 });

  useEffect(() => {
    loadUsers();
  }, [filters, pagination.page]);

  const loadUsers = async () => {
    try {
      const res = await userService.getAll({
        ...filters,
        page: pagination.page,
        limit: 10,
      });
      setUsers(res.data.data || []);
      setPagination((prev) => ({
        ...prev,
        total: res.data.pagination?.total || 0,
      }));
    } catch (err) {
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await userService.create(formData);
      setShowModal(false);
      setFormData({
        username: "",
        password: "123456",
        full_name: "",
        email: "",
        phone: "",
        role: "student",
        address: "",
      });
      loadUsers();
    } catch (err) {
      alert(err.response?.data?.message || "Lỗi tạo user");
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await userService.toggleStatus(id);
      loadUsers();
    } catch (err) {
      alert("Lỗi cập nhật trạng thái");
    }
  };

  const handleLinkParent = async (e) => {
    e.preventDefault();
    try {
      await userService.linkParentStudent(linkData);
      setShowLinkModal(false);
      setLinkData({ parent_id: "", student_id: "", relationship: "" });
      alert("Liên kết thành công!");
    } catch (err) {
      alert("Lỗi liên kết");
    }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Quản lý người dùng</h1>
          <p className="page-subtitle">
            Quản lý giáo viên, học sinh, phụ huynh
          </p>
        </div>
        <div className="page-actions">
          <button
            className="btn btn-ghost"
            onClick={() => setShowLinkModal(true)}
          >
            Liên kết PH-HS
          </button>
          <button
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            + Thêm người dùng
          </button>
        </div>
      </div>

      <div className="filter-bar">
        <div className="form-group">
          <label className="form-label">Vai tro</label>
          <select
            className="form-select"
            value={filters.role}
            onChange={(e) =>
              setFilters((f) => ({ ...f, role: e.target.value }))
            }
          >
            <option value="">Tat ca</option>
            <option value="admin">Quan tri vien</option>
            <option value="teacher">Giao vien</option>
            <option value="student">Học sinh</option>
            <option value="parent">Phụ huynh</option>
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Tìm kiếm</label>
          <input
            className="form-input"
            placeholder="Tên, email, SDT..."
            value={filters.search}
            onChange={(e) =>
              setFilters((f) => ({ ...f, search: e.target.value }))
            }
          />
        </div>
      </div>

      <div className="table-container">
        <table className="table">
          <thead>
            <tr>
              <th>Người dùng</th>
              <th>Username</th>
              <th>Email</th>
              <th>SDT</th>
              <th>Vai trò</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {users.length === 0 ? (
              <tr>
                <td
                  colSpan="7"
                  className="text-center text-muted"
                  style={{ padding: "40px" }}
                >
                  Khong tim thay
                </td>
              </tr>
            ) : (
              users.map((user) => (
                <tr key={user.id}>
                  <td>
                    <div className="user-avatar-cell">
                      <div className={`user-avatar-sm ${user.role}`}>
                        {user.full_name?.charAt(0)}
                      </div>
                      <span>{user.full_name}</span>
                    </div>
                  </td>
                  <td>
                    <code>{user.username}</code>
                  </td>
                  <td>{user.email || "—"}</td>
                  <td>{user.phone || "—"}</td>
                  <td>
                    <span
                      className={`badge badge-${user.role === "admin" ? "blue" : user.role === "teacher" ? "green" : user.role === "student" ? "orange" : "gray"}`}
                    >
                      {ROLE_LABELS[user.role]}
                    </span>
                  </td>
                  <td>
                    <span
                      className={`badge ${user.status === "active" ? "badge-green" : "badge-red"}`}
                    >
                      {user.status === "active" ? "Hoat dong" : "Vo hieu hoa"}
                    </span>
                  </td>
                  <td>
                    <button
                      className={`btn btn-sm ${user.status === "active" ? "btn-ghost" : "btn-success"}`}
                      onClick={() => handleToggleStatus(user.id)}
                    >
                      {user.status === "active" ? "Khoa" : "Mo khoa"}
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Create User Modal */}
      <Modal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        title="Thêm người dùng mới"
        size="lg"
      >
        <form onSubmit={handleCreate}>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Tên đăng nhập</label>
              <input
                className="form-input"
                value={formData.username}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, username: e.target.value }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Mật khẩu</label>
              <input
                className="form-input"
                value={formData.password}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, password: e.target.value }))
                }
              />
            </div>
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Họ tên</label>
              <input
                className="form-input"
                value={formData.full_name}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, full_name: e.target.value }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Vai trò</label>
              <select
                className="form-select"
                value={formData.role}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, role: e.target.value }))
                }
              >
                <option value="student">Học sinh</option>
                <option value="teacher">Giáo viên</option>
                <option value="parent">Phụ huynh</option>
                <option value="admin">Quản trị viên</option>
              </select>
            </div>
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input
                className="form-input"
                type="email"
                value={formData.email}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, email: e.target.value }))
                }
              />
            </div>
            <div className="form-group">
              <label className="form-label">SDT</label>
              <input
                className="form-input"
                value={formData.phone}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, phone: e.target.value }))
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
              Tạo
            </button>
          </div>
        </form>
      </Modal>

      {/* Link Parent-Student Modal */}
      <Modal
        isOpen={showLinkModal}
        onClose={() => setShowLinkModal(false)}
        title="Liên kết Phụ huynh - Học sinh"
      >
        <form onSubmit={handleLinkParent}>
          <div className="form-group">
            <label className="form-label">ID Phụ huynh</label>
            <input
              className="form-input"
              type="number"
              value={linkData.parent_id}
              onChange={(e) =>
                setLinkData((f) => ({ ...f, parent_id: e.target.value }))
              }
              required
            />
          </div>
          <div className="form-group" style={{ marginTop: "16px" }}>
            <label className="form-label">ID Học sinh</label>
            <input
              className="form-input"
              type="number"
              value={linkData.student_id}
              onChange={(e) =>
                setLinkData((f) => ({ ...f, student_id: e.target.value }))
              }
              required
            />
          </div>
          <div className="form-group" style={{ marginTop: "16px" }}>
            <label className="form-label">Quan hệ</label>
            <select
              className="form-select"
              value={linkData.relationship}
              onChange={(e) =>
                setLinkData((f) => ({ ...f, relationship: e.target.value }))
              }
            >
              <option value="">Chọn...</option>
              <option value="Bo">Bố</option>
              <option value="Me">Mẹ</option>
              <option value="Ong">Ông</option>
              <option value="Ba">Bà</option>
            </select>
          </div>
          <div
            className="modal-footer"
            style={{ marginTop: "24px", padding: 0, border: "none" }}
          >
            <button
              type="button"
              className="btn btn-ghost"
              onClick={() => setShowLinkModal(false)}
            >
              Hủy
            </button>
            <button type="submit" className="btn btn-primary">
              Liên kết
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default UserManagement;
