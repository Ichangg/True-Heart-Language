# Tài liệu Đặc tả Yêu cầu Hệ thống (SRS) & Thiết kế UI/UX - Quản lý Trung tâm Tiếng Anh

Tài liệu này được biên soạn dưới góc độ Senior Business Analyst, Senior UI/UX Designer, và Solution Architect để mô tả chi tiết các yêu cầu, thiết kế và kiến trúc cho hệ thống Website Quản lý Trung tâm Tiếng Anh.

---

## 1. Phân tích nghiệp vụ (Business Analysis)

Hệ thống được thiết kế để số hóa và tự động hóa toàn bộ quy trình vận hành của một Trung tâm Tiếng Anh hiện đại. Các quy trình cốt lõi bao gồm:

- **Quản lý Học thuật & Đào tạo:** Quản lý vòng đời của lớp học (Mở, Hoạt động, Đóng), phân bổ học sinh và giáo viên. Theo dõi chuyên cần chặt chẽ và tự động tính toán số buổi học/nghỉ.
- **Quản lý Tài chính:** Số hóa nghiệp vụ thu học phí phức tạp (Học phí chuẩn + % Giảm giá = Thực thu). Tự động theo dõi công nợ, tính toán lương cho giáo viên dựa trên dữ liệu điểm danh.
- **Tương tác & Truyền thông:** Thiết lập kênh giao tiếp đa chiều giữa Trung tâm - Phụ huynh - Học sinh thông qua Website, Zalo OA, Facebook Messenger, SMS.
- **Quản lý Quảng cáo:** Tích hợp công cụ Marketing nội bộ qua hệ thống Slider và Popup.
- **Báo cáo & Thống kê (BI):** Cung cấp Dashboard toàn diện cho Ban quản trị để ra quyết định dựa trên dữ liệu (Data-driven).

---

## 2. Danh sách chức năng (Feature List)

| Phân hệ | Tính năng | Mô tả chi tiết |
|---|---|---|
| **Tài khoản (Auth)** | Đăng nhập / Đăng xuất | Hệ thống xác thực bằng JWT, phân quyền hiển thị theo Role. |
| | Cập nhật hồ sơ | Cập nhật thông tin cá nhân, đổi mật khẩu. |
| **Quản lý Users** | Quản lý Giáo viên, Học sinh, Phụ huynh | CRUD thông tin người dùng. Quản lý liên kết Phụ huynh - Học sinh (1 Phụ huynh nhiều con). |
| **Quản lý Đào tạo** | Quản lý Khóa học | Tạo mới và cấu hình học phí chuẩn cho Khóa học. |
| | Quản lý Lớp học | Tạo lớp theo năm học và khối. Quản lý trạng thái: Draft, Opening, Active, Closed. (Không xóa dữ liệu). |
| | Phân bổ Lớp học | Thêm giáo viên và học sinh vào lớp. |
| **Quản lý Học phí** | Cấu hình Học phí / Khuyến mãi | Thiết lập % giảm học phí cho từng học sinh. |
| | Quản lý Thanh toán | Nhập thanh toán thủ công, xem lịch sử đóng tiền. |
| | Theo dõi Công nợ | Tính toán Tự động: Học phí dự kiến, Số tiền đã thu, Tiền nợ. |
| **Điểm danh** | Điểm danh buổi học | Ghi nhận trạng thái: Có mặt, Vắng có phép, Vắng không phép. |
| | Tính lương & Chuyên cần | Tự động tính số buổi dạy cho giáo viên và số buổi nghỉ của học sinh. |
| **Thông báo** | Gửi thông báo đa kênh | Gửi tin nhắn qua Web, Zalo, FB Messenger. Hỗ trợ gửi toàn trung tâm, theo lớp, hoặc nhóm phụ huynh. |
| **Quảng cáo** | Quản lý Slider & Popup | Thiết lập hình ảnh, text, link, thời gian hiển thị. Preview trực tiếp. |
| **Thống kê** | Dashboard KPI | Hiển thị Biểu đồ doanh thu, số lượng học sinh, tỷ lệ chuyên cần. |

---

## 3. Ma trận phân quyền (RBAC - Role-Based Access Control)

| Chức năng | Super Admin | Giáo viên | Phụ huynh | Học sinh |
|---|:---:|:---:|:---:|:---:|
| **Quản trị Hệ thống** | ✔ | - | - | - |
| Mở / Đóng Lớp học | ✔ | - | - | - |
| Xem thời khóa biểu & DS Lớp | ✔ | ✔ (Lớp dạy) | ✔ (Lớp con học) | ✔ (Lớp học) |
| Xem Danh sách Học sinh / Giáo viên| ✔ | ✔ (Lớp dạy) | ✔ (Giáo viên của con)| ✔ (Giáo viên của mình)|
| Điểm danh | - | ✔ (Lớp dạy) | - | - |
| Xem lịch sử điểm danh / nghỉ | ✔ | ✔ (Lớp dạy) | ✔ (Của con) | ✔ (Của mình) |
| Cấu hình Học phí / Khuyến mãi | ✔ | - | - | - |
| Nhập thanh toán & Quản lý Công nợ | ✔ | - | - | - |
| Xem học phí & Lịch sử thanh toán | ✔ | - | ✔ (Của con) | - |
| Gửi thông báo | ✔ | ✔ (Cho lớp dạy)| - | - |
| Nhận thông báo | ✔ | ✔ | ✔ | ✔ |
| Quản lý Quảng cáo (Slider/Popup) | ✔ | - | - | - |
| Xem Dashboard Báo cáo | ✔ | - | - | - |

---

## 4. Database Schema (Tổng quan)

Sử dụng cơ sở dữ liệu quan hệ (PostgreSQL / MySQL) đáp ứng tính toàn vẹn tài chính.

**Các Bảng cốt lõi:**
1. `users` (id, role, username, password, fullname, phone, zalo_id, fb_id)
2. `parent_student` (parent_id, student_id)
3. `courses` (id, name, standard_fee)
4. `classes` (id, course_id, name, academic_year, status: DRAFT, OPENING, ACTIVE, CLOSED)
5. `class_teacher` (class_id, teacher_id)
6. `class_student` (class_id, student_id, discount_percent, final_fee)
7. `attendance_records` (id, class_id, student_id, session_date, status: PRESENT, EXCUSED, UNEXCUSED, marked_by)
8. `fee_payments` (id, student_id, expected_amount, paid_amount, debt_amount, payment_date)
9. `notifications` (id, sender_id, target_role, target_class, title, content, channel: ZALO/FB/WEB)
10. `advertisements` (id, type: SLIDER/POPUP, image_url, title, action_url, start_time, end_time)

---

## 5. Sitemap

- **Trang chủ (Dành cho Guest/Tất cả):** Slider, Popup, Giới thiệu (nếu có).
- **Super Admin:**
  - Tổng quan (Dashboard)
  - Quản lý Nhân sự (Giáo viên, Admin)
  - Quản lý Khách hàng (Học sinh, Phụ huynh)
  - Quản lý Đào tạo (Khóa học, Lớp học)
  - Quản lý Tài chính (Học phí, Thanh toán, Công nợ, Bảng lương)
  - Truyền thông (Gửi thông báo, Lịch sử gửi, Slider, Popup, Zalo/FB Config)
- **Giáo viên:**
  - Lịch giảng dạy (Lớp học được phân công)
  - Điểm danh (Theo buổi)
  - Lịch sử giảng dạy (Báo cáo số buổi dạy)
  - Thông báo lớp
- **Phụ huynh:**
  - Chọn con (Dropdown nếu nhiều con)
  - Lịch học & Giáo viên
  - Chi tiết chuyên cần (Số buổi nghỉ/có mặt)
  - Học phí & Công nợ (Lịch sử thanh toán)
- **Học sinh:**
  - Lớp học của tôi (Lịch học, Giáo viên)
  - Điểm danh
  - Thông báo

---

## 6. User Flow (Các luồng chính)

**Luồng 1: Admin tạo lớp & Phân bổ học sinh (Không xóa lớp cũ)**
1. Admin vào "Quản lý lớp học" -> Chọn Khóa học, Năm học (VD: 2026), Nhập tên (VD: 3.1). Trạng thái mặc định: Draft.
2. Admin gán Giáo viên vào lớp.
3. Admin thêm Học sinh vào lớp. Nhập % Giảm giá nếu có.
4. Admin đổi trạng thái thành "Opening" (Gom đủ HS) hoặc "Active" (Bắt đầu học).
5. Khi kết thúc năm học, đổi trạng thái thành "Closed" (Ẩn khỏi hoạt động, giữ nguyên lịch sử).

**Luồng 2: Giáo viên Điểm danh**
1. Giáo viên xem Thời khóa biểu hôm nay -> Nhấn "Điểm danh" lớp đang dạy.
2. Tích chọn trạng thái (Có mặt/Vắng phép/Vắng không phép).
3. Bấm "Lưu".
4. Hệ thống tự động: Cộng 1 buổi dạy cho giáo viên, cập nhật số buổi nghỉ của học sinh, gửi notify cho phụ huynh (nếu vắng).

**Luồng 3: Quản lý học phí (Admin & Phụ huynh)**
1. Admin xem Công nợ tổng -> Nhập "Thanh toán" cho học sinh A (VD: Nộp 1.200.000).
2. Hệ thống trừ công nợ, sinh hóa đơn.
3. Phụ huynh đăng nhập -> Xem "Học phí" -> Thấy công nợ bằng 0 và lịch sử thanh toán mới nhất.

---

## 7. Thiết kế UI/UX từng màn hình (Material Design 3 / Ant Design)

### 7.1 Màn hình Danh sách Lớp học (Admin)
- **Mục đích:** Quản lý vòng đời và thông tin lớp học.
- **Thành phần giao diện:** 
  - Bộ lọc: Năm học, Trạng thái (Draft/Opening/Active/Closed), Khối học.
  - Bảng (Data Table): Tên lớp, Giáo viên phụ trách, Sĩ số, Trạng thái (Badge màu: Xám/Cam/Xanh lá/Đỏ).
- **Nút chức năng:** `[+ Tạo lớp mới]`, `[Đổi trạng thái]`, `[Xem chi tiết HS/GV]`. *(Không có nút Xóa)*
- **Luồng xử lý:** Click tạo mới -> Form pop-up nhập thông tin -> Lưu.
- **API cần gọi:** `GET /api/v1/classes`, `PATCH /api/v1/classes/{id}/status`

### 7.2 Màn hình Học phí của Con (Phụ huynh)
- **Mục đích:** Tra cứu minh bạch học phí và công nợ.
- **Thành phần giao diện:** 
  - Dropdown chọn bé (Nếu >1 con).
  - 3 Thẻ Metric (Cards): `Học phí chuẩn` | `Mức giảm (%)` | `Thực thu`.
  - Thẻ `Công nợ hiện tại` (Chữ to, màu đỏ/xanh).
  - Bảng lịch sử: Ngày nộp, Số tiền, Người thu, Trạng thái.
- **Nút chức năng:** `[Thanh toán Online]` (Nếu có), `[Tải Phiếu thu]`.
- **Luồng xử lý:** Chọn bé -> Dữ liệu reload -> Xem công nợ -> Theo dõi lịch sử.
- **API cần gọi:** `GET /api/v1/parents/fees/{student_id}`, `GET /api/v1/parents/payments/{student_id}`

### 7.3 Màn hình Điểm danh nhanh (Giáo viên)
- **Mục đích:** Hỗ trợ giáo viên điểm danh nhanh chóng với số ít thao tác nhất.
- **Thành phần giao diện:** 
  - Header: Tên lớp, Sĩ số, Ngày.
  - Danh sách học sinh (Avatar + Tên). Bên cạnh là 3 nút Radio (Button Group) thiết kế lớn dễ chạm: `[Có mặt (Xanh)]`, `[Phép (Vàng)]`, `[Không phép (Đỏ)]`.
- **Nút chức năng:** `[Lưu & Thông báo Phụ huynh]`.
- **Luồng xử lý:** Tích trạng thái -> Bấm Lưu -> Đẩy Notification.
- **API cần gọi:** `GET /api/v1/teachers/classes/{id}/students`, `POST /api/v1/teachers/attendance`

### 7.4 Màn hình Quản lý Quảng Cáo (Admin)
- **Mục đích:** Thiết lập Popup và Slider truyền thông nội bộ.
- **Thành phần giao diện:** 
  - Layout chia 2 cột: Cột trái (Form nhập liệu), Cột phải (Live Preview khung điện thoại/web).
  - Form: Image Upload (Drag/Drop), Tiêu đề, Mô tả ngắn, Link nút bấm, Date-picker (Thời gian bắt đầu/kết thúc).
- **Nút chức năng:** `[Xem trước]`, `[Xuất bản]`, `[Tạm dừng]`.
- **Luồng xử lý:** Upload ảnh -> Nhập text -> Xem Live Preview -> Xuất bản.
- **API cần gọi:** `POST /api/v1/admin/campaigns`

---

## 8. Dashboard Admin

Dashboard được thiết kế theo dạng Grid trực quan, tập trung vào Data Analytics.

**Row 1: KPI Cards (Summary)**
- **Tổng học sinh:** 1,250 (Tăng 5% - Mũi tên xanh lên).
- **Tổng giáo viên:** 45.
- **Tổng doanh thu (Tháng này):** 1.2B VNĐ.
- **Tổng Công nợ:** 150M VNĐ (Màu đỏ cảnh báo).

**Row 2: Biểu đồ phân tích (Sử dụng Recharts/Chart.js)**
- **Bar Chart (Biểu đồ cột):** Doanh thu theo 12 tháng (Có filter: Quý/Năm).
- **Line Chart (Biểu đồ đường):** Biến động số lượng học sinh (Tăng/giảm theo từng tháng).
- **Doughnut Chart (Biểu đồ tròn khuyết):** Tỷ lệ chuyên cần trung bình (Có mặt 90% / Phép 8% / Không phép 2%).

---

## 9. API Gợi ý (RESTful Framework)

| Method | Endpoint | Mô tả chức năng |
|---|---|---|
| **POST** | `/api/v1/auth/login` | Đăng nhập lấy JWT |
| **GET** | `/api/v1/admin/dashboard/metrics` | Lấy dữ liệu thống kê cho Dashboard |
| **POST** | `/api/v1/admin/classes` | Tạo lớp học mới (Trạng thái Draft) |
| **PATCH**| `/api/v1/admin/classes/{id}/status` | Chuyển trạng thái lớp học (Mở/Đóng) |
| **GET** | `/api/v1/admin/fees/debts` | Lấy danh sách công nợ học sinh |
| **POST** | `/api/v1/teachers/attendance` | Nộp dữ liệu điểm danh |
| **POST** | `/api/v1/notifications/send` | Gửi thông báo (Payload: Zalo, Web, FB) |
| **POST** | `/api/v1/admin/ads/popup` | Cấu hình Popup quảng cáo |

---

## 10. Kiến trúc hệ thống (Solution Architecture)

**1. Frontend (SPA):**
- **Core:** React.js hoặc Next.js.
- **UI Library:** Ant Design (đáp ứng tốt các bảng dữ liệu phức tạp) hoặc Material-UI (MUI).
- **State Management:** Redux Toolkit (quản lý state global) / React Query (quản lý API caching).
- **Responsive:** Mobile-first (rất quan trọng cho Phụ huynh và Giáo viên điểm danh trên điện thoại).

**2. Backend (API Server):**
- **Core:** Java Spring Boot (như project hiện tại của bạn).
- **Security:** Spring Security + JWT Authentication + RBAC (Role-Based Access Control).
- **ORM:** Spring Data JPA / Hibernate.

**3. Database & Cache:**
- **RDBMS:** PostgreSQL (Ưu tiên) hoặc MySQL. Hỗ trợ ACID rất tốt cho dữ liệu học phí.
- **Cache:** Redis (Lưu trữ Slider, Popup, JWT Token bị revoke, đếm số thông báo chưa đọc).

**4. 3rd Party Integrations (Dịch vụ bên thứ ba):**
- **Zalo OA API / Facebook Graph API:** webhook và sending API để bắn thông báo.
- **Firebase (FCM):** Đẩy thông báo Push Notifications real-time trên Web.
- **Cron Jobs (Spring Scheduler):** Tự động tính công nợ vào ngày 1 hàng tháng, quét các quảng cáo hết hạn hiển thị.

---
*Tài liệu được phân tích và hệ thống hóa để đảm bảo khả năng mở rộng, tính bảo mật dữ liệu và mang lại trải nghiệm UX tối ưu nhất cho cả 4 nhóm người dùng của Trung tâm Tiếng Anh.*
