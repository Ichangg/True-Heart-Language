import { useState, useEffect } from "react";
import { promotionService } from "../../services";
import Modal from "../../components/Modal/Modal";
import Loading from "../../components/Loading/Loading";
import "./AdminPages.css";

const PromotionManagement = () => {
  const [promotions, setPromotions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    type: "slider",
    image_url: "",
    link_url: "",
    start_date: "",
    end_date: "",
  });

  useEffect(() => {
    loadPromotions();
  }, []);

  const loadPromotions = async () => {
    try {
      const res = await promotionService.getAll({ limit: 50 });
      setPromotions(res.data.data || []);
    } catch (err) {
      setPromotions([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      const payload = { ...formData };
      if (!payload.start_date) payload.start_date = null;
      if (!payload.end_date) payload.end_date = null;
      await promotionService.create(payload);
      setShowModal(false);
      setFormData({
        title: "",
        description: "",
        type: "slider",
        image_url: "",
        link_url: "",
        start_date: "",
        end_date: "",
      });
      loadPromotions();
    } catch (err) {
      alert(err.response?.data?.message || "Loi");
    }
  };

  const handleToggle = async (id) => {
    try {
      await promotionService.toggleActive(id);
      loadPromotions();
    } catch (err) {
      alert("Lỗi");
    }
  };

  const handleDelete = async (id) => {
    if (!confirm("Xóa quảng cáo này?")) return;
    try {
      await promotionService.delete(id);
      loadPromotions();
    } catch (err) {
      alert("Lỗi");
    }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Quảng cáo trang chủ</h1>
          <p className="page-subtitle">
            Quản lý popup, slider quảng cáo lớp mới
          </p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          + Thêm quảng cáo
        </button>
      </div>

      <div className="promo-card-grid">
        {promotions.map((promo) => (
          <div key={promo.id} className="promo-card">
            <div className="promo-card-image">
              {promo.image_url ? (
                <img src={promo.image_url} alt={promo.title} />
              ) : (
                "📢"
              )}
            </div>
            <div className="promo-card-body">
              <div className="promo-card-title">{promo.title}</div>
              <div className="promo-card-desc">{promo.description}</div>
              <div className="promo-card-footer">
                <span
                  className={`badge ${promo.is_active ? "badge-green" : "badge-gray"}`}
                >
                  {promo.is_active ? "Dang hien" : "An"}
                </span>
                <span className={`badge badge-blue`}>{promo.type}</span>
              </div>
              <div style={{ display: "flex", gap: "8px", marginTop: "12px" }}>
                <button
                  className="btn btn-sm btn-ghost"
                  onClick={() => handleToggle(promo.id)}
                >
                  {promo.is_active ? "An" : "Hien"}
                </button>
                <button
                  className="btn btn-sm btn-danger"
                  onClick={() => handleDelete(promo.id)}
                >
                  Xóa
                </button>
              </div>
            </div>
          </div>
        ))}
        {promotions.length === 0 && (
          <div className="empty-state" style={{ gridColumn: "1/-1" }}>
            <div className="empty-icon">📢</div>
            <p className="empty-text">Chưa có quảng cáo nào</p>
          </div>
        )}
      </div>

      <Modal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        title="Thêm quảng cáo mới"
        size="lg"
      >
        <form onSubmit={handleCreate}>
          <div className="form-group">
            <label className="form-label">Tiêu đề</label>
            <input
              className="form-input"
              value={formData.title}
              onChange={(e) =>
                setFormData((f) => ({ ...f, title: e.target.value }))
              }
              required
            />
          </div>
          <div className="form-group" style={{ marginTop: "16px" }}>
            <label className="form-label">Mô tả</label>
            <textarea
              className="form-textarea"
              value={formData.description}
              onChange={(e) =>
                setFormData((f) => ({ ...f, description: e.target.value }))
              }
            />
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Loại</label>
              <select
                className="form-select"
                value={formData.type}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, type: e.target.value }))
                }
              >
                <option value="slider">Slider</option>
                <option value="popup">Popup</option>
                <option value="banner">Banner</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Link ảnh (URL)</label>
              <input
                className="form-input"
                value={formData.image_url}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, image_url: e.target.value }))
                }
              />
            </div>
          </div>
          <div className="form-row" style={{ marginTop: "16px" }}>
            <div className="form-group">
              <label className="form-label">Ngày bắt đầu</label>
              <input
                type="date"
                className="form-input"
                value={formData.start_date}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, start_date: e.target.value }))
                }
              />
            </div>
            <div className="form-group">
              <label className="form-label">Ngày kết thúc</label>
              <input
                type="date"
                className="form-input"
                value={formData.end_date}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, end_date: e.target.value }))
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
    </div>
  );
};

export default PromotionManagement;
