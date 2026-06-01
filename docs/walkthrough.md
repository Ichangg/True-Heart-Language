# Walkthrough: Hoàn thiện API Backend - Giai đoạn 1

Trong lần triển khai này, tôi đã ưu tiên hoàn thiện các API Backend cốt lõi thuộc **Giai đoạn 1** theo kế hoạch, bao gồm quản lý Lớp học, Điểm danh, và Gen học phí.

> [!NOTE]
> Hệ thống Spring Boot hiện tại đã được cấu trúc đầy đủ, các class Entity và Repository mới đã được bổ sung nhằm hỗ trợ xử lý nghiệp vụ phức tạp.

## 1. Tính năng Quản lý Lớp học (Tạo lớp & Phân bổ GV/HS)
Trước đây API chỉ lưu được một bảng `classes`, bây giờ API đã hỗ trợ phân bổ (assign) giáo viên và học sinh ngay lúc khởi tạo thông qua `ClassCreationDTO`.

- **Entity & DTO Mới:** 
  - Khởi tạo `ClassTeachers` (Entity) và `ClassTeachersRepository` để ánh xạ bảng cơ sở dữ liệu.
  - Tạo `ClassCreationDTO` để Backend có thể nhận 1 JSON chứa thông tin Lớp, mảng `teacherIds` và `studentIds`.
- **API `POST /api/classes`**:
  - Giao dịch (`@Transactional`) an toàn: Lưu thông tin lớp -> Lặp qua mảng `teacherIds` để gán giáo viên (giáo viên đầu tiên `isPrimary = true`) -> Lặp qua mảng `studentIds` để gán học sinh vào `class_students` (mặc định giảm giá 0%).

## 2. Tính năng Điểm danh hàng loạt (Batch Attendance)
Giáo viên sẽ điểm danh cả lớp trong một lần thao tác thay vì gọi API cho từng học sinh.

- **API `POST /api/attendances/batch`**:
  - Nhận vào một List/Array các đối tượng `Attendances` (thường là 15-20 học sinh trong 1 lớp).
  - Sử dụng hàm `saveAll()` của JPA Repository để lưu toàn bộ dữ liệu xuống database trong 1 lần kết nối duy nhất (bulk insert), giúp tối ưu hiệu năng.

## 3. Tính năng Tạo Hóa Đơn Học Phí Tự Động (Fee Generation)
Vào mỗi đầu tháng, hệ thống cần tính toán số tiền phụ huynh phải đóng dựa trên học phí của khóa học và phần trăm giảm giá của từng học sinh.

- **API `POST /api/fee-records/generate/{classId}?month=YYYY-MM-DD`**:
  - Truy xuất thông tin học phí mỗi buổi (`feePerSession`) từ bảng `classes`.
  - Tìm tất cả học sinh đang học trong lớp đó.
  - Vòng lặp tính toán: `feeBase` (tạm tính 8 buổi/tháng) -> Trừ đi `discountAmount` (Dựa trên `discountPercent` của học sinh) -> Ra `feeFinal`.
  - Tạo bản ghi mới trong `fee_records` với trạng thái `unpaid` (Chưa đóng).

> [!TIP]
## 4. Tính năng Xử lý Thanh Toán (Payments)
Khi phụ huynh đến trung tâm nộp tiền, thu ngân sẽ sử dụng API này để ghi nhận.

- **API `POST /api/payments`**:
  - Lưu thông tin hóa đơn thanh toán (Số tiền, Phương thức).
  - Tự động tìm lại `FeeRecord` (Hồ sơ học phí) tương ứng.
  - Cộng dồn `paid_amount` và tự động cập nhật trạng thái `status` thành `paid` (nếu trả đủ) hoặc `partial` (nếu mới trả một phần). Hệ thống SQL Server sẽ tự tính lại `debt_amount`.

## 5. Tính năng Lương Giáo viên (Teacher Salaries)
Kế toán sẽ generate bảng lương vào cuối tháng cho toàn bộ giáo viên.

- **API `POST /api/teacher-salaries/generate?month=YYYY-MM-DD`**:
  - Quét toàn bộ giáo viên đang được gán vào các lớp thông qua bảng `class_teachers`.
  - Dựa trên `salary_per_session` và giả lập số buổi dạy (`sessions_taught`), hệ thống sẽ tính `total_salary` cho từng giáo viên.
  - Bảng lương được khởi tạo với trạng thái mặc định là `pending` (Chờ thanh toán).

## 6. Khởi tạo Giao diện Frontend (Admin Dashboard)
Chuyển sang **Giai đoạn 2**, hệ thống đã có cấu trúc JavaScript rành mạch:

- **`api.js`**: Trở thành cầu nối (Client SDK) với đầy đủ các hàm wrapper gọi Backend (VD: `api.getClasses()`, `api.generateFees()`).
- **`ui.js`**: Cung cấp các tiện ích hiện thông báo (Toast) và vòng xoay tải dữ liệu (Loading Spinner).
- **`admin.js`**: Được nhúng vào màn hình `admin.html` (thông qua `<script type="module">`). Hiện tại, Admin Dashboard đã có khả năng:
  - Tải tự động Danh sách Lớp, Danh sách Giáo viên.
  - Gọi API `getDashboardStats()` để lấy dữ liệu thực tế đẩy lên biểu đồ/số liệu tổng quan.
  - Có các nút bấm gọi trực tiếp vào API sinh học phí.

> [!TIP]
> Việc phân tách rõ ràng `api.js` và `admin.js` giúp code sạch sẽ, dễ dàng tái sử dụng hàm gọi API cho các trang `teacher.html` và `student.html` sau này.

## Bước tiếp theo
- Hoàn thiện toàn bộ giao diện Frontend (`teacher.html`, `student.html`) để kết nối với các API đã viết.
- Viết các test case (nếu cần thiết) để đảm bảo độ tin cậy của hệ thống tài chính.
