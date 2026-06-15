import { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import useAuthStore from "../../store/authStore";
import useUiStore from "../../store/uiStore";
import { ROLE_LABELS } from "../../utils/constants";
import "./Sidebar.css";

const Sidebar = ({ menuItems }) => {
  const { user, logout } = useAuthStore();
  const { sidebarOpen, toggleSidebar } = useUiStore();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <aside className={`sidebar ${sidebarOpen ? "open" : "collapsed"}`}>
      {/* Logo */}
      <div className="sidebar-logo">
        <div className="logo-icon">🎓</div>
        {sidebarOpen && (
          <div className="logo-text">
            <span className="logo-title">English Center</span>
            <span className="logo-subtitle">Management System</span>
          </div>
        )}
        <button className="sidebar-toggle" onClick={toggleSidebar}>
          {sidebarOpen ? "◀" : "▶"}
        </button>
      </div>

      {/* User Info */}
      <div className="sidebar-user">
        <div className="user-avatar">
          {user?.full_name?.charAt(0)?.toUpperCase() || "U"}
        </div>
        {sidebarOpen && (
          <div className="user-info">
            <span className="user-name">{user?.full_name}</span>
            <span className="user-role">{ROLE_LABELS[user?.role]}</span>
          </div>
        )}
      </div>

      {/* Navigation */}
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}
            title={item.label}
          >
            <span className="nav-icon">{item.icon}</span>
            {sidebarOpen && <span className="nav-label">{item.label}</span>}
          </NavLink>
        ))}
      </nav>

      {/* Logout */}
      <div className="sidebar-footer">
        <button className="nav-item logout-btn" onClick={handleLogout}>
          <span className="nav-icon">🚪</span>
          {sidebarOpen && <span className="nav-label">Đăng</span>}
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
