import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar/Sidebar';
import useUiStore from '../store/uiStore';
import './DashboardLayout.css';

const parentMenu = [
  { path: '/parent', icon: '📊', label: 'Tổng quan' },
  { path: '/parent/children', icon: '👶', label: 'Thông tin con em' },
  { path: '/parent/payments', icon: '💰', label: 'Lịch sử học phí' },
];

const ParentLayout = () => {
  const { sidebarOpen } = useUiStore();

  return (
    <div className="dashboard-layout">
      <Sidebar menuItems={parentMenu} />
      <main className={`dashboard-main ${sidebarOpen ? '' : 'expanded'}`}>
        <Outlet />
      </main>
    </div>
  );
};

export default ParentLayout;
