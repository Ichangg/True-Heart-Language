import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Slider from '../../components/Slider/Slider';
import Popup from '../../components/Popup/Popup';
import useAuthStore from '../../store/authStore';
import { promotionService } from '../../services';
import './HomePage.css';

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
      setSliders(promotions.filter(p => p.type === 'slider'));
      const popupItem = promotions.find(p => p.type === 'popup');
      if (popupItem) {
        setPopup(popupItem);
        setTimeout(() => setShowPopup(true), 1500);
      }
    } catch (err) {
      // Use default data if API not available
      setSliders([
        { title: 'Khai giang Lop Tieng Anh Mua He 2024', description: 'Dang ky ngay de nhan uu dai giam 20% hoc phi thang dau tien!', type: 'slider' },
        { title: 'Uu dai dac biet - Gioi thieu ban moi', description: 'Gioi thieu ban moi dang ky hoc, ca hai cung duoc giam 10% hoc phi!', type: 'slider' },
      ]);
    }
  };

  const handleNavigate = () => {
    if (isAuthenticated) {
      const paths = { admin: '/admin', teacher: '/teacher', student: '/student', parent: '/parent' };
      navigate(paths[user?.role] || '/login');
    } else {
      navigate('/login');
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
            {isAuthenticated ? 'Vao Dashboard' : 'Dang nhap'}
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
              <h1 className="hero-title">Trung Tam Tieng Anh</h1>
              <p className="hero-subtitle">Noi khoi dau cho tuong lai cua con ban</p>
              <button className="btn btn-primary btn-lg" onClick={handleNavigate}>
                Tim hieu ngay
              </button>
            </div>
          </div>
        )}
      </section>

      {/* Features */}
      <section className="features-section">
        <h2 className="section-title">Tai sao chon chung toi?</h2>
        <div className="features-grid">
          <div className="feature-card animate-fadeInUp" style={{ animationDelay: '0.1s' }}>
            <div className="feature-icon">👨‍🏫</div>
            <h3>Giao vien chuyen nghiep</h3>
            <p>Doi ngu giao vien co kinh nghiem, tan tam va nhiet huyet voi nghe</p>
          </div>
          <div className="feature-card animate-fadeInUp" style={{ animationDelay: '0.2s' }}>
            <div className="feature-icon">📖</div>
            <h3>Giao trinh chuan quoc te</h3>
            <p>Su dung giao trinh Cambridge, Oxford phu hop voi tung lua tuoi</p>
          </div>
          <div className="feature-card animate-fadeInUp" style={{ animationDelay: '0.3s' }}>
            <div className="feature-icon">👨‍👩‍👧‍👦</div>
            <h3>Lop hoc it nguoi</h3>
            <p>Toi da 20 hoc sinh/lop, dam bao chat luong giang day</p>
          </div>
          <div className="feature-card animate-fadeInUp" style={{ animationDelay: '0.4s' }}>
            <div className="feature-icon">📱</div>
            <h3>Theo doi tien do</h3>
            <p>Phu huynh de dang theo doi ket qua hoc tap qua ung dung</p>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="home-footer">
        <p>&copy; 2024 English Center Management System. All rights reserved.</p>
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
