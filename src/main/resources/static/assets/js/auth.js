import { api, setToken } from './api.js';
import { ui } from './ui.js';

document.addEventListener('DOMContentLoaded', () => {
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault(); // Ngăn form tự load lại trang
      
      const email = document.getElementById('username').value; // Trường HTML đặt ID là username
      const password = document.getElementById('password').value;
      
      const btn = loginForm.querySelector('button[type="submit"]');
      const originalText = btn.innerText;
      btn.innerText = 'Đang xử lý...';
      btn.disabled = true;

      try {
        const response = await api.login(email, password);
        
        // Nếu API trả về thành công
        if (response && response.token) {
          setToken(response.token);
          ui.showToast('Đăng nhập thành công!', 'success');
          
          // Decode token để biết role và chuyển hướng
          try {
             // Lấy phần payload của JWT
             const base64Url = response.token.split('.')[1];
             const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
             const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                 return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
             }).join(''));
             const payload = JSON.parse(jsonPayload);
             
             // Chuyển hướng dựa trên role
             if (payload.role === 'admin') {
               window.location.href = 'admin.html';
             } else if (payload.role === 'teacher') {
               window.location.href = 'teacher.html';
             } else {
               window.location.href = 'student.html';
             }
          } catch(err) {
             window.location.href = 'admin.html'; // Default fallback
          }
        }
      } catch (error) {
        ui.showToast(error.message || 'Sai tên đăng nhập hoặc mật khẩu!', 'error');
      } finally {
        btn.innerText = originalText;
        btn.disabled = false;
      }
    });
  }
});
