<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Quản lý nhân sự</title>
        <%@ include file="layout/css.jsp" %>
    </head>
    <body>

        <%@ include file="layout/header.jsp" %>
        <%@ include file="layout/sidebar.jsp" %>

        <!-- MAIN CONTENT -->
        <main class="main-content">
            <!-- Dashboard Header -->
            <div class="dashboard-header mb-4">
                <h1><i class="fas fa-money-bill-wave"></i> Quản Lý Lương</h1>
                <p>Quản lý thông tin lương và phụ cấp của nhân viên</p>
            </div>

            <!-- Search and Filter Section -->
            <div class="card mb-4 shadow-sm">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4 mb-3 mb-md-0">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-white"><i class="fas fa-search"></i></span>
                                </div>
                                <input type="text" class="form-control" id="searchInput" placeholder="Tìm kiếm theo tên, mã NV...">
                            </div>
                        </div>
                        <div class="col-md-3 mb-3 mb-md-0">
                            <select name="filterDepartment" class="form-control">
                                <option value="">Tất cả phòng ban</option>
                                <c:forEach var="pb" items="${salaryList[0].dsPhongBan}">
                                    <option value="${pb.maPB}">${pb.tenPB}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <select name="filterMonth" class="form-control">
                                <option value="">Chọn tháng</option>
                                <c:forEach var="i" begin="1" end="12">
                                    <c:set var="monthStr" value="${i < 10 ? '0' + i : i}" />
                                    <option value="${monthStr}/2025">
                                        Tháng ${i}/2025
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-primary" id="btnLocLuong">Lọc</button>
                        </div>
                        <div class="col-md-2 mt-2">
                            <button class="btn btn-primary btn-block" data-toggle="modal" data-target="#addSalaryModal">
                                <i class="fas fa-plus"></i> Thêm Mới
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Salary Statistics Cards -->
            <div class="row mb-4">
                <div class="col-md-3 mb-3">
                    <div class="card text-white" style="background: linear-gradient(135deg, #1bceff 0%, #0c62a8 100%);">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="mb-1">Tổng Lương Tháng</h6>
                                    <c:set var="tongLuongThangFormatted" value="${tongLuongThang >= 1e9 ? (tongLuongThang / 1e9) : (tongLuongThang / 1e6)}" />
                                    <c:set var="tongLuongThangUnit" value="${tongLuongThang >= 1e9 ? 'Tỷ' : 'Tr'}" />

                                    <h3 class="mb-0">
                                        <fmt:formatNumber value="${tongLuongThangFormatted}" type="number" maxFractionDigits="2" groupingUsed="true" /> ${tongLuongThangUnit}
                                    </h3>
                                </div>
                                <div class="stat-icon">
                                    <i class="fas fa-wallet fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="card text-white" style="background: linear-gradient(135deg, #32aacf 0%, #0c62a8 100%);">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="mb-1">Lương Trung Bình</h6>
                                    <c:set var="luongTrungBinhFormatted" value="${luongTrungBinh >= 1e9 ? (luongTrungBinh / 1e9) : (luongTrungBinh / 1e6)}" />
                                    <c:set var="luongTrungBinhUnit" value="${luongTrungBinh >= 1e9 ? 'Tỷ' : 'Tr'}" />

                                    <h3 class="mb-0">
                                        <fmt:formatNumber value="${luongTrungBinhFormatted}" type="number" maxFractionDigits="2" groupingUsed="true" /> ${luongTrungBinhUnit}
                                    </h3>
                                </div>
                                <div class="stat-icon">
                                    <i class="fas fa-chart-line fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="card text-white" style="background: linear-gradient(135deg, #0c62a8 0%, #1bceff 100%);">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="mb-1">Đã Thanh Toán</h6>
                                    <h3 class="mb-0">${daThanhToan}</h3>
                                </div>
                                <div class="stat-icon">
                                    <i class="fas fa-check-circle fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="card text-white" style="background: linear-gradient(135deg, #32aacf 0%, #1bceff 100%);">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="mb-1">Chờ Thanh Toán</h6>
                                    <h3 class="mb-0">${choThanhToan}</h3>
                                </div>
                                <div class="stat-icon">
                                    <i class="fas fa-clock fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Salary Table -->
            <div class="card shadow-sm">
                <div class="card-header" style="background: linear-gradient(135deg, #1bceff 0%, #0c62a8 100%); color: white;">
                    <h5 class="mb-0"><i class="fas fa-table"></i> Danh Sách Lương Nhân Viên</h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="bg-light">
                                <tr>
                                    <th class="text-nowrap text-center align-middle">Mã NV</th>
                                    <th class="text-nowrap text-center align-middle">Họ Tên</th>
                                    <th class="text-nowrap text-center align-middle">Phòng Ban</th>
                                    <th class="text-nowrap text-center align-middle">Chức Vụ</th>
                                    <th class="text-nowrap text-center align-middle">Lương Cơ Bản</th>
                                    <th class="text-nowrap text-center align-middle">Phụ Cấp</th>
                                    <th class="text-nowrap text-center align-middle">Thưởng</th>
                                    <th class="text-nowrap text-center align-middle">Tổng Lương</th>
                                    <th class="text-nowrap text-center align-middle">Trạng Thái</th>
                                    <th class="text-nowrap text-center align-middle">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody id="salaryTableBody">
                                <c:forEach var="l" items="${salaryList}">
                                    <tr>
                                        <td><strong>${l.maNV}</strong></td>
                                        <td class="text-nowrap">${l.hoTen}</td>
                                        <td class="text-nowrap">${l.phongBan}</td>
                                        <td class="text-nowrap">${l.chucVu}</td>
                                        <td><fmt:formatNumber value="${l.luongCoBan}" type="number" groupingUsed="true" /></td>
                                        <td><fmt:formatNumber value="${l.phuCap}" type="number" groupingUsed="true" /></td>
                                        <td><fmt:formatNumber value="${l.phuCap}" type="number" groupingUsed="true" /></td>
                                        <td><fmt:formatNumber value="${l.tongLuong}" type="number" groupingUsed="true" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${l.trangThai eq 'Paid'}">
                                                    <span class="badge badge-success">Đã thanh toán</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning text-white">Chờ thanh toán</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <button class="btn btn-sm btn-info" onclick="showSalaryDetail('${l.maNV}')">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button class="btn btn-sm btn-warning" 
                                                        id="btn-edit-salary" data-id="${l.maNV}"
                                                        data-toggle="modal" data-target="#editSalaryModal">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-danger"><i class="fas fa-trash"></i></button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-footer">
                    <nav>
                        <ul class="pagination pagination-luong mb-0 justify-content-center">
                            <!-- Nút Trước -->
                            <li class="page-item ${currentPageLuong == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPageLuong > 1 ? currentPageLuong - 1 : 1}&filterMonth=${param.filterMonth}&filterDepartment=${param.filterDepartment}" tabindex="-1">Trước</a>
                            </li>

                            <c:forEach var="i" begin="1" end="${totalLuongPages}">
                                <li class="page-item ${currentPageLuong == i ? 'active' : ''}">
                                    <a class="page-link" href="?page=${i}&filterMonth=${param.filterMonth}&filterDepartment=${param.filterDepartment}">${i}</a>
                                </li>
                            </c:forEach>

                            <!-- Nút Sau -->
                            <li class="page-item ${currentPageLuong == totalLuongPages ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPageLuong < totalLuongPages ? currentPageLuong + 1 : totalLuongPages}&filterMonth=${param.filterMonth}&filterDepartment=${param.filterDepartment}">Sau</a>
                            </li>
                        </ul>
                    </nav>
                    <input type="hidden" id="currentPageLuong" value="${currentPageLuong}">
                </div>
            </div>
        </main>

        <!-- Modal Thêm Mới -->
        <div class="modal fade" id="addSalaryModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header" style="background: linear-gradient(135deg, #1bceff 0%, #0c62a8 100%); color: white;">
                        <h5 class="modal-title"><i class="fas fa-plus-circle"></i> Thêm Thông Tin Lương</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="salaryForm">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>Nhân Viên <span class="text-danger">*</span></label>
                                        <select class="form-control" id="employeeSelect" required>
                                            <option value="">-- Chọn nhân viên --</option>
                                            <c:forEach var="nv" items="${dsNhanVien}">
                                                <option value="${nv.maNV}">${nv.hoTen} (${nv.maNV})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Phòng Ban <span class="text-danger">*</span></label>
                                        <select class="form-control" id="phongBan" disabled style="background-color: #fff3b0;"></select>               
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Chức Vụ <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="chucVu" placeholder="Nhập chức vụ" readonly style="background-color: #fff3b0;">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Tháng <span class="text-danger">*</span></label>
                                        <input type="month" class="form-control" value="2025-10" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Lương Cơ Bản (VNĐ) <span class="text-danger">*</span></label>
                                        <input type="number" class="form-control" id="luongCoBan" placeholder="Nhập lương cơ bản" required style="background-color: #fff3b0;">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Phụ Cấp (VNĐ)</label>
                                        <input type="number" class="form-control" placeholder="Nhập phụ cấp" value="0">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Thưởng (VNĐ)</label>
                                        <input type="number" class="form-control" placeholder="Nhập tiền thưởng" value="0">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Khấu Trừ (VNĐ)</label>
                                        <input type="number" class="form-control" placeholder="Nhập khấu trừ" value="0">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Trạng Thái <span class="text-danger">*</span></label>
                                        <select class="form-control" required>
                                            <option value="UnPaid">Chờ thanh toán</option>
                                            <option value="Paid">Đã thanh toán</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Ghi Chú</label>
                                <textarea class="form-control" rows="3" placeholder="Nhập ghi chú (nếu có)"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <i class="fas fa-times"></i> Đóng
                        </button>
                        <button type="submit" class="btn btn-primary" style="background: linear-gradient(135deg, #1bceff 0%, #0c62a8 100%); border: none;">
                            <i class="fas fa-save"></i> Lưu Lại
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal Sửa Lương -->
        <div class="modal fade" id="editSalaryModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header" style="background: linear-gradient(135deg, #ffc107 0%, #ff8c00 100%); color: white;">
                        <h5 class="modal-title"><i class="fas fa-edit"></i> Chỉnh Sửa Thông Tin Lương</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="editSalaryForm">
                            <!-- Hidden input lưu ID lương -->
                            <input type="hidden" id="editSalaryId" name="salaryId">

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Mã Nhân Viên <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="editMaNV" name="maNV" placeholder="Nhập mã NV" required readonly>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Họ Tên <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="editHoTen" name="hoTen" placeholder="Nhập họ tên" required readonly>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Phòng Ban <span class="text-danger">*</span></label>
                                        <input type="hidden" id="editPhongBanId" name="phongBanId">
                                        <input type="text" class="form-control" id="editPhongBan"  name="phongBan" placeholder="" readonly>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Chức Vụ <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="editChucVu" name="chucVu" placeholder="Nhập chức vụ" readonly>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Tháng <span class="text-danger">*</span></label>
                                        <input type="month" class="form-control" id="editThang" name="thang" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Lương Cơ Bản (VNĐ) <span class="text-danger">*</span></label>
                                        <input type="number" class="form-control" id="editLuongCoBan" name="luongCoBan" required>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Phụ Cấp (VNĐ)</label>
                                        <input type="number" class="form-control" id="editPhuCap" name="phuCap" value="0">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Thưởng (VNĐ)</label>
                                        <input type="number" class="form-control" id="editThuong" name="thuong" value="0">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Khấu Trừ (VNĐ)</label>
                                        <input type="number" class="form-control" id="editKhauTru" name="khauTru" value="0">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Trạng Thái <span class="text-danger">*</span></label>
                                        <select class="form-control" id="editTrangThai" name="trangThai" required>
                                            <option value="Unpaid">Chờ thanh toán</option>
                                            <option value="Paid">Đã thanh toán</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Ghi Chú</label>
                                <textarea class="form-control" id="editGhiChu" name="ghiChu" rows="3" placeholder="Nhập ghi chú (nếu có)"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <i class="fas fa-times"></i> Đóng
                        </button>
                        <button type="button" class="btn btn-warning" id="btnSaveEditSalary">
                            <i class="fas fa-save"></i> Lưu Thay Đổi
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Chi Tiết Lương -->
        <div class="modal fade" id="salaryDetailModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header" style="background: linear-gradient(135deg, #1bceff 0%, #0c62a8 100%); color: white;">
                        <h5 class="modal-title"><i class="fas fa-file-invoice-dollar"></i> Chi Tiết Lương Nhân Viên</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="salaryDetailBody">
                        <!-- Nội dung sẽ load từ AJAX -->
                        <div class="text-center">
                            <i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <i class="fas fa-times"></i> Đóng
                        </button>
                        <button type="button" class="btn btn-primary" id="exportExcelCTL">
                            <i class="fas fa-file-excel"></i> Xuất Excel
                        </button>
                        <button type="button" class="btn btn-primary" id="exportPDFCTL">
                            <i class="fas fa-file-pdf"></i> Xuất PDF
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="layout/footer.jsp" %>
    </body>
    <%@ include file="layout/js.jsp" %>
</html>
