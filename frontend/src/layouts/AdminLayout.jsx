import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar/Sidebar';
import Header from '../components/Header/Header';
import useUiStore from '../store/uiStore';
import './DashboardLayout.css';

const adminMenu = [
  { path: '/admin', label: 'Tổng quan' },
  { path: '/admin/classes', label: 'Quản lý lớp học' },
  { path: '/admin/users', label: 'Quản lý người dùng' },
  { path: '/admin/finance', label: 'Báo cáo tài chính' },
  { path: '/admin/teacher-payment', label: 'Lương giáo viên' },
  { path: '/admin/student-stats', label: 'Thống kê học sinh' },
  { path: '/admin/promotions', label: 'Quảng cáo trang chủ' },
  { path: '/admin/messages', label: 'Tin nhắn phụ huynh' },
];

const AdminLayout = () => {
  const { sidebarOpen } = useUiStore();

  return (
    <div className="dashboard-layout">
      <Sidebar menuItems={adminMenu} />
      <main className={`dashboard-main ${sidebarOpen ? '' : 'expanded'}`}>
        <Outlet />
      </main>
    </div>
  );
};

export default AdminLayout;
