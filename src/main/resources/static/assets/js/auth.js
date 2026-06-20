import { api, setToken } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
  // Role Tabs Logic (for register)
  const roleTabs = document.querySelectorAll('.role-tab');
  let selectedRole = 'STUDENT'; // default

  roleTabs.forEach(tab => {
    tab.addEventListener('click', () => {
      roleTabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');
      selectedRole = tab.getAttribute('data-role').toUpperCase();
    });
  });

  // Login Form Logic
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const email = document.getElementById('loginEmail').value;
      const password = document.getElementById('loginPassword').value;
      const btn = document.getElementById('loginBtn');
      
      try {
        btn.disabled = true;
        btn.innerText = 'Đang xử lý...';
        
        const response = await api.login(email, password);
        
        // Save token
        setToken(response.token);
        
        Swal.fire({
          icon: 'success',
          title: 'Đăng nhập thành công',
          text: `Chào mừng ${response.fullname || ''}!`,
          timer: 1500,
          showConfirmButton: false
        }).then(() => {
          // Redirect based on role
          const role = response.role.toUpperCase();
          if (role === 'ADMIN') window.location.href = 'admin.html';
          else if (role === 'TEACHER') window.location.href = 'teacher.html';
          else if (role === 'STUDENT') window.location.href = 'student.html';
          else if (role === 'PARENT') window.location.href = 'parent.html';
          else window.location.href = 'index.html';
        });

      } catch (error) {
        console.error(error);
        Swal.fire({
          icon: 'error',
          title: 'Đăng nhập thất bại',
          text: 'Email hoặc mật khẩu không chính xác.'
        });
      } finally {
        btn.disabled = false;
        btn.innerText = 'Đăng nhập';
      }
    });
  }

  // Register Form Logic
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const fullname = document.getElementById('regName').value;
      const email = document.getElementById('regEmail').value;
      const password = document.getElementById('regPassword').value;
      const btn = document.getElementById('registerBtn');

      try {
        btn.disabled = true;
        btn.innerText = 'Đang xử lý...';

        await api.register({
          fullname,
          email,
          password,
          role: selectedRole
        });

        Swal.fire({
          icon: 'success',
          title: 'Đăng ký thành công',
          text: 'Vui lòng đăng nhập với tài khoản vừa tạo.',
        }).then(() => {
          window.location.href = 'login.html';
        });

      } catch (error) {
        console.error(error);
        Swal.fire({
          icon: 'error',
          title: 'Đăng ký thất bại',
          text: error.message || 'Có lỗi xảy ra khi tạo tài khoản.'
        });
      } finally {
        btn.disabled = false;
        btn.innerText = 'Tạo tài khoản';
      }
    });
  }
});
