/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.TaiKhoanDAO;
import Model.TaiKhoan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author PC
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        request.getRequestDispatcher("../JSP/login.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("account") != null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
    javax.servlet.http.Cookie[] cookies = request.getCookies();
    String username = null, token = null;
    if (cookies != null) {
        for (javax.servlet.http.Cookie c : cookies) {
            if ("username".equals(c.getName())) username = c.getValue();
            if ("rememberToken".equals(c.getName())) token = c.getValue();
        }
    }

    if (username != null && token != null) {
        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
        TaiKhoan account = taiKhoanDAO.getByRememberToken(username, token);
        if (account != null) {
            // Tạo session tự động
            session = request.getSession();
            session.setAttribute("account", account);
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
    }

    request.getRequestDispatcher("JSP/login.jsp").forward(request, response);
}


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String remember = request.getParameter("rememberMe");
            TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
            TaiKhoan account = taiKhoanDAO.DangNhap(username, password);
//            System.out.println(account);

            if (account != null) {
                HttpSession session = request.getSession(); // Tạo hoặc lấy session hiện có
                session.setAttribute("account", account); // Lưu đối tượng TaiKhoan vào session
                session.setMaxInactiveInterval(30 * 60); // Thời gian sống của session
                if ("on".equals(remember)) {
                    String token = java.util.UUID.randomUUID().toString();
                    taiKhoanDAO.saveRememberToken(account.getMaNV(), token); // Lưu token vào DB

                    javax.servlet.http.Cookie userCookie = new javax.servlet.http.Cookie("username", account.getTenDangNhap());
                    javax.servlet.http.Cookie tokenCookie = new javax.servlet.http.Cookie("rememberToken", token);

                    userCookie.setMaxAge(7 * 24 * 60 * 60); 
                    tokenCookie.setMaxAge(7 * 24 * 60 * 60);

                    userCookie.setPath(request.getContextPath());
                    tokenCookie.setPath(request.getContextPath());

                    response.addCookie(userCookie);
                    response.addCookie(tokenCookie);
                } else {
                    javax.servlet.http.Cookie userCookie = new javax.servlet.http.Cookie("username", "");
                    javax.servlet.http.Cookie tokenCookie = new javax.servlet.http.Cookie("rememberToken", "");
                    userCookie.setMaxAge(0);
                    tokenCookie.setMaxAge(0);
                    userCookie.setPath(request.getContextPath());
                    tokenCookie.setPath(request.getContextPath());
                    response.addCookie(userCookie);
                    response.addCookie(tokenCookie);
                }

                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
                request.setAttribute("enteredUsername", username);
                request.getRequestDispatcher("JSP/login.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace(); 
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
