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
            <div class="container-fluid">
                <!-- Page Header -->
                <div class="row mb-4">
                    <div class="col-12">
                        <h2 class="mb-1"><i class="fas fa-calendar-check"></i> Quản Lý Chấm Công</h2>
                        <p class="text-muted">Chấm công, theo dõi và báo cáo tình hình đi làm của nhân viên</p>
                    </div>
                </div>

                <!-- Quick Stats Today -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card bg-success text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3 class="mb-0 font-weight-bold">${daChamCong}</h3>
                                        <p class="mb-0">Đã Chấm Công</p>
                                    </div>
                                    <i class="fas fa-check-circle fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card bg-warning text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3 class="mb-0 font-weight-bold">${diMuon}</h3>
                                        <p class="mb-0">Đi Muộn</p>
                                    </div>
                                    <i class="fas fa-exclamation-triangle fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card bg-danger text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3 class="mb-0 font-weight-bold">${vangHomNay}</h3>
                                        <p class="mb-0">Vắng Mặt</p>
                                    </div>
                                    <i class="fas fa-times-circle fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card bg-info text-white">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3 class="mb-0 font-weight-bold">${tyLe}</h3>
                                        <p class="mb-0">Tỷ Lệ Đi Làm</p>
                                    </div>
                                    <i class="fas fa-chart-pie fa-3x opacity-50"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action Bar & Filters -->
                <div class="card mb-4">
                    <div class="card-body">
                        <form action="checkin" method="GET">
                            <input type="hidden" name="action" value="search">
                            <div class="row g-3 align-items-end"> 
                                <div class="col-md-4">
                                    <label class="form-label fw-bold">
                                        <i class="fas fa-calendar"></i> Chọn Ngày
                                    </label>
                                    <input type="date" id="date" class="form-control" name="date"
                                           value="${dateValue != null ? dateValue : today}" />
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label fw-bold">
                                        <i class="fas fa-building"></i> Phòng Ban
                                    </label>
                                    <select class="form-control" name="phongban">
                                        <option value="">Tất cả phòng ban</option>
                                        <c:forEach var="pb" items="${dsPhongBan}">
                                            <option value="${pb.maPB}" ${pb.maPB eq selectedPB ? 'selected' : ''}>${pb.tenPB}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label fw-bold">
                                        <i class="fas fa-search"></i> Tìm Tên NV
                                    </label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" name="tenNV" value="${paramTenNV}" placeholder="Nhập tên nhân viên...">
                                        <button class="btn btn-primary" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>

                        <div class="row mt-3">
                            <div class="col-12">
                                <button class="btn btn-success ml-2" data-toggle="modal" data-target="#reportModal">
                                    <i class="fas fa-chart-bar"></i> Xem Báo Cáo
                                </button>
                                <button class="btn btn-info ml-2" id="btnXuatExcelCC">
                                    <i class="fas fa-file-export"></i> Xuất Excel
                                </button>
                                <button class="btn btn-secondary ml-2" onclick="window.print()">
                                    <i class="fas fa-print"></i> In Bảng Chấm Công
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Attendance Table -->
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-table"></i> Bảng Chấm Công - Ngày ${dateValue}</h5>
                    </div>
                    <!-- Toolbar Chấm Công -->
                    <div id="chamCongToolbar" class="d-none bg-light p-2 mb-2 border rounded">
                        <span id="selectedCount">0</span> nhân viên được chọn
                        <button id="btnChamCong" class="btn btn-primary btn-sm ml-2">
                            <i class="fas fa-calendar-check"></i> Chấm Công
                        </button>
                        <button id="btnCloseToolbar" class="btn btn-light btn-sm ml-2" style="padding: 0 8px; font-size: 18px; line-height: 1;">
                            &times;
                        </button>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover table-bordered mb-0" id="tableChamCong">
                                <thead class="thead-light">
                                    <tr>
                                        <th width="5%">
                                            <input type="checkbox" id="select-all">
                                        </th>
                                        <th>Mã NV</th>
                                        <th>Họ Tên</th>
                                        <th>Phòng Ban</th>
                                        <th>Giờ Vào</th>
                                        <th>Giờ Ra</th>
                                        <th>Trạng Thái</th>
                                        <th class="text-center">Số Ngày Công</th>
                                        <th class="text-center">Thao Tác</th>
                                    </tr>
                                </thead>
                                <tbody id="table-body">
                                    <c:forEach var="nv" items="${dsNhanVien}">
                                        <c:set var="cc" value="${chamCongMap[nv.maNV]}" />
                                        <c:set var="ngayCong" value="${soNgayCongMap[nv.maNV]}" />

                                        <tr>
                                            <td>
                                                <input type="checkbox" class="employee-checkbox" value="${nv.maNV}">
                                            </td>
                                            <td><strong>${nv.maNV}</strong></td>
                                            <td>${nv.hoTen}</td>

                                            <td>
                                                <span class="badge badge-primary">
                                                    ${nv.tenPB}
                                                </span>
                                            </td>

                                            <td>
                                                <span class="text-success">
                                                    <c:out value="${cc != null && cc.gioVao != null ? cc.gioVao : '--:--'}" />
                                                </span>
                                            </td>

                                            <td>
                                                <c:out value="${cc != null && cc.gioRa != null ? cc.gioRa : '--:--'}" />
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${cc != null && cc.trangThai eq 'present'}">
                                                        <span class="badge badge-success">
                                                            <i class="fas fa-check"></i> Đi làm
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-danger">
                                                            <i class="fas fa-times"></i> Vắng
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <td class="text-center">
                                                <c:set var="ngayCong" value="${soNgayCongMap[nv.maNV]}" />
                                                <strong class="text-success">
                                                    <c:out value="${ngayCong != null ? ngayCong : 0}" />/26
                                                </strong>
                                                <small class="d-block text-muted">
                                                    <fmt:formatNumber value="${(ngayCong != null ? ngayCong : 0) * 100.0 / 26}" maxFractionDigits="1" />%
                                                </small>
                                            </td>
                                            <!-- Nút thao tác -->
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-info btn-editChamCong"
                                                        data-toggle="modal" data-target="#editAttendanceModal"
                                                        data-id="${nv.maNV}">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-primary btn-view-detail"
                                                        data-toggle="modal" data-target="#detailAttendanceModal"
                                                        data-id="${nv.maNV}">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer">
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <span class="text-muted">
                                Hiển thị ${dsNhanVien.size()} / ${totalCount} nhân viên
                            </span>
                            <nav>
                                <ul class="pagination pagination-cc mb-0">
                                    <li class="page-item ${page == 1 ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="${redirectUrl}${page > 1 ? '&page=' : '?page='}${page - 1}">
                                            <i class="fas fa-chevron-left"></i>
                                        </a>
                                    </li>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == page ? 'active' : ''}">
                                            <a class="page-link"
                                               href="${redirectUrl}${redirectUrl.contains('?') ? '&' : '?'}page=${i}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:forEach>
                                    <li class="page-item ${page == totalPages ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="${redirectUrl}${redirectUrl.contains('?') ? '&' : '?'}page=${page + 1}">
                                            <i class="fas fa-chevron-right"></i>
                                        </a>
                                    </li>

                                </ul>
                            </nav>
                            <input type="hidden" id="currentPageChamCong" value="${page != null ? page : 1}">
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!-- Modal Xem Chi Tiết Chấm Công Tháng -->
        <div class="modal fade" id="detailAttendanceModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-xl" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title"><i class="fas fa-calendar-alt"></i> Chi Tiết Chấm Công</h5>
                        <button type="button" class="close text-white" data-dismiss="modal"><span>&times;</span></button>
                    </div>
                    <div class="modal-body">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-3"><strong><i class="fas fa-id-card"></i> Mã NV:</strong> <span class="nv-ma"></span></div>
                                    <div class="col-md-3"><strong><i class="fas fa-user"></i> Họ Tên:</strong> <span class="nv-ten"></span></div>
                                    <div class="col-md-3"><strong><i class="fas fa-building"></i> Phòng Ban:</strong> <span class="nv-pb"></span></div>
                                    <div class="col-md-3"><strong><i class="fas fa-calendar-check"></i> Số Ngày Công:</strong> <span class="badge badge-success nv-ngayCong"></span></div>
                                </div>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-3"><div class="card bg-success text-white"><div class="card-body p-3"><h4 class="mb-0 ngay-co-mat">0</h4><small>Ngày Có Mặt</small></div></div></div>
                            <div class="col-md-3"><div class="card bg-warning text-white"><div class="card-body p-3"><h4 class="mb-0 ngay-di-muon">0</h4><small>Ngày Đi Muộn</small></div></div></div>
                            <div class="col-md-3"><div class="card bg-danger text-white"><div class="card-body p-3"><h4 class="mb-0 ngay-vang">0</h4><small>Ngày Vắng</small></div></div></div>
                            <div class="col-md-3"><div class="card bg-info text-white"><div class="card-body p-3"><h4 class="mb-0 ngay-nghi-phep">0</h4><small>Ngày Nghỉ Phép</small></div></div></div>
                        </div>
                        <div class="table-responsive">
                            <table id="tableChiTietCC" class="table table-bordered table-hover table-sm mb-0">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Ngày</th>
                                        <th>Thứ</th>
                                        <th>Giờ Vào</th>
                                        <th>Giờ Ra</th>
                                        <th>Tổng Giờ</th>
                                        <th>Trạng Thái</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" data-dismiss="modal"><i class="fas fa-times"></i> Đóng</button>
                        <button class="btn btn-primary" id="btnExportExcelChiTietCC">
                            <i class="fas fa-file-excel"></i> Xuất Excel
                        </button>
                        <button class="btn btn-danger" id="btnExportPDFChiTietCC"><i class="fas fa-file-pdf"></i> Xuất PDF</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Sửa Chấm Công -->
        <div class="modal fade" id="editAttendanceModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-info text-white">
                        <h5 class="modal-title"><i class="fas fa-edit"></i> Sửa Thông Tin Chấm Công</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <form id="editAttendanceForm">
                        <input type="hidden" name="id" id="edit_id">
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="font-weight-bold">Nhân Viên</label>
                                <input type="text" class="form-control" id="edit_nhanvien" readonly>
                            </div>
                            <div class="form-group">
                                <label class="font-weight-bold">Ngày</label>
                                <input type="date" class="form-control" id="edit_ngay" name="ngay" required>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="font-weight-bold">Giờ Vào</label>
                                        <input type="time" class="form-control" id="edit_gio_vao" name="gio_vao">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="font-weight-bold">Giờ Ra</label>
                                        <input type="time" class="form-control" id="edit_gio_ra" name="gio_ra">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="font-weight-bold">Trạng Thái</label>
                                <select class="form-control" id="edit_trangthai" name="trangthai" required>
                                    <option value="present">Có mặt</option>
                                    <option value="late">Đi muộn</option>
                                    <option value="">Vắng mặt</option>
                                    <option value="leave">Nghỉ phép</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                <i class="fas fa-times"></i> Hủy
                            </button>
                            <button type="submit" class="btn btn-info">
                                <i class="fas fa-save"></i> Cập Nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal Báo Cáo -->
        <div class="modal fade" id="reportModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-xl" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title"><i class="fas fa-chart-bar"></i> Báo Cáo Chấm Công</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <!-- Report Filters -->
                        <div class="row mb-4">
                            <div class="col-md-4">
                                <label class="font-weight-bold">Từ Ngày</label>
                                <input type="date" class="form-control" value="2025-01-01">
                            </div>
                            <div class="col-md-4">
                                <label class="font-weight-bold">Đến Ngày</label>
                                <input type="date" class="form-control" value="2025-01-15">
                            </div>
                            <div class="col-md-4">
                                <label class="font-weight-bold">&nbsp;</label>
                                <button class="btn btn-primary btn-block">
                                    <i class="fas fa-search"></i> Xem Báo Cáo
                                </button>
                            </div>
                        </div>
                        <!-- Department Stats -->
                        <h6 class="font-weight-bold mb-3"><i class="fas fa-building"></i> Thống Kê Theo Phòng Ban</h6>
                        <div class="table-responsive mb-4">
                            <table class="table table-bordered table-hover">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Phòng Ban</th>
                                        <th class="text-center">Tổng NV</th>
                                        <th class="text-center">Đi Làm Đều</th>
                                        <th class="text-center">Đi Muộn</th>
                                        <th class="text-center">Vắng Không Phép</th>
                                        <th class="text-center">Tỷ Lệ (%)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><span class="badge badge-primary">IT</span></td>
                                        <td class="text-center">15</td>
                                        <td class="text-center"><span class="badge badge-success">13</span></td>
                                        <td class="text-center"><span class="badge badge-warning">2</span></td>
                                        <td class="text-center"><span class="badge badge-danger">0</span></td>
                                        <td class="text-center"><strong class="text-success">86.7%</strong></td>
                                    </tr>
                                    <tr>
                                        <td><span class="badge badge-success">Marketing</span></td>
                                        <td class="text-center">12</td>
                                        <td class="text-center"><span class="badge badge-success">10</span></td>
                                        <td class="text-center"><span class="badge badge-warning">1</span></td>
                                        <td class="text-center"><span class="badge badge-danger">1</span></td>
                                        <td class="text-center"><strong class="text-success">83.3%</strong></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="layout/footer.jsp" %>
    </body>
    <%@ include file="layout/js.jsp" %>
</html>
