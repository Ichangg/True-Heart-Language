import { create } from 'zustand';

const useUiStore = create((set) => ({
  sidebarOpen: true,
  modalOpen: false,
  modalContent: null,
  toasts: [],

  toggleSidebar: () => set((s) => ({ sidebarOpen: !s.sidebarOpen })),
  setSidebarOpen: (open) => set({ sidebarOpen: open }),

  openModal: (content) => set({ modalOpen: true, modalContent: content }),
  closeModal: () => set({ modalOpen: false, modalContent: null }),

  addToast: (toast) => {
    const id = Date.now();
    set((s) => ({
      toasts: [...s.toasts, { ...toast, id }],
    }));
    setTimeout(() => {
      set((s) => ({
        toasts: s.toasts.filter((t) => t.id !== id),
      }));
    }, toast.duration || 4000);
  },

  removeToast: (id) => set((s) => ({
    toasts: s.toasts.filter((t) => t.id !== id),
  })),
}));

export default useUiStore;
