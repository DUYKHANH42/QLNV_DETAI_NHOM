<%-- 
    Document   : login
    Created on : Oct 14, 2025, 2:28:09 PM
    Author     : PC
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/login.css">
    </head>
    <body>
        <h1 class="auth-title"><img src="<%= request.getContextPath()%>/img/logo.png" alt="Logo"></h1>
       <form class="auth-form" id="loginForm" action="<%= request.getContextPath() %>/login" method="post">
            <div class="form-group" id="employee-id-group" style="display:none;">
                <label for="employee-id">Mã nhân viên</label>
                <input type="text" id="employee-id" name="employee-id" placeholder="Nhập mã nhân viên...">
            </div>
            <c:if test="${not empty errorMessage}">
                <div style="color: red; margin-bottom: 15px; text-align: center;">
                    ${errorMessage}
                </div>
            </c:if>
            <div class="form-group" id="username-group" >
                <label for="username">Tên tài khoản</label>
                <input type="text" id="username" name="username" placeholder="Nhập tên tài khoản...">
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Nhập mật khẩu..." required>
            </div>
            <div class="form-check">
                <label class="form-check-label" for="rememberMe">RememberMe</label>
                <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                <c:if test="${not empty enteredUsername}"> checked </c:if>
            </div>
            <button type="submit" class="auth-btn">Đăng nhập</button>
<!--
            <div class="auth-switch">
                Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a>
            </div>-->
        </form>
    </body>
</html>
