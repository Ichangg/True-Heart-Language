import useUiStore from '../../store/uiStore';
import './Header.css';

const Header = ({ title, children }) => {
  const { sidebarOpen } = useUiStore();

  return (
    <header className={`main-header ${sidebarOpen ? '' : 'expanded'}`}>
      <div className="header-left">
        <h1 className="header-title">{title}</h1>
      </div>
      <div className="header-right">
        {children}
      </div>
    </header>
  );
};

export default Header;
