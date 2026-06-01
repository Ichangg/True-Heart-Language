-- Tạo database mới
CREATE DATABASE EnglishCenterV2;
GO
USE EnglishCenterV2;
GO

-- BẢNG 1: users
CREATE TABLE users (
    id            BIGINT PRIMARY KEY IDENTITY(1,1),
    full_name     NVARCHAR(100)  NOT NULL,
    email         VARCHAR(150)   NOT NULL UNIQUE,
    phone         VARCHAR(20),
    password_hash VARCHAR(255)   NOT NULL,
    role          VARCHAR(10)    NOT NULL
                  CHECK (role IN ('admin','teacher','student','parent')),
    avatar_url    VARCHAR(500)   NULL,
    zalo_id       VARCHAR(100)   NULL,
    facebook_id   VARCHAR(100)   NULL,
    is_active     BIT            NOT NULL DEFAULT 1,
    deleted_at    DATETIME       NULL,
    created_at    DATETIME       NOT NULL DEFAULT GETDATE()
);

select * from users
GO

-- BẢNG 2:Khối/Lứa tuổi
CREATE TABLE class_groups (
    id          BIGINT PRIMARY KEY IDENTITY(1,1),
    name        NVARCHAR(50)   NOT NULL,  -- VD: "Lớp 3"
    age_range   VARCHAR(20)    NULL,      -- VD: "8-9 tuổi"
    description NVARCHAR(500)  NULL,
    created_at  DATETIME       NOT NULL DEFAULT GETDATE()
);
GO

-- BẢNG 3:Lớp cụ thể
CREATE TABLE classes (
    id              BIGINT PRIMARY KEY IDENTITY(1,1),
    class_group_id  BIGINT         NOT NULL REFERENCES class_groups(id),
    name            VARCHAR(20)    NOT NULL,  -- VD: "3.1"
    year            INT            NOT NULL,  -- VD: 2024
    fee_per_session DECIMAL(12,2)  NOT NULL,  -- Học phí mỗi buổi
    max_students    INT            NOT NULL DEFAULT 20,
    schedule        NVARCHAR(500)  NULL,      -- Lịch học (JSON string)
    status          VARCHAR(10)    NOT NULL DEFAULT 'active'
                    CHECK (status IN ('active','closed')),
    deleted_at      DATETIME       NULL,
    created_at      DATETIME       NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_Class UNIQUE (class_group_id, name, year)
);
GO

-- BẢNG 4: class_teachers
CREATE TABLE class_teachers (
    id                BIGINT PRIMARY KEY IDENTITY(1,1),
    class_id          BIGINT          NOT NULL REFERENCES classes(id),
    teacher_id        BIGINT          NOT NULL REFERENCES users(id),
    is_primary        BIT             NOT NULL DEFAULT 1,
    salary_per_session DECIMAL(12,2)  NULL,
    assigned_at       DATETIME        NOT NULL DEFAULT GETDATE(),
    removed_at        DATETIME        NULL,

    CONSTRAINT UQ_ClassTeacher UNIQUE (class_id, teacher_id)
);
GO


-- BẢNG 5: class_students (Enrollment)
CREATE TABLE class_students (
    id               BIGINT PRIMARY KEY IDENTITY(1,1),
    class_id         BIGINT          NOT NULL REFERENCES classes(id),
    student_id       BIGINT          NOT NULL REFERENCES users(id),
    discount_percent DECIMAL(5,2)    NOT NULL DEFAULT 0,
    custom_fee       DECIMAL(12,2)   NULL,
    enrolled_at      DATETIME        NOT NULL DEFAULT GETDATE(),
    left_at          DATETIME        NULL,
    status           VARCHAR(10)     NOT NULL DEFAULT 'active'
                     CHECK (status IN ('active','left')),

    CONSTRAINT UQ_ClassStudent UNIQUE (class_id, student_id)
);
GO

-- BẢNG 6: parent_student
CREATE TABLE parent_student (
    id                  BIGINT PRIMARY KEY IDENTITY(1,1),
    parent_id           BIGINT      NOT NULL REFERENCES users(id),
    student_id          BIGINT      NOT NULL REFERENCES users(id),
    relationship        VARCHAR(10) NOT NULL
                        CHECK (relationship IN ('father','mother','guardian')),
    is_primary_contact  BIT         NOT NULL DEFAULT 1,
    created_at          DATETIME    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_ParentStudent UNIQUE (parent_id, student_id)
);
GO

-- BẢNG 7: sessions (Buổi học)

CREATE TABLE sessions (
    id           BIGINT PRIMARY KEY IDENTITY(1,1),
    class_id     BIGINT      NOT NULL REFERENCES classes(id),
    teacher_id   BIGINT      NOT NULL REFERENCES users(id),
    session_date DATE        NOT NULL,
    start_time   TIME        NULL,
    end_time     TIME        NULL,
    status       VARCHAR(10) NOT NULL DEFAULT 'scheduled'
                 CHECK (status IN ('scheduled','done','cancelled')),
    note         NVARCHAR(500) NULL,
    created_at   DATETIME    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_Session UNIQUE (class_id, session_date)
);
GO


-- BẢNG 8: attendances (Điểm danh)

CREATE TABLE attendances (
    id               BIGINT PRIMARY KEY IDENTITY(1,1),
    session_id       BIGINT      NOT NULL REFERENCES sessions(id),
    student_id       BIGINT      NOT NULL REFERENCES users(id),
    status           VARCHAR(10) NOT NULL
                     CHECK (status IN ('present','absent','late','excused')),
    note             NVARCHAR(300) NULL,
    notified_parent  BIT         NOT NULL DEFAULT 0,
    marked_at        DATETIME    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_Attendance UNIQUE (session_id, student_id)
);
GO

-- BẢNG 9:Hồ sơ học phí tháng

CREATE TABLE fee_records (
    id                BIGINT PRIMARY KEY IDENTITY(1,1),
    student_id        BIGINT          NOT NULL REFERENCES users(id),
    class_id          BIGINT          NOT NULL REFERENCES classes(id),
    month             DATE            NOT NULL,  -- VD: 2024-03-01
    sessions_attended INT             NOT NULL DEFAULT 0,
    fee_base          DECIMAL(12,2)   NOT NULL,  -- Tiền gốc
    discount_amount   DECIMAL(12,2)   NOT NULL DEFAULT 0,
    fee_final         DECIMAL(12,2)   NOT NULL,  -- Sau giảm
    paid_amount       DECIMAL(12,2)   NOT NULL DEFAULT 0,
    debt_amount       AS (fee_final - paid_amount),  -- Tự tính
    status            VARCHAR(10)     NOT NULL DEFAULT 'unpaid'
                      CHECK (status IN ('unpaid','partial','paid')),
    created_at        DATETIME        NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_FeeRecord UNIQUE (student_id, class_id, month)
);
GO


-- BẢNG 10: payments (Mỗi lần đóng tiền)
CREATE TABLE payments (
    id              BIGINT PRIMARY KEY IDENTITY(1,1),
    fee_record_id   BIGINT      NOT NULL REFERENCES fee_records(id),
    collected_by    BIGINT      NOT NULL REFERENCES users(id),
    amount          DECIMAL(12,2) NOT NULL,
    payment_method  VARCHAR(10) NOT NULL DEFAULT 'cash'
                    CHECK (payment_method IN ('cash','transfer')),
    note            NVARCHAR(300) NULL,
    paid_at         DATETIME    NOT NULL DEFAULT GETDATE()
);
GO


-- BẢNG 11: teacher_salaries

CREATE TABLE teacher_salaries (
    id             BIGINT PRIMARY KEY IDENTITY(1,1),
    teacher_id     BIGINT          NOT NULL REFERENCES users(id),
    class_id       BIGINT          NOT NULL REFERENCES classes(id),
    month          DATE            NOT NULL,
    sessions_taught INT            NOT NULL DEFAULT 0,
    total_salary   DECIMAL(12,2)   NOT NULL,
    paid_amount    DECIMAL(12,2)   NOT NULL DEFAULT 0,
    status         VARCHAR(10)     NOT NULL DEFAULT 'pending'
                   CHECK (status IN ('pending','paid')),
    paid_at        DATETIME        NULL,

    CONSTRAINT UQ_TeacherSalary UNIQUE (teacher_id, class_id, month)
);
GO


-- BẢNG 12: cms_banners

CREATE TABLE cms_banners (
    id         BIGINT PRIMARY KEY IDENTITY(1,1),
    type       VARCHAR(10)   NOT NULL
               CHECK (type IN ('popup','slider','banner')),
    title      NVARCHAR(200) NOT NULL,
    content    NVARCHAR(MAX) NULL,
    image_url  VARCHAR(500)  NULL,
    link_url   VARCHAR(500)  NULL,
    is_active  BIT           NOT NULL DEFAULT 1,
    start_at   DATETIME      NULL,
    end_at     DATETIME      NULL,
    position   INT           NOT NULL DEFAULT 0,
    created_at DATETIME      NOT NULL DEFAULT GETDATE()
);
GO


-- BẢNG 13: notification_logs

CREATE TABLE notification_logs (
    id           BIGINT PRIMARY KEY IDENTITY(1,1),
    sent_by      BIGINT      NOT NULL REFERENCES users(id),
    recipient_id BIGINT      NOT NULL REFERENCES users(id),
    channel      VARCHAR(10) NOT NULL
                 CHECK (channel IN ('zalo','facebook','sms','inapp')),
    type         VARCHAR(20) NOT NULL
                 CHECK (type IN ('attendance','fee','class_cancel','general')),
    message      NVARCHAR(MAX) NOT NULL,
    status       VARCHAR(10) NOT NULL DEFAULT 'pending'
                 CHECK (status IN ('sent','failed','pending')),
    sent_at      DATETIME    NULL,
    error_msg    NVARCHAR(500) NULL,
    created_at   DATETIME    NOT NULL DEFAULT GETDATE()
);
GO

-- =============================================
-- BẢNG 14: system_settings
-- =============================================
CREATE TABLE system_settings (
    [key]       VARCHAR(100)  PRIMARY KEY,
    value       NVARCHAR(500) NOT NULL,
    description NVARCHAR(300) NULL,
    updated_at  DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

-- =============================================
-- SEED DATA
-- =============================================

-- Users (mật khẩu đều là "123456")

INSERT INTO users (full_name, email, phone, password_hash, role) VALUES
(N'Nguyễn Quản Trị', 'admin@trungtam.com',  '0901000001', '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'admin'),
(N'Trần Thị Lan',    'lan@trungtam.com',    '0901000002', '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'teacher'),
(N'Lê Văn Minh',     'minh@trungtam.com',   '0901000003', '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'teacher'),
(N'Nguyễn Bảo An',  'an@trungtam.com',     NULL,         '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'student'),
(N'Phạm Minh Khoa', 'khoa@trungtam.com',   NULL,         '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'student'),
(N'Nguyễn Văn Bình','binh@gmail.com',      '0908000001', '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'parent'),
(N'Phạm Thị Hoa',   'hoa@gmail.com',       '0908000002', '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG', 'parent');

GO

-- class_groups
INSERT INTO class_groups (name, age_range, description) VALUES
(N'Lớp 3', N'8-9 tuổi',  N'Khối lớp 3 dành cho trẻ 8-9 tuổi'),
(N'Lớp 4', N'9-10 tuổi', N'Khối lớp 4 dành cho trẻ 9-10 tuổi'),
(N'Lớp 5', N'10-11 tuổi',N'Khối lớp 5 dành cho trẻ 10-11 tuổi');

GO

-- classes
INSERT INTO classes (class_group_id, name, year, fee_per_session, max_students, status) VALUES
(1, '3.1', 2024, 100000, 20, 'active'),
(1, '3.2', 2024, 100000, 20, 'active'),
(3, '5.1', 2024, 120000, 15, 'active');
GO

-- class_teachers
INSERT INTO class_teachers (class_id, teacher_id, is_primary, salary_per_session) VALUES
(1, 2, 1, 200000),
(2, 3, 1, 200000),
(3, 2, 1, 220000);
GO

-- class_students
INSERT INTO class_students (class_id, student_id, discount_percent) VALUES
(1, 4, 0),
(1, 5, 20),
(3, 4, 0);
GO

-- parent_student
INSERT INTO parent_student (parent_id, student_id, relationship, is_primary_contact) VALUES
(6, 4, 'father', 1),
(7, 5, 'mother', 1);
GO

-- system_settings
INSERT INTO system_settings ([key], value, description) VALUES
('show_teacher_to_parent', 'true',  N'Hiển thị tên giáo viên cho phụ huynh'),
('max_absent_notify',      '2',     N'Số buổi vắng tối đa trước khi thông báo'),
('center_name',            N'Trung tâm Tiếng Anh ABC', N'Tên trung tâm'),
('center_phone',           '0901234567', N'Số điện thoại trung tâm');
GO

-- =============================================
-- KIỂM TRA
-- =============================================
SELECT 'users'             AS Bang, COUNT(*) AS SoBanGhi FROM users             UNION ALL
SELECT 'class_groups',              COUNT(*)              FROM class_groups       UNION ALL
SELECT 'classes',                   COUNT(*)              FROM classes            UNION ALL
SELECT 'class_teachers',            COUNT(*)              FROM class_teachers     UNION ALL
SELECT 'class_students',            COUNT(*)              FROM class_students     UNION ALL
SELECT 'parent_student',            COUNT(*)              FROM parent_student     UNION ALL
SELECT 'system_settings',           COUNT(*)              FROM system_settings;

GO

SELECT DB_NAME() AS CurrentDatabase;
DELETE FROM users
WHERE id BETWEEN 8 AND 11;
select * from users