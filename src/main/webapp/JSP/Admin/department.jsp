<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                        <h2 class="mb-1"><i class="fas fa-building"></i> Quản Lý Phòng Ban</h2>
                        <p class="text-muted">Quản lý thông tin các phòng ban và nhân sự</p>
                    </div>
                </div>

                <!-- Actions Bar -->
                <div class="row mb-4">
                    <div class="col-12">
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addDepartmentModal">
                            <i class="fas fa-plus-circle"></i> Thêm Phòng Ban
                        </button>
                        <button id="btnExportPhongBan" class="btn btn-success ml-2">
                            <i class="fas fa-file-export"></i> Xuất Excel
                        </button>
                    </div>
                </div>

                <!-- Departments Grid -->
                <div class="row" id="departmentsGrid">
                    <!-- Department Card 1 -->
                    <c:set var="colorClasses" value="bg-primary,bg-success,bg-warning,bg-danger,bg-info,bg-secondary" />

                    <c:forEach var="pb" items="${dsPhongBan}" varStatus="status">
                        <c:set var="colorArray" value="${fn:split(colorClasses, ',')}" />
                        <c:set var="cardColor" value="${colorArray[status.index % fn:length(colorArray)]}" />

                        <div class="col-xl-3 col-lg-4 col-md-6 mb-4">
                            <div class="card department-card h-100">
                                <div class="card-header ${cardColor} text-white d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">
                                        <i class="fas fa-building"></i> ${pb.tenPB}
                                    </h5>
                                    <div class="dropdown">
                                        <button class="btn btn-sm btn-light dropdown-toggle" type="button" data-toggle="dropdown">
                                            <i class="fas fa-ellipsis-v"></i>
                                        </button>
                                        <div class="dropdown-menu dropdown-menu-right">
                                            <a class="dropdown-item btn-edit-department" href="#" data-mapb="${pb.maPB}">
                                                <i class="fas fa-edit"></i> Sửa
                                            </a>
                                            <a href="#" class="dropdown-item text-danger btn-delete-department" data-mapb="${pb.maPB}">
                                                <i class="fas fa-trash"></i> Xóa
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <div class="department-info mb-3">
                                        <p class="mb-2"><strong>Mã phòng ban:</strong> ${pb.maPB}</p>
                                        <p class="mb-2"><strong>Trưởng phòng:</strong> 
                                            <c:choose>
                                                <c:when test="${pb.truongPhong != null}">
                                                    ${nhanVienMap[pb.truongPhong.toString()]}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa có</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                        <p class="mb-2"><strong>Số nhân viên:</strong> ${nhanVienTheoPhongMap[pb.maPB]}</p>
                                    </div>
                                    <button class="btn btn-outline-primary btn-block btn-detail-department" data-mapb="${pb.maPB}">
                                        <i class="fas fa-eye"></i> Xem Chi Tiết
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>                 
                </div>
                <!-- Summary Statistics -->
                <div class="row mt-4">
                    <!-- Tổng Phòng Ban -->
                    <div class="col-md-3">
                        <div class="card bg-primary text-white">
                            <div class="card-body text-center">
                                <h3 class="mb-1">${tongPhongBan}</h3>
                                <p class="mb-0">Tổng Phòng Ban</p>
                            </div>
                        </div>
                    </div>

                    <!-- Tổng Nhân Viên -->
                    <div class="col-md-3">
                        <div class="card bg-success text-white">
                            <div class="card-body text-center">
                                <h3 class="mb-1">${tongNhanVien}</h3>
                                <p class="mb-0">Tổng Nhân Viên</p>
                            </div>
                        </div>
                    </div>

                    <!-- TB Nhân Viên/PB -->
                    <div class="col-md-3">
                        <div class="card bg-warning text-white">
                            <div class="card-body text-center">
                                <h3 class="mb-1">${tbNhanVienPB}</h3>
                                <p class="mb-0">TB Nhân Viên/PB</p>
                            </div>
                        </div>
                    </div>

                    <!-- Phòng Ban Đông Nhất -->
                    <div class="col-md-3">
                        <div class="card bg-info text-white">
                            <div class="card bg-info text-white">
                                <div class="card-body text-center">
                                    <h3 class="mb-1">${phongDongNhat}</h3>
                                    <p class="mb-0">Phòng Ban</p>
                                    <p class="mb-0">Đông Nhất</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div> 
        </main>
        <!-- Modal Thêm Phòng Ban -->
        <div class="modal fade" id="addDepartmentModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title"><i class="fas fa-plus-circle"></i> Thêm Phòng Ban Mới</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <form action="them-phong-ban.jsp" method="post">
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Tên Phòng Ban <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" name="tenpb" required placeholder="VD: IT">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Trưởng Phòng <span class="text-danger">*</span></label>
                                <select class="form-control" name="truongphong" required>
                                    <option value="">-- Chọn trưởng phòng --</option>
                                    <c:forEach var="nv" items="${dsNhanVien}">
                                        <option value="${nv.maNV}"
                                                ${nv.maNV == searchNhanVien ? 'selected' : ''}>
                                            ${nv.hoTen}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Màu Hiển Thị</label>
                                <select class="form-control" name="color">
                                    <option value="primary">Xanh Dương</option>
                                    <option value="success">Xanh Lá</option>
                                    <option value="warning">Vàng</option>
                                    <option value="info">Xanh Nhạt</option>
                                    <option value="danger">Đỏ</option>
                                    <option value="secondary">Xám</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Mô Tả</label>
                                <textarea class="form-control" name="mota" rows="3" placeholder="Nhập mô tả về phòng ban..."></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                <i class="fas fa-times"></i> Hủy
                            </button>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Lưu
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal Sửa Phòng Ban -->
        <div class="modal fade" id="editDepartmentModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-warning text-white">
                        <h5 class="modal-title"><i class="fas fa-edit"></i> Chỉnh Sửa Phòng Ban</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <form id="formEditDepartment">
                        <input type="hidden" name="mapb"> 
                        <div class="modal-body">
                            <div class="form-group">
                                <label>Tên Phòng Ban <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="tenpb" required>
                            </div>
                            <div class="form-group">
                                <label>Trưởng Phòng <span class="text-danger">*</span></label>
                                <select class="form-control" name="truongphong" required>
                                    <option value="">-- Chọn trưởng phòng --</option>

                                </select>
                            </div>
                            <div class="form-group">
                                <label>Mô Tả</label>
                                <textarea class="form-control" name="mota" rows="3" placeholder="Nhập mô tả về phòng ban..."></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                <i class="fas fa-times"></i> Hủy
                            </button>
                            <button type="submit" class="btn btn-warning">
                                <i class="fas fa-save"></i> Cập Nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal Chi Tiết Phòng Ban -->
        <div class="modal fade" id="departmentDetailModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-xl" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-info text-white">
                        <h5 class="modal-title"><i class="fas fa-building"></i> Chi Tiết Phòng Ban</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <!-- Department Info -->
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-body">
                                        <h6 class="text-muted mb-1">Mã Phòng Ban</h6>
                                        <h4 class="mb-0" id="detail-maPB"></h4>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-body">
                                        <h6 class="text-muted mb-1">Trưởng Phòng</h6>
                                        <h4 class="mb-0" id="detail-truongPhong"></h4>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-body">
                                        <h6 class="text-muted mb-1">Tổng Nhân Viên</h6>
                                        <h4 class="mb-0 text-primary" id="detail-tongNV"></h4>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-body">
                                        <h6 class="text-muted mb-1">Đang Làm Việc</h6>
                                        <h4 class="mb-0 text-success" id="detail-dangLam"></h4>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Employee List -->
                        <h6 class="mb-3"><i class="fas fa-users"></i> Danh Sách Nhân Viên</h6>
                        <div class="table-responsive">
                            <table class="table table-hover table-bordered">
                                <thead class="thead-light">
                                    <tr>
                                        <th>STT</th>
                                        <th>Mã NV</th>
                                        <th>Họ Tên</th>
                                        <th>Chức Vụ</th>
                                        <th>Email</th>
                                        <th>Số Điện Thoại</th>
                                        <th>Trạng Thái</th>
                                    </tr>
                                </thead>
                                <tbody id="detail-nhanVien">
                                    <!-- Dữ liệu sẽ được JS render -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <i class="fas fa-times"></i> Đóng
                        </button>
                        <button type="button" id="btnExportPDF" class="btn btn-primary">
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
