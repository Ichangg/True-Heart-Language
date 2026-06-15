import { useState, useEffect } from 'react';
import './Popup.css';

const Popup = ({ isOpen, onClose, title, description, imageUrl, linkUrl }) => {
  const [show, setShow] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setTimeout(() => setShow(true), 100);
    } else {
      setShow(false);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  return (
    <div className="popup-overlay" onClick={onClose}>
      <div className={`popup-container ${show ? 'show' : ''}`} onClick={(e) => e.stopPropagation()}>
        <button className="popup-close" onClick={onClose}>✕</button>
        {imageUrl && (
          <div className="popup-image-wrapper">
            <img src={imageUrl} alt={title} className="popup-image" />
          </div>
        )}
        <div className="popup-content">
          <h3 className="popup-title">{title}</h3>
          <p className="popup-description">{description}</p>
          {linkUrl && (
            <a href={linkUrl} className="btn btn-primary popup-cta" target="_blank" rel="noopener noreferrer">
              Tìm hiểu thêm
            </a>
          )}
          <button className="btn btn-ghost popup-dismiss" onClick={onClose}>
            Đóng
          </button>
        </div>
      </div>
    </div>
  );
};

export default Popup;
