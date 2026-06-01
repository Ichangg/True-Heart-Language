# Thiết kế UI/UX Chi tiết Tất cả Màn hình - Hệ thống Quản lý Trung tâm Tiếng Anh

Tài liệu này mô tả chi tiết mục đích, thành phần giao diện, nút chức năng, luồng xử lý và API tương tác cho **toàn bộ các màn hình** của 4 vai trò: Super Admin, Giáo viên, Phụ huynh, và Học sinh.

---

## PHẦN 1: SUPER ADMIN (BAN QUẢN TRỊ)

### 1.1 Màn hình Đăng nhập
- **Mục đích:** Cổng xác thực duy nhất cho toàn bộ người dùng (Role-based).
- **Thành phần:** Logo trung tâm, Ảnh minh họa (bên trái), Form đăng nhập (Username, Password, Quên mật khẩu).
- **Luồng xử lý:** Gọi API Login -> Nhận JWT -> Check Role (Admin/Giáo viên/Phụ huynh/HS) -> Redirect đến Dashboard tương ứng.
- **API:** `POST /api/v1/auth/login`

### 1.2 Màn hình Dashboard (Tổng quan)
- Đã phân tích chi tiết ở tài liệu trước (Gồm các KPI Doanh thu, Học sinh, Biểu đồ).

### 1.3 Màn hình Quản lý Tài khoản (Users)
- **Mục đích:** Quản lý thông tin, cấp quyền truy cập, và xem liên kết phụ huynh - con cái.
- **Thành phần giao diện:**
  - Tab Navigation: `[Giáo viên]` | `[Học sinh]` | `[Phụ huynh]`.
  - Bảng dữ liệu: Avatar, Họ tên, SĐT, Email, Trạng thái (Hoạt động/Khóa). Riêng tab Phụ huynh có thêm cột "Học sinh liên kết".
- **Nút chức năng:** `[+ Thêm mới]`, `[Chỉnh sửa]`, `[Khóa tài khoản]`, `[Reset Mật khẩu]`.
- **Luồng xử lý:** Chọn tab -> Xem danh sách -> Bấm Thêm mới -> Điền form (Đối với phụ huynh, có tính năng "Chọn học sinh liên kết" dạng Dropdown search) -> Lưu.
- **API:** `GET /api/v1/users?role={role}`, `POST /api/v1/users`, `POST /api/v1/users/{id}/link-student`

### 1.4 Phân hệ Lớp học - Tạo Lớp & Phân bổ (Mới)
- **Mục đích:** Admin tạo lớp học mới và phân bổ giáo viên, học sinh.
- **Thành phần giao diện (Dạng Form Stepper 4 Bước):**
  - *Bước 1 (Thông tin chung):* Khóa học, Khối, Tên lớp, Năm học.
  - *Bước 2 (Giáo viên):* Search & Select giáo viên phụ trách.
  - *Bước 3 (Học sinh):* Bảng Transfer List (chuyển học sinh từ DS chờ sang Lớp).
  - *Bước 4 (Học phí):* Điền % giảm giá (nếu có) cho các HS trong lớp.
- **Nút chức năng:** `[Tiếp tục]`, `[Quay lại]`, `[Hoàn tất Tạo lớp]`.
- **Luồng xử lý:** Điền thông tin -> Lưu tạm (Draft) -> Thêm GV/HS -> Đổi thành (Opening).
- **API:** `POST /api/v1/classes`, `POST /api/v1/classes/{id}/assign`

### 1.5 Màn hình Quản lý Công nợ Toàn Trung tâm
- **Mục đích:** Theo dõi tình trạng đóng học phí, ra quyết định truy thu.
- **Thành phần giao diện:**
  - Bộ lọc: Tháng/Năm, Lớp học, Trạng thái thanh toán (Hoàn thành / Còn nợ / Quá hạn).
  - Bảng: Tên HS, Lớp, Học phí chuẩn, % Giảm, Số tiền dự kiến, Đã thu, **Công nợ (Chữ in Đỏ đậm)**.
- **Nút chức năng:** `[Nhập thanh toán]`, `[Gửi tin nhắn Nhắc nợ tự động (Zalo/SMS)]`, `[Xuất Excel]`.
- **Luồng xử lý:** Lọc danh sách nợ -> Chọn các HS -> Bấm gửi nhắc nợ hàng loạt (Batch action).
- **API:** `GET /api/v1/finances/debts`, `POST /api/v1/finances/remind-debts`

### 1.6 Màn hình Nhập Thanh toán (Dành cho Thu ngân)
- **Mục đích:** Ghi nhận tiền khi phụ huynh đóng.
- **Thành phần giao diện:**
  - Thanh tìm kiếm lớn ở giữa (Search theo Tên/SĐT phụ huynh).
  - Khung thông tin HS và số tiền cần đóng.
  - Form thu tiền: Hình thức (Tiền mặt, CK), Số tiền nộp, Ghi chú, Ngày.
- **Nút chức năng:** `[Xác nhận thanh toán & In biên lai]`.
- **API:** `POST /api/v1/finances/payments`

### 1.7 Màn hình Quản lý Thông báo
- **Mục đích:** Gửi thông báo đa kênh.
- **Thành phần giao diện:**
  - Tabs: `[Tạo thông báo]` | `[Lịch sử gửi]` | `[Nhật ký lỗi]`.
  - Form Tạo: Chọn Kênh (Web/Zalo/FB/SMS), Chọn Đối tượng (Toàn bộ / Theo Lớp / Theo Nhóm Phụ huynh), Tiêu đề, Nội dung Text Editor (hỗ trợ chèn biến như `{Ten_HS}`, `{Cong_No}`).
- **Nút chức năng:** `[Gửi ngay]`, `[Lên lịch gửi]`.
- **API:** `POST /api/v1/notifications/send`, `GET /api/v1/notifications/history`

### 1.8 Màn hình Cài đặt Tích hợp (Zalo/FB)
- **Mục đích:** Cấu hình Webhook và Access Token hệ thống.
- **Thành phần:** Form nhập App ID, Secret Key, Access Token cho từng kênh.
- **Nút chức năng:** `[Test Connection (Ping)]`, `[Lưu cấu hình]`.

---

## PHẦN 2: GIÁO VIÊN (TEACHER)

### 2.1 Màn hình Thời khóa biểu & Lớp dạy
- **Mục đích:** Xem lịch dạy trong tuần.
- **Thành phần:** Calendar Layout (hoặc Time Grid). Mỗi slot màu xanh hiển thị Giờ, Tên lớp, Phòng học.
- **Nút chức năng:** Bấm vào slot sẽ pop-up menu: `[Điểm danh ngay]`, `[Danh sách HS]`.
- **API:** `GET /api/v1/teachers/schedule`

### 2.2 Màn hình Điểm danh nhanh
- Đã phân tích chi tiết (Chạm để chọn Có mặt/Phép/Không phép).

### 2.3 Màn hình Danh sách Học sinh của lớp
- **Mục đích:** Xem thông tin HS của lớp mình phụ trách để quản lý.
- **Thành phần:** Dạng Card lưới. Mỗi thẻ chứa: Avatar HS, Tên, Ngày sinh, **Tên & SĐT Phụ huynh** (Nổi bật để GV tiện gọi điện).
- **Nút chức năng:** `[Chat/Gửi tin nhắn cho Phụ huynh]`.
- **API:** `GET /api/v1/teachers/classes/{id}/students`

### 2.4 Màn hình Lịch sử Giảng dạy (Bảng chấm công)
- **Mục đích:** Theo dõi số buổi đã dạy để đối chiếu lương.
- **Thành phần:** 
  - Thẻ tổng hợp: Tổng số buổi dạy trong tháng.
  - Bảng chi tiết: Ngày dạy, Tên lớp, Trạng thái (Đã nộp điểm danh/Chưa nộp).
- **API:** `GET /api/v1/teachers/teaching-history`

### 2.5 Màn hình Gửi Thông báo cho Lớp
- **Mục đích:** Nhắc nhở bài tập, chuẩn bị dụng cụ.
- **Thành phần:** Dropdown chọn Lớp (Chỉ hiện lớp mình dạy), Input Text Area.
- **Nút:** `[Gửi tới App/Web Phụ huynh lớp]`.
- **API:** `POST /api/v1/teachers/classes/{id}/notifications`

---

## PHẦN 3: PHỤ HUYNH (PARENT)

### 3.1 Màn hình Tổng quan (Chọn Con)
- **Mục đích:** Dành cho phụ huynh có 2 con trở lên học tại trung tâm.
- **Thành phần:** Màn hình trung tâm hiển thị các Card lớn với Avatar và Tên các con. 
- **Luồng xử lý:** Click vào "Bé A" -> Đi vào Dashboard của Bé A. (Có nút chuyển đổi con ở Menu trên cùng).
- **API:** `GET /api/v1/parents/children`

### 3.2 Màn hình Lịch học & Chuyên cần của Con
- **Mục đích:** Theo dõi lịch đi học.
- **Thành phần:**
  - Calendar các buổi học trong tháng.
  - Đánh dấu màu: Xanh lá (Có mặt), Vàng (Nghỉ phép), Đỏ (Trốn học/Không phép).
  - Khung thống kê: Số buổi học, Số buổi vắng.
- **API:** `GET /api/v1/parents/students/{id}/attendance`

### 3.3 Màn hình Quản lý Học phí & Thanh toán
- Đã phân tích chi tiết (Hiển thị học phí, công nợ, lịch sử).

### 3.4 Hộp thư Thông báo
- **Mục đích:** Nhận tin nhắn từ Admin / Giáo viên.
- **Thành phần:** Danh sách tin nhắn dạng List-view. Dấu chấm đỏ báo tin chưa đọc. Bấm vào đọc chi tiết.
- **API:** `GET /api/v1/notifications/my-messages`

---

## PHẦN 4: HỌC SINH (STUDENT)

### 4.1 Màn hình Lớp học của Tôi
- **Mục đích:** Học sinh tự theo dõi thông tin học tập của mình.
- **Thành phần:** Tên lớp đang học, Khóa học, Tiến độ khai giảng/bế giảng. Khung thông tin giáo viên phụ trách (Avatar, Lời chào).
- **API:** `GET /api/v1/students/my-class`

### 4.2 Màn hình Lịch học & Điểm danh
- **Mục đích:** Tự xem lịch học.
- **Thành phần:** Tương tự màn hình chuyên cần của Phụ huynh nhưng dạng View-only (Chỉ xem).

---

## TỔNG KẾT VỀ YÊU CẦU UX / RESPONSIVE

1. **Giao diện Desktop (Dành cho Admin & Thu ngân):**
   - **Bố cục:** Sidebar Menu cố định bên trái, Header trên cùng, nội dung chính bên phải.
   - **Tối ưu:** Tối đa hóa chiều ngang cho Data Table. Sử dụng Sticky Header và cố định cột Action cuối cùng để cuộn ngang không bị mất dấu.
2. **Giao diện Tablet (Dành cho Giáo viên):**
   - **Bố cục:** Ưu tiên Layout dạng Grid/Thẻ để chạm.
   - **Tối ưu:** Rất quan trọng khi Giáo viên cầm iPad/Tablet đi quanh lớp để chấm điểm danh nhanh bằng ngón tay.
3. **Giao diện Mobile (Dành cho Phụ huynh & Học sinh):**
   - **Bố cục:** Sử dụng Bottom Navigation Bar (Home, Lịch học, Học phí, Hộp thư).
   - **Tối ưu:** Thẻ Card hiển thị dọc, font chữ lớn, nút bấm to. Hỗ trợ thao tác vuốt (Swipe) mượt mà giống như các app hiện đại.
