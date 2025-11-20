
CREATE DATABASE QL_PhanCongCongViec;
USE QL_PhanCongCongViec;
CREATE TABLE PhongBan (
    MaPB INT PRIMARY KEY IDENTITY,
    TenPB NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(255),
    TruongPhong INT NULL  -- NULL tạm để tránh vòng FK
);

-- 3️⃣ Tạo bảng NhanVien
CREATE TABLE NhanVien (
    MaNV INT PRIMARY KEY IDENTITY,
    HoTen NVARCHAR(100) NOT NULL,
    GioiTinh NVARCHAR(10) CHECK (GioiTinh IN (N'Nam', N'Nữ')),
    NgaySinh DATE,
    Email NVARCHAR(100),
    SDT NVARCHAR(20),
    DiaChi NVARCHAR(255),
    ChucVu NVARCHAR(50),
    NgayVaoLam DATE,
    MaPB INT,
    TrangThai NVARCHAR(20) DEFAULT N'Đang làm',
	  LuongCoBan DECIMAL(12,2), 
	  HeSoLuong DECIMAL(12,2),
);

-- 4️⃣ Chèn phòng ban trước, TruongPhong NULL
INSERT INTO PhongBan (TenPB, MoTa) VALUES
(N'Phòng Kỹ Thuật', N'Phụ trách kỹ thuật và phát triển sản phẩm'),
(N'Phòng Nhân Sự', N'Quản lý nhân sự và tuyển dụng'),
(N'Phòng Tài Chính - Kế toán', N'Quản lý ngân sách'),
(N'Phòng Marketing', N'Quản lý truyền thông và quảng cáo');

-- 5️⃣ Chèn nhân viên (tham chiếu MaPB)
INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, Email, SDT, DiaChi, ChucVu, NgayVaoLam, MaPB, TrangThai)
VALUES
(N'Nguyễn Văn A', N'Nam', '1995-03-12', 'nguyenvana@cty.com', '0901111111', N'Bình Thạnh, TP.HCM', N'Nhân viên lập trình', '2023-03-01', 1, N'Đang làm'),
(N'Lê Thị B',    N'Nữ', '1998-07-20', 'lethib@cty.com',    '0902222222', N'Tân Bình, TP.HCM', N'Thiết kế UI/UX', '2023-05-10', 2, N'Đang làm'),
(N'Quản trị viên',N'Nam', '1990-01-10', 'admin@cty.com',     '0909999999', N'Quận 3, TP.HCM', N'Admin hệ thống', '2022-01-01', 3, N'Đang làm');

-- 6️⃣ Cập nhật TruongPhong
UPDATE PhongBan SET TruongPhong = (SELECT TOP 1 MaNV FROM NhanVien WHERE HoTen = N'Nguyễn Văn A') WHERE TenPB = N'Phòng Kỹ Thuật';
UPDATE PhongBan SET TruongPhong = (SELECT TOP 1 MaNV FROM NhanVien WHERE HoTen = N'Lê Thị B') WHERE TenPB = N'Phòng Nhân Sự';
UPDATE PhongBan SET TruongPhong = (SELECT TOP 1 MaNV FROM NhanVien WHERE HoTen = N'Quản trị viên') WHERE TenPB = N'Phòng Tài Chính - Kế toán';

-- 7️⃣ Thêm FK sau khi dữ liệu đã hợp lệ
ALTER TABLE NhanVien
ADD CONSTRAINT FK_NhanVien_PhongBan FOREIGN KEY (MaPB) REFERENCES PhongBan(MaPB);

ALTER TABLE PhongBan
ADD CONSTRAINT FK_PhongBan_NhanVien FOREIGN KEY (TruongPhong) REFERENCES NhanVien(MaNV);

CREATE TABLE TaiKhoan (
    MaNV INT PRIMARY KEY, -- Liên kết 1-1 với nhân viên
    TenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    MatKhau NVARCHAR(255) NOT NULL,
    VaiTro NVARCHAR(50) NOT NULL,
    rememberToken NVARCHAR(255) NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

INSERT INTO TaiKhoan (MaNV, TenDangNhap, MatKhau, VaiTro)
VALUES
(1, N'admin', '123456', N'Admin'),
(2, N'nguyenvana', '123456', N'Nhân viên'),
(3, N'lethib', '123456', N'Nhân viên');

-- ===== PHÒNG KỸ THUẬT (MaPB = 1) =====
INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, Email, SDT, DiaChi, ChucVu, NgayVaoLam, MaPB, TrangThai)
VALUES
(N'Trần Văn Hùng', N'Nam', '1988-02-15', 'hungtv@cty.com', '0903000001', N'Gò Vấp, TP.HCM', N'Trưởng phòng', '2020-01-01', 1, N'Đang làm'),
(N'Lưu Thị Mai', N'Nữ', '1992-06-25', 'mailt@cty.com', '0903000002', N'Phú Nhuận, TP.HCM', N'Phó phòng', '2021-03-12', 1, N'Đang làm'),
(N'Nguyễn Thành Tâm', N'Nam', '1996-09-10', 'tamnt@cty.com', '0903000003', N'Bình Thạnh, TP.HCM', N'Kỹ sư phần mềm', '2022-05-05', 1, N'Đang làm'),
(N'Phạm Thị Hoa', N'Nữ', '1998-04-18', 'hoapt@cty.com', '0903000004', N'Tân Phú, TP.HCM', N'Lập trình viên', '2023-01-10', 1, N'Đang làm'),
(N'Lê Hoàng Nam', N'Nam', '1997-11-22', 'namlh@cty.com', '0903000005', N'Thủ Đức, TP.HCM', N'Lập trình viên', '2023-02-20', 1, N'Đang làm'),
(N'Ngô Thanh Bình', N'Nam', '1995-10-05', 'binhnt@cty.com', '0903000006', N'Bình Tân, TP.HCM', N'Tester', '2023-03-01', 1, N'Đang làm'),
(N'Đặng Mỹ Duyên', N'Nữ', '1999-01-12', 'duyendm@cty.com', '0903000007', N'Quận 1, TP.HCM', N'Lập trình viên', '2024-04-10', 1, N'Đang làm'),
(N'Phan Văn Đạt', N'Nam', '1996-03-14', 'datpv@cty.com', '0903000008', N'Quận 2, TP.HCM', N'Lập trình viên', '2024-05-05', 1, N'Đang làm'),
(N'Lê Thị Hồng', N'Nữ', '2000-07-09', 'honglt@cty.com', '0903000009', N'Quận 9, TP.HCM', N'Tester', '2024-06-15', 1, N'Đang làm'),
(N'Trương Quốc Việt', N'Nam', '1994-05-19', 'viettq@cty.com', '0903000010', N'Bình Chánh, TP.HCM', N'Lập trình viên', '2024-07-01', 1, N'Đang làm');

-- ===== PHÒNG NHÂN SỰ (MaPB = 2) =====
INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, Email, SDT, DiaChi, ChucVu, NgayVaoLam, MaPB, TrangThai)
VALUES
(N'Võ Thị Hạnh', N'Nữ', '1987-01-15', 'hanhvt@cty.com', '0904000001', N'Quận 5, TP.HCM', N'Trưởng phòng', '2019-01-01', 2, N'Đang làm'),
(N'Nguyễn Văn Phúc', N'Nam', '1990-05-10', 'phucnv@cty.com', '0904000002', N'Quận 10, TP.HCM', N'Phó phòng', '2020-02-20', 2, N'Đang làm'),
(N'Lê Hồng Nhung', N'Nữ', '1994-03-15', 'nhunglh@cty.com', '0904000003', N'Tân Bình, TP.HCM', N'Nhân viên tuyển dụng', '2022-03-01', 2, N'Đang làm'),
(N'Phạm Văn Dũng', N'Nam', '1996-09-19', 'dungpv@cty.com', '0904000004', N'Gò Vấp, TP.HCM', N'Nhân viên nhân sự', '2023-01-01', 2, N'Đang làm'),
(N'Ngô Mai Hương', N'Nữ', '1998-11-28', 'huongnm@cty.com', '0904000005', N'Thủ Đức, TP.HCM', N'Chuyên viên đào tạo', '2023-04-05', 2, N'Đang làm'),
(N'Trần Quốc Cường', N'Nam', '1995-06-12', 'cuongtq@cty.com', '0904000006', N'Bình Thạnh, TP.HCM', N'Nhân viên hành chính', '2023-05-10', 2, N'Đang làm'),
(N'Lê Thị Mỹ', N'Nữ', '1999-02-17', 'myle@cty.com', '0904000007', N'Quận 4, TP.HCM', N'Nhân viên tuyển dụng', '2024-02-02', 2, N'Đang làm'),
(N'Đoàn Quốc Huy', N'Nam', '1997-08-30', 'huydq@cty.com', '0904000008', N'Quận 7, TP.HCM', N'Nhân viên hành chính', '2024-03-05', 2, N'Đang làm'),
(N'Nguyễn Thị Thu', N'Nữ', '2000-10-02', 'thunt@cty.com', '0904000009', N'Quận 8, TP.HCM', N'Nhân viên nhân sự', '2024-05-10', 2, N'Đang làm'),
(N'Phạm Minh Tâm', N'Nam', '1995-12-14', 'tampm@cty.com', '0904000010', N'Bình Tân, TP.HCM', N'Nhân viên hành chính', '2024-06-01', 2, N'Đang làm');

-- ===== PHÒNG TÀI CHÍNH - KẾ TOÁN (MaPB = 3) =====
INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, Email, SDT, DiaChi, ChucVu, NgayVaoLam, MaPB, TrangThai)
VALUES
(N'Nguyễn Hoàng Lan', N'Nữ', '1989-02-05', 'lanhn@cty.com', '0905000001', N'Quận 3, TP.HCM', N'Trưởng phòng', '2020-05-10', 3, N'Đang làm'),
(N'Phan Văn Toàn', N'Nam', '1991-08-20', 'toanpv@cty.com', '0905000002', N'Quận 1, TP.HCM', N'Phó phòng', '2021-01-01', 3, N'Đang làm'),
(N'Lê Kim Ngân', N'Nữ', '1996-04-17', 'nganlk@cty.com', '0905000003', N'Bình Thạnh, TP.HCM', N'Kế toán viên', '2022-05-01', 3, N'Đang làm'),
(N'Ngô Minh Đức', N'Nam', '1994-09-10', 'ducnm@cty.com', '0905000004', N'Tân Bình, TP.HCM', N'Nhân viên tài chính', '2023-01-20', 3, N'Đang làm'),
(N'Phạm Thị Hạnh', N'Nữ', '1998-03-15', 'hanhpt@cty.com', '0905000005', N'Quận 4, TP.HCM', N'Kế toán viên', '2023-03-01', 3, N'Đang làm'),
(N'Trần Văn Long', N'Nam', '1997-05-11', 'longtv@cty.com', '0905000006', N'Quận 2, TP.HCM', N'Nhân viên kế toán', '2023-06-01', 3, N'Đang làm'),
(N'Đỗ Thị Mai', N'Nữ', '1999-07-07', 'maidt@cty.com', '0905000007', N'Bình Chánh, TP.HCM', N'Thủ quỹ', '2023-09-09', 3, N'Đang làm'),
(N'Võ Văn Hiếu', N'Nam', '1995-11-03', 'hieuvv@cty.com', '0905000008', N'Bình Tân, TP.HCM', N'Nhân viên kế toán', '2024-01-15', 3, N'Đang làm'),
(N'Lâm Thị Thảo', N'Nữ', '2000-01-28', 'thaolt@cty.com', '0905000009', N'Tân Phú, TP.HCM', N'Kế toán viên', '2024-03-12', 3, N'Đang làm'),
(N'Phạm Đức Duy', N'Nam', '1996-06-06', 'duypd@cty.com', '0905000010', N'Bình Thạnh, TP.HCM', N'Nhân viên tài chính', '2024-06-10', 3, N'Đang làm');

-- ===== PHÒNG MARKETING (MaPB = 4) =====
INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, Email, SDT, DiaChi, ChucVu, NgayVaoLam, MaPB, TrangThai)
VALUES
(N'Phạm Thị Ly', N'Nữ', '1986-10-11', 'lypham@cty.com', '0906000001', N'Quận 1, TP.HCM', N'Trưởng phòng', '2019-05-01', 4, N'Đang làm'),
(N'Trần Quốc Khánh', N'Nam', '1990-12-09', 'khanhtq@cty.com', '0906000002', N'Quận 10, TP.HCM', N'Phó phòng', '2020-02-10', 4, N'Đang làm'),
(N'Lê Thị Mỹ Duyên', N'Nữ', '1997-04-08', 'duyenltm@cty.com', '0906000003', N'Tân Bình, TP.HCM', N'Nhân viên truyền thông', '2022-03-15', 4, N'Đang làm'),
(N'Nguyễn Hoàng Minh', N'Nam', '1998-06-12', 'minhnh@cty.com', '0906000004', N'Bình Thạnh, TP.HCM', N'Nhân viên PR', '2023-01-25', 4, N'Đang làm'),
(N'Phạm Thị Nhung', N'Nữ', '1999-02-18', 'nhungpt@cty.com', '0906000005', N'Phú Nhuận, TP.HCM', N'Chuyên viên nội dung', '2023-03-03', 4, N'Đang làm'),
(N'Trần Văn Tú', N'Nam', '1996-08-07', 'tutv@cty.com', '0906000006', N'Thủ Đức, TP.HCM', N'Chuyên viên quảng cáo', '2023-04-20', 4, N'Đang làm'),
(N'Lê Minh Thư', N'Nữ', '1998-05-22', 'thulm@cty.com', '0906000007', N'Quận 3, TP.HCM', N'Nhân viên SEO', '2023-06-06', 4, N'Đang làm'),
(N'Đặng Quốc Hào', N'Nam', '1995-09-10', 'haodq@cty.com', '0906000008', N'Quận 5, TP.HCM', N'Chuyên viên Marketing', '2023-08-01', 4, N'Đang làm'),
(N'Nguyễn Thị Tố', N'Nữ', '2000-11-02', 'tont@cty.com', '0906000009', N'Bình Chánh, TP.HCM', N'Nhân viên PR', '2024-01-05', 4, N'Đang làm'),
(N'Phan Văn Phát', N'Nam', '1997-03-27', 'phatpv@cty.com', '0906000010', N'Quận 9, TP.HCM', N'Nhân viên truyền thông', '2024-05-01', 4, N'Đang làm');

CREATE TABLE DuAn (
    MaDuAn INT PRIMARY KEY Identity,
    TenDuAn NVARCHAR(200) NOT NULL,
    NgayBatDau DATE,
    NgayKetThuc DATE,
    MoTa NVARCHAR(255)
);

INSERT INTO DuAn (TenDuAn, NgayBatDau, NgayKetThuc, MoTa)
VALUES
(N'Hệ thống quản lý phân công công việc', '2025-09-01', '2025-12-31', N'Dự án nội bộ nâng cao hiệu quả quản lý công việc.');

CREATE TABLE ChiTietDuAn (
    MaTask INT PRIMARY KEY Identity,
    TenTask NVARCHAR(200) NOT NULL,
    MoTa NVARCHAR(255),
    HanHoanThanh DATE,
    TrangThai NVARCHAR(50),
    MucDoUuTien NVARCHAR(20),
    MaDuAn INT,
    FOREIGN KEY (MaDuAn) REFERENCES DuAn(MaDuAn)
);

INSERT INTO ChiTietDuAn (TenTask, MoTa, HanHoanThanh, TrangThai, MucDoUuTien, MaDuAn)
VALUES
(N'Phân tích yêu cầu', N'Phân tích chức năng và quy trình làm việc', '2025-09-15', N'Hoàn thành', N'Cao', 1),
(N'Phát triển module phân công', N'Lập trình chức năng phân công task cho nhân viên', '2025-10-20', N'Đang thực hiện', N'Cao', 1);

CREATE TABLE PhanCong (
    MaPhanCong INT PRIMARY KEY Identity,
    MaTask INT,
    MaNV INT,
    GhiChu NVARCHAR(255),
    FOREIGN KEY (MaTask) REFERENCES ChiTietDuAn(MaTask),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

INSERT INTO PhanCong (MaTask, MaNV, GhiChu)
VALUES
(1, 1, N'Phụ trách chính'),
(2, 2, N'Hỗ trợ UI');

CREATE TABLE ChamCong (
    MaCC INT PRIMARY KEY Identity,
    MaNV INT,
    NgayLam DATE,
    GioVao TIME,
    GioRa TIME,
    SoGioLam DECIMAL(4,2),
    TrangThai NVARCHAR(50),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);


INSERT INTO ChamCong (MaNV, NgayLam, GioVao, GioRa, SoGioLam, TrangThai)
VALUES 
(1, '2025-10-21', '08:00', '17:00', 8, N'present'),
(2, '2025-10-21', '08:15', '17:00', 7.75, N'present');
-- Nhân viên 1
INSERT INTO ChamCong (MaNV, NgayLam, GioVao, GioRa, SoGioLam, TrangThai)
VALUES 
(1, '2025-11-01', '08:00', '17:00', 8, N'present'),
(1, '2025-11-02', '08:00', '17:00', 8, N'present'),
(1, '2025-11-03', '08:00', '17:00', 8, N'present'),
(1, '2025-11-04', '08:00', '17:00', 8, N'present'),
(1, '2025-11-05', '08:00', '17:00', 8, N'present'),
(1, '2025-11-06', '08:00', '17:00', 8, N'present'),
(1, '2025-11-07', '08:00', '17:00', 8, N'present'),
(1, '2025-11-08', '08:00', '17:00', 8, N'present'),
(1, '2025-11-09', '08:00', '17:00', 8, N'present'),
(1, '2025-11-10', '08:00', '17:00', 8, N'present'),
(1, '2025-11-11', '08:00', '17:00', 8, N'present'),
(1, '2025-11-12', '08:00', '17:00', 8, N'present'),
(1, '2025-11-13', '08:00', '17:00', 8, N'present'),
(1, '2025-11-14', '08:00', '17:00', 8, N'present'),
(1, '2025-11-15', '08:00', '17:00', 8, N'present');

-- Nhân viên 2
INSERT INTO ChamCong (MaNV, NgayLam, GioVao, GioRa, SoGioLam, TrangThai)
VALUES 
(2, '2025-11-01', '08:00', '17:00', 8, N'present'),
(2, '2025-11-02', '08:00', '17:00', 8, N'present'),
(2, '2025-11-03', '08:00', '17:00', 8, N'present'),
(2, '2025-11-04', '08:00', '17:00', 8, N'present'),
(2, '2025-11-05', '08:00', '17:00', 8, N'present'),
(2, '2025-11-06', '08:00', '17:00', 8, N'present'),
(2, '2025-11-07', '08:00', '17:00', 8, N'present'),
(2, '2025-11-08', '08:00', '17:00', 8, N'present'),
(2, '2025-11-09', '08:00', '17:00', 8, N'present'),
(2, '2025-11-10', '08:00', '17:00', 8, N'present'),
(2, '2025-11-11', '08:00', '17:00', 8, N'present'),
(2, '2025-11-12', '08:00', '17:00', 8, N'present'),
(2, '2025-11-13', '08:00', '17:00', 8, N'present'),
(2, '2025-11-14', '08:00', '17:00', 8, N'present'),
(2, '2025-11-15', '08:00', '17:00', 8, N'present');


CREATE TABLE TangCa (
    MaTangCa INT PRIMARY KEY Identity,
    MaNV INT,
    NgayTangCa DATE,
    SoGio DECIMAL(4,2),
    HeSo DECIMAL(3,2),
    GhiChu NVARCHAR(255),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

INSERT INTO TangCa (MaNV, NgayTangCa, SoGio, HeSo, GhiChu)
VALUES 
(1, '2025-10-22', 2, 1.5, N'Tăng ca hoàn thiện module'),
(2, '2025-10-22', 1.5, 1.5, N'Hỗ trợ thiết kế');

CREATE TABLE NghiPhep (
    MaNghiPhep INT PRIMARY KEY Identity,
    MaNV INT,
    NgayBatDau DATE,
    NgayKetThuc DATE,
    SoNgay DECIMAL(4,1),
    LyDo NVARCHAR(255),
    LoaiNghi NVARCHAR(50),
    TrangThai NVARCHAR(50),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

INSERT INTO NghiPhep (MaNV, NgayBatDau, NgayKetThuc, SoNgay, LyDo, LoaiNghi, TrangThai)
VALUES
(1, '2025-10-10', '2025-10-11', 2, N'Nghỉ phép cá nhân', N'Có phép', N'Đã duyệt');

CREATE TABLE PhuCap (
    MaPhuCap INT PRIMARY KEY Identity,
    MaNV INT,
    Thang INT,
    Nam INT,
    LoaiPhuCap NVARCHAR(100),
    SoTien DECIMAL(12,2),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

INSERT INTO PhuCap (MaNV, Thang, Nam, LoaiPhuCap, SoTien)
VALUES 
(1, 10, 2025, N'Phụ cấp ăn trưa', 500000),
(2, 10, 2025, N'Phụ cấp đi lại', 400000);

CREATE TABLE Luong (
    MaLuong INT PRIMARY KEY Identity,
    MaNV INT,
    Thang INT,
    Nam INT,
    LuongCoBan DECIMAL(12,2),
    SoNgayCong INT,
    TongPhuCap DECIMAL(12,2),
    Thuong DECIMAL(12,2),
    Phat DECIMAL(12,2),
    TongTangCa DECIMAL(12,2),
    TongKhauTru DECIMAL(12,2),
    TongThuNhap DECIMAL(12,2),
    ThucLinh DECIMAL(12,2),
	TrangThai VARCHAR(20) NOT NULL DEFAULT 'Unpaid'
    CHECK (TrangThai IN ('Paid', 'Unpaid')),
	GhiChu NVARCHAR(200),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

INSERT INTO Luong (MaNV, Thang, Nam, LuongCoBan, SoNgayCong, TongPhuCap, Thuong, Phat, TongTangCa, TongKhauTru, TongThuNhap, ThucLinh,TrangThai)
VALUES 
(1, 10, 2025, 15000000, 26, 500000, 1000000, 0, 300000, 0, 16800000, 16800000,'Unpaid'),
(2, 10, 2025, 13000000, 25, 400000, 500000, 0, 200000, 0, 14100000, 14100000,N'Paid');


CREATE TABLE ChiTietLuong (
    MaChiTietLuong INT PRIMARY KEY Identity,
    MaLuong INT,
    LoaiChiTiet NVARCHAR(100),
    MoTa NVARCHAR(255),
    SoTien DECIMAL(12,2),
    FOREIGN KEY (MaLuong) REFERENCES Luong(MaLuong)
);

INSERT INTO ChiTietLuong (MaLuong, LoaiChiTiet, MoTa, SoTien)
VALUES
(1, N'Lương cơ bản', N'Lương chính tháng 10', 15000000),
(1, N'Phụ cấp', N'Phụ cấp ăn trưa', 500000),
(1, N'Thưởng', N'Hoàn thành dự án sớm', 1000000),
(1, N'Tăng ca', N'2h tăng ca', 300000),

(2, N'Lương cơ bản', N'Lương chính tháng 10', 13000000),
(2, N'Phụ cấp', N'Phụ cấp đi lại', 400000),
(2, N'Thưởng', N'Hiệu suất tốt', 500000),
(2, N'Tăng ca', N'1.5h tăng ca', 200000);


CREATE VIEW v_ThongTinLuongChiTiet AS
SELECT 
    nv.MaNV,
    nv.HoTen,
    pb.TenPB,
    l.Thang,
    l.Nam,
    l.LuongCoBan,
    l.SoNgayCong,
    l.TongPhuCap,
    l.Thuong,
    l.Phat,
    l.TongTangCa,
    l.TongKhauTru,
    l.TongThuNhap,
    l.ThucLinh,
    (
        SELECT SUM(SoTien) 
        FROM ChiTietLuong ctl 
        WHERE ctl.MaLuong = l.MaLuong
    ) AS TongChiTietLuong
FROM Luong l
JOIN NhanVien nv ON nv.MaNV = l.MaNV
LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB;
GO

CREATE VIEW v_TangCaChiTiet AS
SELECT 
    tc.MaTangCa,
    nv.MaNV,
    nv.HoTen,
    pb.TenPB,
    tc.NgayTangCa,
    tc.SoGio,
    tc.HeSo,
    tc.GhiChu,
    CAST((tc.SoGio * (l.LuongCoBan / 26 / 8) * tc.HeSo) AS DECIMAL(18,2)) AS TienTangCa
FROM TangCa tc
JOIN NhanVien nv ON nv.MaNV = tc.MaNV
LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB
LEFT JOIN Luong l ON l.MaNV = nv.MaNV AND MONTH(tc.NgayTangCa) = l.Thang AND YEAR(tc.NgayTangCa) = l.Nam;
GO
use QL_PhanCongCongViec
CREATE OR ALTER VIEW v_ChamCongTongHop AS
SELECT 
    nv.MaNV,
    nv.HoTen,
    pb.TenPB,
    COUNT(CASE WHEN cc.TrangThai = N'Đi làm' THEN 1 END) AS SoNgayLam,
    COUNT(CASE WHEN cc.TrangThai = N'Vắng' THEN 1 END) AS SoNgayVang,
    SUM(cc.SoGioLam) AS TongGioLam
FROM ChamCong cc
JOIN NhanVien nv ON nv.MaNV = cc.MaNV
LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB
GROUP BY nv.MaNV, nv.HoTen, pb.TenPB;
GO

use QL_PhanCongCongViec
CREATE VIEW v_PhanCongChiTiet AS
SELECT 
    pc.MaPhanCong,
    da.TenDuAn,
    t.MaTask,
    t.TenTask,
    nv.MaNV,
    nv.HoTen,
    t.TrangThai,
    t.MucDoUuTien,
    DATEDIFF(DAY, GETDATE(), t.HanHoanThanh) AS ConLaiNgay
FROM PhanCong pc
JOIN ChiTietDuAn t ON pc.MaTask = t.MaTask
JOIN DuAn da ON da.MaDuAn = t.MaDuAn
JOIN NhanVien nv ON nv.MaNV = pc.MaNV;
GO

use QL_PhanCongCongViec

-- ================================================
-- VIEW 1: Tổng quan Dashboard
-- ================================================
CREATE VIEW vw_Dashboard_TongQuan AS
SELECT 
    COUNT(*) AS TongNhanVien,
    SUM(CASE WHEN TrangThai = N'Đang làm việc' THEN 1 ELSE 0 END) AS NhanVienDangLam,
    (SELECT COUNT(*) FROM PhongBan) AS TongPhongBan,
    CAST(
        (
            SELECT 
                CAST(SUM(CASE WHEN TrangThai = N'Đi làm' THEN 1 ELSE 0 END) AS FLOAT) /
                NULLIF(COUNT(*), 0) * 100
            FROM ChamCong
        AS FLOAT)
    AS DECIMAL(5,2)) AS TyLeChuyenCan
FROM NhanVien;

use QL_PhanCongCongViec

Create VIEW vw_NhanVienTheoPhongBan AS
SELECT 
    pb.TenPB,
    COUNT(nv.MaNV) AS SoLuongNhanVien
FROM PhongBan pb
LEFT JOIN NhanVien nv ON pb.MaPB = nv.MaPB
GROUP BY pb.TenPB;
Go 
use QL_PhanCongCongViec
CREATE VIEW vw_TyLeGioiTinh AS
SELECT 
    GioiTinh,
    COUNT(*) AS SoLuong
FROM NhanVien
GROUP BY GioiTinh;
Go 
use QL_PhanCongCongViec
CREATE VIEW vw_NhanVienMoiNhat AS
SELECT TOP 5
    nv.MaNV,
    nv.HoTen,
    pb.TenPB AS PhongBan,
    nv.ChucVu,
    nv.Email,
    nv.TrangThai,
    nv.NgayVaoLam
FROM NhanVien nv
LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB
ORDER BY nv.NgayVaoLam DESC;
go
use QL_PhanCongCongViec
CREATE TRIGGER trg_UpdateChiTietLuong
ON Luong
AFTER UPDATE
AS
BEGIN
    UPDATE ctl
    SET ctl.SoTien = i.LuongCoBan
    FROM ChiTietLuong ctl
    INNER JOIN inserted i ON ctl.MaLuong = i.MaLuong
    WHERE ctl.LoaiChiTiet = 'Lương cơ bản';
END
go 
use QL_PhanCongCongViec
ALTER TABLE NhanVien
ADD NgayTao DATETIME DEFAULT GETDATE();
