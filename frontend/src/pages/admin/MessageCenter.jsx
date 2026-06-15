import { useState, useEffect } from "react";
import { messageService } from "../../services";
import Loading from "../../components/Loading/Loading";
import { CHANNEL_LABELS } from "../../utils/constants";
import { formatDateTime } from "../../utils/formatDate";
import "./AdminPages.css";

const MessageCenter = () => {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState("send");
  const [formData, setFormData] = useState({
    recipient_type: "individual",
    recipient_ids: "",
    class_id: "",
    channel: "system",
    subject: "",
    content: "",
  });
  const [reminderData, setReminderData] = useState({
    parent_ids: "",
    channel: "system",
  });
  const [emergencyData, setEmergencyData] = useState({
    class_id: "",
    content: "",
    channel: "system",
  });

  useEffect(() => {
    if (tab === "history") loadHistory();
  }, [tab]);

  const loadHistory = async () => {
    try {
      const res = await messageService.getHistory({ limit: 50 });
      setMessages(res.data.data || []);
    } catch (err) {
      setMessages([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSend = async (e) => {
    e.preventDefault();
    try {
      const data = {
        ...formData,
        recipient_ids: formData.recipient_ids
          ? formData.recipient_ids.split(",").map(Number)
          : [],
      };
      await messageService.send(data);
      alert("Da gui tin nhan!");
      setFormData({
        recipient_type: "individual",
        recipient_ids: "",
        class_id: "",
        channel: "system",
        subject: "",
        content: "",
      });
    } catch (err) {
      alert(err.response?.data?.message || "Loi");
    }
  };

  const handleReminder = async (e) => {
    e.preventDefault();
    try {
      const data = {
        parent_ids: reminderData.parent_ids.split(",").map(Number),
        channel: reminderData.channel,
      };
      await messageService.sendAbsenceReminder(data);
      alert("Da gui nhac nho!");
    } catch (err) {
      alert(err.response?.data?.message || "Loi");
    }
  };

  const handleEmergency = async (e) => {
    e.preventDefault();
    try {
      await messageService.sendEmergency(emergencyData);
      alert("Da gui thong bao khan cap!");
    } catch (err) {
      alert(err.response?.data?.message || "Loi");
    }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Trung tâm tin nhắn</h1>
          <p className="page-subtitle">
            Gửi thông báo cho phụ huynh qua Zalo, Facebook, SMS
          </p>
        </div>
      </div>

      <div className="tabs">
        <button
          className={`tab ${tab === "send" ? "active" : ""}`}
          onClick={() => setTab("send")}
        >
          Gửi tin nhắn
        </button>
        <button
          className={`tab ${tab === "reminder" ? "active" : ""}`}
          onClick={() => setTab("reminder")}
        >
          Nhắc nhở tự động
        </button>
        <button
          className={`tab ${tab === "emergency" ? "active" : ""}`}
          onClick={() => setTab("emergency")}
        >
          Thông báo khẩn cấp
        </button>
        <button
          className={`tab ${tab === "history" ? "active" : ""}`}
          onClick={() => setTab("history")}
        >
          Lịch sử
        </button>
      </div>

      {tab === "send" && (
        <div className="card">
          <form className="message-form" onSubmit={handleSend}>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Loại gửi</label>
                <select
                  className="form-select"
                  value={formData.recipient_type}
                  onChange={(e) =>
                    setFormData((f) => ({
                      ...f,
                      recipient_type: e.target.value,
                    }))
                  }
                >
                  <option value="individual">Cá nhân</option>
                  <option value="class">Ca lớp</option>
                  <option value="all">Tất cả</option>
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Kênh</label>
                <select
                  className="form-select"
                  value={formData.channel}
                  onChange={(e) =>
                    setFormData((f) => ({ ...f, channel: e.target.value }))
                  }
                >
                  <option value="system">Hệ thống</option>
                  <option value="zalo">Zalo</option>
                  <option value="facebook">Facebook</option>
                  <option value="sms">SMS</option>
                </select>
              </div>
            </div>
            {formData.recipient_type === "individual" && (
              <div className="form-group">
                <label className="form-label">
                  ID phụ huynh (cách nhau bởi dấu phẩy)
                </label>
                <input
                  className="form-input"
                  placeholder="VD: 1,2,3"
                  value={formData.recipient_ids}
                  onChange={(e) =>
                    setFormData((f) => ({
                      ...f,
                      recipient_ids: e.target.value,
                    }))
                  }
                />
              </div>
            )}
            {formData.recipient_type === "class" && (
              <div className="form-group">
                <label className="form-label">ID Lớp</label>
                <input
                  className="form-input"
                  type="number"
                  value={formData.class_id}
                  onChange={(e) =>
                    setFormData((f) => ({ ...f, class_id: e.target.value }))
                  }
                />
              </div>
            )}
            <div className="form-group">
              <label className="form-label">Tiêu đề</label>
              <input
                className="form-input"
                value={formData.subject}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, subject: e.target.value }))
                }
              />
            </div>
            <div className="form-group">
              <label className="form-label">Nội dung</label>
              <textarea
                className="form-textarea"
                rows="5"
                value={formData.content}
                onChange={(e) =>
                  setFormData((f) => ({ ...f, content: e.target.value }))
                }
                required
              />
            </div>
            <button type="submit" className="btn btn-primary">
              Gửi tin nhắn
            </button>
          </form>
        </div>
      )}

      {tab === "reminder" && (
        <div className="card">
          <p style={{ color: "var(--text-secondary)", marginBottom: "16px" }}>
            Tự động gửi thông báo số buổi vắng + tiền chưa đóng cho phụ huynh
          </p>
          <form className="message-form" onSubmit={handleReminder}>
            <div className="form-group">
              <label className="form-label">
                ID phụ huynh (cách nhau bởi dấu phẩy)
              </label>
              <input
                className="form-input"
                placeholder="VD: 7,8,9"
                value={reminderData.parent_ids}
                onChange={(e) =>
                  setReminderData((f) => ({ ...f, parent_ids: e.target.value }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Kênh</label>
              <select
                className="form-select"
                value={reminderData.channel}
                onChange={(e) =>
                  setReminderData((f) => ({ ...f, channel: e.target.value }))
                }
              >
                <option value="system">Hệ thống</option>
                <option value="zalo">Zalo</option>
                <option value="facebook">Facebook</option>
                <option value="sms">SMS</option>
              </select>
            </div>
            <button type="submit" className="btn btn-warning">
              Gửi nhắc nhở
            </button>
          </form>
        </div>
      )}

      {tab === "emergency" && (
        <div className="card">
          <p style={{ color: "var(--text-secondary)", marginBottom: "16px" }}>
            Gửi thông báo nghỉ học khẩn cấp cho toàn bộ phụ huynh trong lớp
          </p>
          <form className="message-form" onSubmit={handleEmergency}>
            <div className="form-group">
              <label className="form-label">ID Lớp</label>
              <input
                className="form-input"
                type="number"
                value={emergencyData.class_id}
                onChange={(e) =>
                  setEmergencyData((f) => ({ ...f, class_id: e.target.value }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Nội dung</label>
              <textarea
                className="form-textarea"
                rows="4"
                placeholder="VD: Lớp nghỉ học ngày 15/10 do giáo viên bị ốm..."
                value={emergencyData.content}
                onChange={(e) =>
                  setEmergencyData((f) => ({ ...f, content: e.target.value }))
                }
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Kênh</label>
              <select
                className="form-select"
                value={emergencyData.channel}
                onChange={(e) =>
                  setEmergencyData((f) => ({ ...f, channel: e.target.value }))
                }
              >
                <option value="system">Hệ thống</option>
                <option value="zalo">Zalo</option>
                <option value="facebook">Facebook</option>
                <option value="sms">SMS</option>
              </select>
            </div>
            <button type="submit" className="btn btn-danger">
              Gửi thông báo khẩn cấp
            </button>
          </form>
        </div>
      )}

      {tab === "history" && (
        <div className="table-container">
          {loading ? (
            <Loading />
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Người gửi</th>
                  <th>Kênh</th>
                  <th>Tiêu đề</th>
                  <th>Trạng thái</th>
                  <th>Thời gian</th>
                </tr>
              </thead>
              <tbody>
                {messages.map((m) => (
                  <tr key={m.id}>
                    <td>{m.sender?.full_name || "—"}</td>
                    <td>
                      <span className="badge badge-blue">
                        {CHANNEL_LABELS[m.channel]}
                      </span>
                    </td>
                    <td>{m.subject || m.content?.substring(0, 50) + "..."}</td>
                    <td>
                      <span
                        className={`badge ${m.status === "sent" ? "badge-green" : m.status === "failed" ? "badge-red" : "badge-orange"}`}
                      >
                        {m.status === "sent"
                          ? "Da gui"
                          : m.status === "failed"
                            ? "That bai"
                            : "Cho xu ly"}
                      </span>
                    </td>
                    <td>{formatDateTime(m.sent_at || m.created_at)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
};

export default MessageCenter;
