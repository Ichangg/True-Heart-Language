import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar/Sidebar';
import useUiStore from '../store/uiStore';
import './DashboardLayout.css';

const studentMenu = [
  { path: '/student', icon: '📊', label: 'Tổng quan' },
  { path: '/student/classes', icon: '📚', label: 'Lớp của tôi' },
];

const StudentLayout = () => {
  const { sidebarOpen } = useUiStore();

  return (
    <div className="dashboard-layout">
      <Sidebar menuItems={studentMenu} />
      <main className={`dashboard-main ${sidebarOpen ? '' : 'expanded'}`}>
        <Outlet />
      </main>
    </div>
  );
};

export default StudentLayout;
