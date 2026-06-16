import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Slider from "../../components/Slider/Slider";
import Popup from "../../components/Popup/Popup";
import useAuthStore from "../../store/authStore";
import { promotionService } from "../../services";
import "./HomePage.css";

const HomePage = () => {
  const [sliders, setSliders] = useState([]);
  const [popup, setPopup] = useState(null);
  const [showPopup, setShowPopup] = useState(false);
  const { isAuthenticated, user } = useAuthStore();
  const navigate = useNavigate();

  useEffect(() => {
    loadPromotions();
  }, []);

  const loadPromotions = async () => {
    try {
      const res = await promotionService.getActive();
      const promotions = res.data.data || [];
      setSliders(promotions.filter((p) => p.type === "slider"));
      const popupItem = promotions.find((p) => p.type === "popup");
      if (popupItem) {
        setPopup(popupItem);
        setTimeout(() => setShowPopup(true), 1500);
      }
    } catch (err) {
      // Use default data if API not available
      setSliders([
        {
          title: "Khai giảng lớp Tiếng Anh Mùa Hè 2024",
          description:
            "Đăng ký ngay để nhận ưu đãi giảm 20% học phí tháng đầu tiên!",
          type: "slider",
        },
        {
          title: "Ưu đãi đặc biệt - Giới thiệu bạn mới",
          description:
            "Giới thiệu bạn mới đăng ký học, cả hai cùng được giảm 10% học phí!",
          type: "slider",
        },
      ]);
    }
  };

  const handleNavigate = () => {
    if (isAuthenticated) {
      const paths = {
        admin: "/admin",
        teacher: "/teacher",
        student: "/student",
        parent: "/parent",
      };
      navigate(paths[user?.role] || "/login");
    } else {
      navigate("/login");
    }
  };

  return (
    <div className="homepage">
      {/* Navigation */}
      <nav className="home-nav">
        <div className="home-nav-inner">
          <div className="home-logo">
            <span className="home-logo-icon">🎓</span>
            <span className="home-logo-text">English Center</span>
          </div>
          <button className="btn btn-primary" onClick={handleNavigate}>
            {isAuthenticated ? "Vao Dashboard" : "Dang nhap"}
          </button>
        </div>
      </nav>

      {/* Hero Section with Slider */}
      <section className="hero-section">
        {sliders.length > 0 ? (
          <Slider items={sliders} />
        ) : (
          <div className="hero-default">
            <div className="hero-content">
              <h1 className="hero-title">Trung tâm Tiếng Anh</h1>
              <p className="hero-subtitle">
                Nơi khởi đầu cho tương lai của con bạn
              </p>
              <button
                className="btn btn-primary btn-lg"
                onClick={handleNavigate}
              >
                Tìm hiểu ngay
              </button>
            </div>
          </div>
        )}
      </section>

      {/* Features */}
      <section className="features-section">
        <h2 className="section-title">Tại sao chọn chúng tôi?</h2>
        <div className="features-grid">
          <div
            className="feature-card animate-fadeInUp"
            style={{ animationDelay: "0.1s" }}
          >
            <div className="feature-icon">👨‍🏫</div>
            <h3>Giáo viên chuyên nghiệp</h3>
            <p>
              Đội ngũ giáo viên có kinh nghiệm, tận tâm và nhiệt huyết với nghề
            </p>
          </div>
          <div
            className="feature-card animate-fadeInUp"
            style={{ animationDelay: "0.2s" }}
          >
            <div className="feature-icon">📖</div>
            <h3>Giáo trình chuẩn quốc tế</h3>
            <p>
              Sử dụng giáo trình Cambridge, Oxford phù hợp với từng lứa tuổi
            </p>
          </div>
          <div
            className="feature-card animate-fadeInUp"
            style={{ animationDelay: "0.3s" }}
          >
            <div className="feature-icon">👨‍👩‍👧‍👦</div>
            <h3>Lớp học ít người</h3>
            <p>Tối đa 20 học sinh/lớp, đảm bảo chất lượng giảng dạy</p>
          </div>
          <div
            className="feature-card animate-fadeInUp"
            style={{ animationDelay: "0.4s" }}
          >
            <div className="feature-icon">📱</div>
            <h3>Theo dõi tiến độ</h3>
            <p>Phụ huynh dễ dàng theo dõi kết quả học tập qua ứng dụng</p>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="home-footer">
        <p>
          &copy; 2024 English Center Management System. All rights reserved.
        </p>
      </footer>

      {/* Popup */}
      {popup && (
        <Popup
          isOpen={showPopup}
          onClose={() => setShowPopup(false)}
          title={popup.title}
          description={popup.description}
          imageUrl={popup.image_url}
          linkUrl={popup.link_url}
        />
      )}
    </div>
  );
};

export default HomePage;
