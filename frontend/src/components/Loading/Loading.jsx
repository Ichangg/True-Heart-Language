import './Loading.css';

const Loading = ({ text = 'Đang tải...' }) => {
  return (
    <div className="loading-container">
      <div className="loading-content">
        <div className="loading-spinner"></div>
        <p className="loading-text">{text}</p>
      </div>
    </div>
  );
};

export default Loading;
