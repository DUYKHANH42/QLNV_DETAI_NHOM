<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Quản lý nhân sự</title>
        <%@ include file="layout/css.jsp" %>
    </head>
    <body>

        <%@ include file="layout/header.jsp" %>
        <%@ include file="layout/sidebar.jsp" %>

        <main class="main-content">
            <div class="container-fluid">
                <!-- Page Header -->
                <div class="row mb-4">
                    <div class="col-12">
                        <h2 class="mb-1"><i class="fas fa-users"></i> Quản Lý Nhân Viên</h2>
                        <p class="text-muted">Thêm, sửa, xóa và tìm kiếm thông tin nhân viên</p>
                    </div>
                </div>

                <!-- Search & Filter Section -->
                <div class="card mb-4">
                    <div class="card-body">
                        <form class="row g-3 align-items-end" action="emloyment" method="post">
                            <div class="col-md-3">
                                <input type="hidden" name="action" value="search">
                                <label for="hoten" class="form-label">
                                    <i class="fas fa-search"></i> Tìm theo tên
                                </label>
                                <input type="text" class="form-control" id="hoten" name="hoten" value="${param.hoten}" placeholder="Nhập tên nhân viên...">
                            </div>
                            <div class="col-md-3">
                                <label for="filter-phongban" class="form-label">
                                    <i class="fas fa-building"></i> Phòng ban
                                </label>
                                <select class="form-control" id="filter-phongban" name="phongban">
                                    <option value="">Tất cả phòng ban</option>
                                    <c:forEach var="pb" items="${dsPhongBan}">
                                        <option value="${pb.maPB}"
                                                ${pb.maPB == searchPhongBan ? 'selected' : ''}>
                                            ${pb.tenPB}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <button type="submit" class="btn btn-primary w-100">
                                    <i class="bi bi-search"></i> Tra cứu
                                </button>
                            </div>
                            <div class="col-md-3">
                                <button type="button" class="btn btn-success w-100" data-toggle="modal" data-target="#modalAddEmployee">
                                    <i class="fas fa-plus-circle"></i> Thêm Nhân Viên
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Employees Table -->
                <div class="card">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="fas fa-list"></i> Danh Sách Nhân Viên</h5>
                        <button id="btnExportExcel" class="btn btn-sm btn-secondary">
                            <i class="fas fa-file-export"></i> Xuất Excel
                        </button>
                    </div>

                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table id="tableNhanVien" class="table table-hover table-striped mb-0">
                                <thead class="thead-primary">
                                    <tr>
                                        <th>Mã NV</th>
                                        <th>Họ Tên</th>
                                        <th>Phòng Ban</th>
                                        <th>Chức Vụ</th>
                                        <th>Email</th>
                                        <th>Số Điện Thoại</th>
                                        <th>Trạng Thái</th>
                                        <th class="text-center">Thao Tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="nv" items="${dsNhanVien}">
                                        <tr>
                                            <td><strong>${nv.maNV}</strong></td>
                                            <td>${nv.hoTen}</td>
                                            <td>${nv.tenPB}</td>
                                            <td>${nv.chucVu}</td>
                                            <td>${nv.email}</td>
                                            <td>${nv.SDT}</td>
                                            <td>
                                                <span class="badge ${nv.trangThai == 'Đang làm' ? 'badge-success' : 'badge-danger'}">
                                                    ${nv.trangThai}
                                                </span>
                                            </td>
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-info btn-edit" data-id="${nv.maNV}" data-toggle="modal" data-target="#editEmployeeModal">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-danger" onclick="deleteNhanVien(${nv.maNV})">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <!-- Pagination -->
                            <div class="card-footer bg-white">
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center mb-0">
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <c:set var="cp" value="${currentPage != null ? currentPage : 1}" />
                                            <a class="page-link ajax-link" href="emloyment?page=${cp - 1}">Previous</a>
                                        </li>

                                        <c:forEach var="p" begin="1" end="${totalPages}">
                                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                                <a class="page-link ajax-link" href="emloyment?page=${p}">${p}</a>
                                            </li>
                                        </c:forEach>

                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link ajax-link" href="emloyment?page=${currentPage + 1}">Next</a>
                                        </li>
                                    </ul>
                                </nav>
                                <input type="hidden" id="currentPage" value="${currentPage}">
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <!-- Modal Thêm Nhân Viên -->
            <div class="modal fade" id="modalAddEmployee" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-lg">
                    <div class="modal-content">
                        <form id="formAddEmployee">
                            <input type="hidden" name="action" value="add" />
                            <div class="modal-header bg-primary text-white">
                                <h5 class="modal-title"><i class="bi bi-person-plus me-2"></i> Thêm Nhân Viên</h5>
                                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Họ Tên</label>
                                        <input type="text" class="form-control" name="hoten" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Phòng Ban</label>
                                        <select id="add-phongban" class="form-control" name="phongban" required>
                                            <c:forEach var="pb" items="${dsPhongBan}">
                                                <option value="${pb.maPB}">${pb.tenPB}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Chức Vụ</label>
                                        <input type="text" class="form-control" name="chucvu" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Email</label>
                                        <input type="email" class="form-control" name="email" required>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Số Điện Thoại</label>
                                        <input type="text" class="form-control" name="sdt">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Tên Đăng Nhập</label>
                                        <input type="text" class="form-control" name="tendangnhap" required>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Mật Khẩu</label>
                                        <input type="password" class="form-control" name="password" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Vai Trò</label>
                                        <select class="form-control" name="vaitro" required>
                                            <option value="Admin">Admin</option>
                                            <option value="NhanVien">Nhân Viên</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-primary">Lưu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- Modal Sửa Nhân Viên -->
            <div class="modal fade" id="editEmployeeModal" tabindex="-1" role="dialog" aria-labelledby="editEmployeeModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header bg-info text-white">
                            <h5 class="modal-title" id="editEmployeeModalLabel">
                                <i class="fas fa-user-edit"></i> Chỉnh Sửa Nhân Viên
                            </h5>
                            <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>

                        <form id="FormEditEmployment">
                            <input type="hidden" name="id" value="1">

                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="edit-manv">Mã Nhân Viên <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" id="edit-manv" name="maNV" required value="NV001" readonly>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="edit-hoten">Họ Tên <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" id="edit-hoten" name="hoTen" required value="Nguyễn Văn An">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="edit-phongban">Phòng Ban <span class="text-danger">*</span></label>
                                            <select class="form-control" id="edit-phongban" name="phongban" required>
                                                <c:forEach var="pb" items="${dsPhongBan}">
                                                    <option value="${pb.maPB}">${pb.tenPB}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="edit-chucvu">Chức Vụ <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" id="edit-chucvu" name="chucVu" required value="Developer">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="edit-email">Email <span class="text-danger">*</span></label>
                                            <input type="email" class="form-control" id="edit-email" name="email" required value="an.nguyen@company.com">
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="edit-sdt">Số Điện Thoại</label>
                                            <input type="tel" class="form-control" id="edit-sdt" name="sdt" value="0901234567">
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="edit-trangthai">Trạng Thái</label>
                                    <select class="form-control" id="edit-trangthai" name="trangThai">
                                        <option value="Đang làm" selected>Đang Làm</option>
                                        <option value="Nghỉ việc">Đã nghỉ việc</option>
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
        </main>
        <%@ include file="layout/footer.jsp" %>
    </body>
    <%@ include file="layout/js.jsp" %>
</html>
