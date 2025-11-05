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
            <div class="dashboard-header">
                <h1>Dashboard Quản Lý Nhân Sự</h1>
                <p>Chào mừng bạn quay trở lại! Đây là tổng quan hệ thống.</p>
            </div>

            <!-- Stats Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-card-icon blue">
                        <i class="fas fa-users"></i>
                    </div>
                    <h3>${tongQuan.tongNV}</h3>
                    <p>Nhân Viên</p>

                </div>
                <div class="stat-card">
                    <div class="stat-card-icon green">
                        <i class="fas fa-user-check"></i>
                    </div>
                    <h3>${tongQuan.tongNV}</h3>
                    <p>Đang Làm Việc</p>
                </div>
                <div class="stat-card">
                    <div class="stat-card-icon orange">
                        <i class="fas fa-building"></i>
                    </div>
                    <h3>${tongQuan.soPhongBan}</h3>
                    <p>Phòng Ban</p>
                </div>
                <div class="stat-card">
                    <div class="stat-card-icon purple">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <h3><fmt:formatNumber value="${tongQuan.tyLeChuyenCan}" type="number" maxFractionDigits="0"/>%</h3>
                    <p>Tỷ Lệ Chuyên Cần</p>
                </div>
            </div>

            <!-- Charts Section -->
            <div class="charts-section">
                <div class="chart-card">
                    <h4><i class="fas fa-chart-bar"></i> Thống Kê Nhân Sự Theo Phòng Ban</h4>
                    <canvas id="departmentChart" height="80"></canvas>
                </div>
                <div class="chart-card">
                    <h4><i class="fas fa-pie-chart"></i> Tỷ Lệ Giới Tính</h4>
                    <canvas id="genderChart"></canvas>
                </div>
            </div>

            <!-- Recent Employees Table -->
            <div class="table-card">
                <h4><i class="fas fa-table"></i> Nhân Viên Mới Nhất</h4>
                <table class="custom-table table">
                    <thead>
                        <tr>
                            <th>Mã NV</th>
                            <th>Họ Tên</th>
                            <th>Phòng Ban</th>
                            <th>Chức Vụ</th>
                            <th>Email</th>
                            <th>Trạng Thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="nv" items="${nvMoiNhat}">
                            <tr>
                                <td>${nv.maNV}</td>
                                <td>${nv.hoTen}</td>
                                <td>${nv.phongBan}</td>
                                <td>${nv.chucVu}</td>
                                <td>${nv.email}</td>
                                <td><span class="badge-status badge-active">${nv.trangThai}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>
        <%@ include file="layout/footer.jsp" %>
    </body>
    <%@ include file="layout/js.jsp" %>
</html>
