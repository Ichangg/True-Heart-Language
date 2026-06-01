// assets/js/ui.js
export const ui = {
  showToast: (message, type = 'info') => {
    // Simple toast implementation
    const toast = document.createElement('div');
    toast.textContent = message;
    toast.style.position = 'fixed';
    toast.style.bottom = '20px';
    toast.style.right = '20px';
    toast.style.padding = '10px 20px';
    toast.style.borderRadius = '5px';
    toast.style.color = '#fff';
    toast.style.zIndex = '9999';
    toast.style.transition = 'opacity 0.3s';
    
    if (type === 'success') toast.style.backgroundColor = '#4caf50';
    else if (type === 'error') toast.style.backgroundColor = '#f44336';
    else toast.style.backgroundColor = '#2196f3';
    
    document.body.appendChild(toast);
    
    setTimeout(() => {
      toast.style.opacity = '0';
      setTimeout(() => toast.remove(), 300);
    }, 3000);
  },
  
  showLoading: (elementId) => {
    const el = document.getElementById(elementId);
    if(el) el.innerHTML = '<tr><td colspan="10" style="text-align:center">Loading...</td></tr>';
  }
};
