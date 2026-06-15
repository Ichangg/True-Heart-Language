import { useState, useEffect } from 'react';
import './Slider.css';

const Slider = ({ items = [], autoPlay = true, interval = 5000 }) => {
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    if (!autoPlay || items.length <= 1) return;
    const timer = setInterval(() => {
      setCurrentIndex((prev) => (prev + 1) % items.length);
    }, interval);
    return () => clearInterval(timer);
  }, [items.length, autoPlay, interval]);

  if (!items.length) return null;

  return (
    <div className="slider">
      <div className="slider-track" style={{ transform: `translateX(-${currentIndex * 100}%)` }}>
        {items.map((item, idx) => (
          <div key={idx} className="slider-slide">
            <div className="slide-content">
              {item.image_url && <img src={item.image_url} alt={item.title} className="slide-image" />}
              <div className="slide-overlay">
                <h3 className="slide-title">{item.title}</h3>
                <p className="slide-description">{item.description}</p>
              </div>
            </div>
          </div>
        ))}
      </div>

      {items.length > 1 && (
        <>
          <button className="slider-btn slider-prev" onClick={() => setCurrentIndex((prev) => (prev - 1 + items.length) % items.length)}>
            ‹
          </button>
          <button className="slider-btn slider-next" onClick={() => setCurrentIndex((prev) => (prev + 1) % items.length)}>
            ›
          </button>
          <div className="slider-dots">
            {items.map((_, idx) => (
              <button
                key={idx}
                className={`slider-dot ${idx === currentIndex ? 'active' : ''}`}
                onClick={() => setCurrentIndex(idx)}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default Slider;
