import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar/Sidebar';
import useUiStore from '../store/uiStore';
import './DashboardLayout.css';

const teacherMenu = [
  { path: '/teacher', icon: '📊', label: 'Tổng quan' },
  { path: '/teacher/classes', icon: '📚', label: 'Lớp của tôi' },
  { path: '/teacher/attendance', icon: '📋', label: 'Điểm danh' },
];

const TeacherLayout = () => {
  const { sidebarOpen } = useUiStore();

  return (
    <div className="dashboard-layout">
      <Sidebar menuItems={teacherMenu} />
      <main className={`dashboard-main ${sidebarOpen ? '' : 'expanded'}`}>
        <Outlet />
      </main>
    </div>
  );
};

export default TeacherLayout;
