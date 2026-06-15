import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import useAuthStore from './store/authStore';

// Layouts
import PublicLayout from './layouts/PublicLayout';
import AdminLayout from './layouts/AdminLayout';
import TeacherLayout from './layouts/TeacherLayout';
import StudentLayout from './layouts/StudentLayout';
import ParentLayout from './layouts/ParentLayout';

// Components
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute';

// Public Pages
import HomePage from './pages/public/HomePage';
import LoginPage from './pages/public/LoginPage';

// Admin Pages
import AdminDashboard from './pages/admin/Dashboard';
import ClassManagement from './pages/admin/ClassManagement';
import UserManagement from './pages/admin/UserManagement';
import FinanceReport from './pages/admin/FinanceReport';
import TeacherPaymentPage from './pages/admin/TeacherPayment';
import StudentStats from './pages/admin/StudentStats';
import PromotionManagement from './pages/admin/PromotionManagement';
import MessageCenter from './pages/admin/MessageCenter';

// Teacher Pages
import TeacherDashboard from './pages/teacher/Dashboard';
import Attendance from './pages/teacher/Attendance';

// Student Pages
import StudentDashboard from './pages/student/Dashboard';

// Parent Pages
import ParentDashboard from './pages/parent/Dashboard';

// Styles
import './assets/styles/global.css';
import './assets/styles/components.css';

function App() {
  const { isAuthenticated, user } = useAuthStore();

  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route element={<PublicLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={
            isAuthenticated ? (
              <Navigate to={`/${user?.role === 'admin' ? 'admin' : user?.role}`} replace />
            ) : (
              <LoginPage />
            )
          } />
        </Route>

        {/* Admin Routes */}
        <Route path="/admin" element={
          <ProtectedRoute allowedRoles={['admin']}>
            <AdminLayout />
          </ProtectedRoute>
        }>
          <Route index element={<AdminDashboard />} />
          <Route path="classes" element={<ClassManagement />} />
          <Route path="users" element={<UserManagement />} />
          <Route path="finance" element={<FinanceReport />} />
          <Route path="teacher-payment" element={<TeacherPaymentPage />} />
          <Route path="student-stats" element={<StudentStats />} />
          <Route path="promotions" element={<PromotionManagement />} />
          <Route path="messages" element={<MessageCenter />} />
        </Route>

        {/* Teacher Routes */}
        <Route path="/teacher" element={
          <ProtectedRoute allowedRoles={['teacher']}>
            <TeacherLayout />
          </ProtectedRoute>
        }>
          <Route index element={<TeacherDashboard />} />
          <Route path="classes" element={<TeacherDashboard />} />
          <Route path="classes/:classId" element={<Attendance />} />
          <Route path="attendance" element={<Attendance />} />
        </Route>

        {/* Student Routes */}
        <Route path="/student" element={
          <ProtectedRoute allowedRoles={['student']}>
            <StudentLayout />
          </ProtectedRoute>
        }>
          <Route index element={<StudentDashboard />} />
          <Route path="classes" element={<StudentDashboard />} />
        </Route>

        {/* Parent Routes */}
        <Route path="/parent" element={
          <ProtectedRoute allowedRoles={['parent']}>
            <ParentLayout />
          </ProtectedRoute>
        }>
          <Route index element={<ParentDashboard />} />
          <Route path="children" element={<ParentDashboard />} />
          <Route path="payments" element={<ParentDashboard />} />
        </Route>

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
