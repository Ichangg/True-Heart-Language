# Kế hoạch Triển khai Hệ thống Quản lý Trung tâm Tiếng Anh

Dựa trên cấu trúc cơ sở dữ liệu `EnglishCenterV2.sql` và mã nguồn hiện tại của dự án, tôi nhận thấy dự án đã được khởi tạo sẵn các Controller và Service cho Spring Boot, cùng với một số file giao diện HTML cơ bản (như `admin.html`). 

Để hoàn thiện hệ thống, chúng ta cần thực hiện công việc lập trình thực tế cho cả Backend và Frontend. Tôi đã chia công việc thành 2 giai đoạn chính dưới đây.

## Open Questions

> [!IMPORTANT] **Bạn muốn ưu tiên triển khai phần nào trước?**
> 1. **Option 1:** Tập trung hoàn thiện toàn bộ **API Backend (Spring Boot)**, xử lý logic nghiệp vụ (tạo lớp, tính học phí, điểm danh), test kỹ bằng Postman/HTTP Client trước.
> 2. **Option 2:** Tập trung xây dựng **UI Frontend (JS, HTML)**, kết nối các màn hình (Admin Dashboard, Quản lý lớp, Điểm danh) với các API giả (hoặc các API cơ bản đã có).
> 3. **Option 3:** Làm song song theo **từng tính năng / Use Case** (Ví dụ: Hoàn thiện tính năng "Quản lý lớp học" từ Backend ra Frontend trước, sau đó tới tính năng "Học phí").

Vui lòng cho tôi biết bạn muốn bắt đầu với phương án nào nhé!

## Proposed Changes

### Giai đoạn 1: Hoàn thiện API Backend (Spring Boot)

Chúng ta sẽ hoàn thành logic nghiệp vụ cho các Service và Controller đã được định hình.

#### [MODIFY] API Quản lý Lớp học & Đào tạo
* **`ClassesServiceImpl.java`**: Viết logic `createClass()`, thêm bảng `class_teachers` và thêm học sinh vào `class_students`.
* **`AttendancesServiceImpl.java`**: Xử lý logic điểm danh hàng loạt, ghi nhận `status` và tự động cập nhật số buổi vắng.

#### [MODIFY] API Quản lý Tài chính (Cốt lõi)
* **`FeeRecordsServiceImpl.java`**: Xử lý logic tự động gen hóa đơn học phí (`fee_records`) cho học sinh vào đầu tháng, tự tính `debt_amount`.
* **`PaymentsServiceImpl.java`**: Xử lý logic khi thu ngân nộp tiền thanh toán -> update `paid_amount` và giảm `debt_amount` trong `fee_records`.
* **`TeacherSalariesServiceImpl.java`**: Logic tính lương giáo viên dựa vào số lượng `sessions_taught` thực tế từ bảng `attendances`.

#### [MODIFY] API Dashboard & Thống kê
* **`StatisticsServiceImpl.java`**: Truy vấn SQL gom nhóm dữ liệu (đếm HS, GV, sum doanh thu tháng, tổng nợ) và trả về format JSON phù hợp để vẽ Biểu đồ.

---

### Giai đoạn 2: Triển khai UI Frontend (Vanilla JS, HTML)

Chúng ta sẽ không dùng Framework nặng mà dùng JS thuần để nhẹ và dễ tích hợp.

#### [NEW] [admin.js](file:///d:/english-center/src/main/resources/static/assets/js/admin.js)
* Khởi tạo file xử lý mọi sự kiện click trên màn hình `admin.html`.
* Viết hàm fetch data Render bảng Danh sách lớp, Danh sách giáo viên, Thống kê doanh thu.
* Viết logic xử lý Form: Mở popup tạo lớp mới, Submit ghi nhận thanh toán.

#### [NEW] [ui.js](file:///d:/english-center/src/main/resources/static/assets/js/ui.js)
* Chứa các hàm tiện ích UI độc lập: Hàm hiển thị Toast notification (thông báo thành công/thất bại), quản lý Loading spinner, đóng mở Modal.

#### [MODIFY] [api.js](file:///d:/english-center/src/main/resources/static/assets/js/api.js)
* Hoàn thiện mapping tất cả các endpoint tương tác với Backend: `createClass()`, `recordPayment()`, `getDashboardStats()`, `markAttendance()`.

#### [MODIFY] Các màn hình HTML hiện tại
* **`admin.html`**: Gắn các thuộc tính `id` chuẩn để DOM Manipulation, style lại các Modal/Popup.
* **`teacher.html`**: Code màn hình hiển thị danh sách lớp và tick điểm danh.
* **`student.html`**: Code màn hình hiển thị công nợ và lịch học.

## Verification Plan

### Automated / API Tests
* Tiếp tục chạy dự án Spring Boot, dùng file `test.http` hiện có (cập nhật thêm các request mới) để test độc lập các API đảm bảo không có Exception và dữ liệu đúng logic.

### Manual Verification
* Truy cập `http://localhost:8080/admin.html` trên trình duyệt.
* Thử kịch bản: Thêm mới 1 giáo viên -> Thêm 1 lớp -> Đăng nhập tài khoản giáo viên -> Điểm danh thử -> Xem số buổi hiển thị trên bảng lương có chính xác hay không.
