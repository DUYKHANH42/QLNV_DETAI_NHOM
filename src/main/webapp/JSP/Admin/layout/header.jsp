<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.TaiKhoan" %>
<%@ page import="DAO.NhanVienDAO" %>
<%@ page import="Model.NhanVien" %>
<%
    TaiKhoan user = (TaiKhoan) session.getAttribute("account");
    String hoTen = "";
    if (user != null) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        NhanVien nv = nvDAO.getById(user.getMaNV());
        if (nv != null) hoTen = nv.getHoTen();
    }
%>

<div class="header fixed-top">
    <nav class="navbar navbar-expand-md container-fluid navbar-dark">
        <a class="navbar-brand text-center" href="#">
            <p class="font-weight-bold mb-0"><i class="fa fa-book"></i> Quản lý Nhân Sự</p>
            <p class="small">EDUCATION</p>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarInstruction">
            <i class="fa fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" id="navbarInstruction">
            <div class="ml-auto d-flex align-items-center">
                <% if (user != null) { %>
                    <div class="user-info d-flex align-items-center mr-3">
                        <i class="fas fa-user-circle fa-lg mr-2"></i>
                        <span><%= hoTen %></span>
                    </div>
                    <!-- Nút đăng xuất -->
                    <form action="<%= request.getContextPath() %>/Logout" method="post" style="display:inline;">
                        <button type="submit" class="btn btn-danger btn-sm">Đăng Xuất</button>
                    </form>
                <% } else { %>
                    <!-- Nút đăng nhập -->
                    <button class="btn-login">
                        <a href="<%= request.getContextPath() %>/Login">
                            <i class="fas fa-user"></i> Đăng Nhập
                        </a>
                    </button>
                <% } %>
            </div>
        </div>
    </nav>
</div>
