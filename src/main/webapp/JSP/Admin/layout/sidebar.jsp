<%-- 
    Document   : header
    Created on : Oct 21, 2025, 1:07:52 PM
    Author     : PC
--%>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>

<div class="sidebar-container">
    <ul class="sidebar-menu">
        <li>
            <a href="home" class="active ajax-link">
                <i class="fas fa-home"></i>
                <span>Dashboard</span>
            </a>
        </li>
        <li>
            <a href="emloyment" class="ajax-link">
                <i class="fas fa-users"></i>
                <span>Nhân Viên</span>
            </a>
        </li>
        <li>
            <a href="department" class="ajax-link">
                <i class="fas fa-user-tie"></i>
                <span>Phòng Ban</span>
            </a>
        </li>
        <li>
            <a href="checkin" class="ajax-link">
                <i class="fas fa-calendar-alt"></i>
                <span>Chấm Công</span>
            </a>
        </li>
        <li>
            <a href="salary" class="ajax-link">
                <i class="fas fa-money-bill-wave"></i>
                <span>Lương</span>
            </a>
        </li>
        <li>
            <a href="<%= request.getContextPath() %>/admin/Reports.jsp">
                <i class="fas fa-chart-line"></i>
                <span>Báo Cáo</span>
            </a>
        </li>
        <li>
            <a href="<%= request.getContextPath() %>/admin/task.jsp">
                <i class="fas fa-user-tie"></i>
                <span>Công Việc</span>
            </a>
        </li>
    </ul>
</div>
