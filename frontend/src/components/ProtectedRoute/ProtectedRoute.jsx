import { Navigate } from 'react-router-dom';
import useAuthStore from '../../store/authStore';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { isAuthenticated, user } = useAuthStore();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user?.role)) {
    // Redirect to their own dashboard
    const dashboardPaths = {
      admin: '/admin',
      teacher: '/teacher',
      student: '/student',
      parent: '/parent',
    };
    return <Navigate to={dashboardPaths[user?.role] || '/login'} replace />;
  }

  return children;
};

export default ProtectedRoute;
