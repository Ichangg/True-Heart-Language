import { useState, useEffect } from "react";
import { userService, enrollmentService, paymentService } from "../../services";
import Loading from "../../components/Loading/Loading";
import { formatCurrency } from "../../utils/formatCurrency";
import useAuthStore from "../../store/authStore";

const ParentDashboard = () => {
  const [children, setChildren] = useState([]);
  const [childDetails, setChildDetails] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuthStore();

  useEffect(() => {
    loadChildren();
  }, []);

  const loadChildren = async () => {
    try {
      const res = await userService.getChildrenByParent(user.id);
      const links = res.data.data || [];
      setChildren(links);

      // Load enrollment details for each child
      const details = [];
      for (const link of links) {
        try {
          const enrollRes = await enrollmentService.getByStudent(
            link.student.id,
          );
          const enrollments = enrollRes.data.data || [];

          // Load balance for each enrollment
          for (const enrollment of enrollments) {
            try {
              const balanceRes = await paymentService.getBalance(enrollment.id);
              enrollment.balance = balanceRes.data.data;
            } catch (e) {
              enrollment.balance = null;
            }
          }

          details.push({
            student: link.student,
            relationship: link.relationship,
            enrollments,
          });
        } catch (e) {
          details.push({
            student: link.student,
            relationship: link.relationship,
            enrollments: [],
          });
        }
      }
      setChildDetails(details);
    } catch (err) {
      setChildren([]);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Xin chào, {user?.full_name}!</h1>
          <p className="page-subtitle">Thông tin học tập của con em bạn</p>
        </div>
      </div>

      {childDetails.map((child, idx) => (
        <div key={idx} style={{ marginBottom: "32px" }}>
          <h2
            style={{
              fontSize: "1.3rem",
              fontWeight: 700,
              marginBottom: "16px",
              display: "flex",
              alignItems: "center",
              gap: "8px",
            }}
          >
            <span style={{ fontSize: "1.5rem" }}>👶</span>{" "}
            {child.student.full_name}
            <span className="badge badge-blue" style={{ marginLeft: "8px" }}>
              {child.relationship}
            </span>
          </h2>

          {child.enrollments.length === 0 ? (
            <p style={{ color: "var(--text-secondary)" }}>
              Chưa đăng ký lớp nào
            </p>
          ) : (
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(400px, 1fr))",
                gap: "20px",
              }}
            >
              {child.enrollments.map((e) => (
                <div key={e.id} className="card">
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                      marginBottom: "16px",
                    }}
                  >
                    <h3 style={{ fontSize: "1.05rem", fontWeight: 700 }}>
                      {e.class?.name} ({e.class?.year})
                    </h3>
                    <span className="badge badge-green">Đang học</span>
                  </div>

                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      gap: "10px",
                      fontSize: "0.875rem",
                    }}
                  >
                    {e.class?.teacher && (
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                          color: "var(--text-secondary)",
                        }}
                      >
                        <span>Giáo viên:</span>
                        <span style={{ color: "var(--text-primary)" }}>
                          {e.class.teacher.full_name}
                        </span>
                      </div>
                    )}
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        color: "var(--text-secondary)",
                      }}
                    >
                      <span>Đã học:</span>
                      <span className="text-success font-semibold">
                        {e.attended_sessions} buổi
                      </span>
                    </div>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        color: "var(--text-secondary)",
                      }}
                    >
                      <span>Vắng mặt:</span>
                      <span className="text-danger font-semibold">
                        {e.absent_sessions} buổi
                      </span>
                    </div>

                    {e.balance && (
                      <>
                        <div
                          style={{
                            height: "1px",
                            background: "var(--border-color)",
                            margin: "8px 0",
                          }}
                        ></div>
                        <div
                          style={{
                            display: "flex",
                            justifyContent: "space-between",
                            color: "var(--text-secondary)",
                          }}
                        >
                          <span>Học phí (sau giảm):</span>
                          <span style={{ color: "var(--text-primary)" }}>
                            {formatCurrency(e.balance.total_after_discount)}
                          </span>
                        </div>
                        {e.discount_percent > 0 && (
                          <div
                            style={{
                              display: "flex",
                              justifyContent: "space-between",
                              color: "var(--text-secondary)",
                            }}
                          >
                            <span>Giảm giá:</span>
                            <span className="text-success">
                              {e.discount_percent}% (-
                              {formatCurrency(e.balance.discount_amount)})
                            </span>
                          </div>
                        )}
                        <div
                          style={{
                            display: "flex",
                            justifyContent: "space-between",
                            color: "var(--text-secondary)",
                          }}
                        >
                          <span>Đã đóng:</span>
                          <span className="text-success font-semibold">
                            {formatCurrency(e.balance.total_paid)}
                          </span>
                        </div>
                        <div
                          style={{
                            display: "flex",
                            justifyContent: "space-between",
                            color: "var(--text-secondary)",
                          }}
                        >
                          <span>Còn thiếu:</span>
                          <span
                            className={`font-bold ${e.balance.outstanding > 0 ? "text-danger" : "text-success"}`}
                          >
                            {formatCurrency(e.balance.outstanding)}
                          </span>
                        </div>
                      </>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      ))}

      {childDetails.length === 0 && (
        <div className="empty-state">
          <div className="empty-icon">👶</div>
          <p className="empty-text">Chưa có thông tin con em</p>
        </div>
      )}
    </div>
  );
};

export default ParentDashboard;
