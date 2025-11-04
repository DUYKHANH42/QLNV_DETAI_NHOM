/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.NhanVienDAO;
import DAO.PhongBanDAO;
import DAO.TaiKhoanDAO;
import Model.NhanVien;
import Model.PhongBan;
import Model.TaiKhoan;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author PC
 */
@WebServlet(name = "EmloymentServlet", urlPatterns = {"/emloyment"})
public class EmloymentServlet extends HttpServlet {

    NhanVienDAO nvdao;
    PhongBanDAO pbdao;
    TaiKhoanDAO tkdao;

    @Override
    public void init() throws ServletException {
        nvdao = new NhanVienDAO();
        pbdao = new PhongBanDAO();
        tkdao = new TaiKhoanDAO();
    }

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "list":
                hienThiDanhSach(request, response);
                break;
            case "add":
                System.out.println("HAHA");
                xuLiThem(request, response);
                break;
            case "search":
                timKiem(request, response);
                break;
            case "delete":
                xuliXoa(request, response);
                break;
            case "update":
                xuliUpdate(request, response);
                break;
            case "getById":
                GetById(request, response);
                break;
            default:
//                hienThiDanhSach(request, response);
        }

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
//        HttpSession session = request.getSession(false);
//        TaiKhoan user = (session != null) ? (TaiKhoan) session.getAttribute("account") : null;
//        if (user == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
        hienThiDanhSach(request, response);
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
        processRequest(request, response);
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

    private void hienThiDanhSach(HttpServletRequest request, HttpServletResponse response) {
        try {
            int page = 1;
            int pagesize = 5;
            String currentPage = request.getParameter("page");
            if (currentPage != null && !currentPage.isEmpty()) {
                page = Integer.parseInt(currentPage);
            }
//            System.out.println("Giá trị của page: "+page);
            List<NhanVien> dsNhanVien = nvdao.getByPage(page, pagesize);
            request.setAttribute("dsNhanVien", dsNhanVien);
            int totalPages = (int) Math.ceil((double) nvdao.getAll().size() / pagesize);
            request.setAttribute("page", page);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("redirectUrl", "emloyment?action=list");
            List<PhongBan> dsPhongBan = pbdao.getAllPhongBan();
            request.setAttribute("dsPhongBan", dsPhongBan);
//            System.out.println(dsNhanVien);
            request.getRequestDispatcher("JSP/Admin/emloyment.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(EmloymentServlet.class.getName()).log(Level.SEVERE, "Lỗi khi hiển thị danh sách nhân viên", ex);
        }

    }

    private void timKiem(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            String hoten = request.getParameter("hoten");
            String phongban = request.getParameter("phongban");

            List<NhanVien> list = nvdao.searchAdvanced(hoten, phongban);

//        System.out.println("Đây là danh sách"+list);
            List<PhongBan> dsPhongBan = pbdao.getAllPhongBan();
            request.setAttribute("dsPhongBan", dsPhongBan);
            request.setAttribute("searchHoten", hoten);
            request.setAttribute("searchPhongBan", phongban);
            request.setAttribute("dsNhanVien", list);
            request.getRequestDispatcher("JSP/Admin/emloyment.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(EmloymentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void xuLiThem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<>();

        try {
            String tenDangNhap = request.getParameter("tendangnhap");
            String password = request.getParameter("password");
            String vaiTro = request.getParameter("vaitro");
            String hoTen = request.getParameter("hoten");
            String maPB = request.getParameter("phongban");
            String chucVu = request.getParameter("chucvu");
            String email = request.getParameter("email");
            String sdt = request.getParameter("sdt");

            if (tenDangNhap == null || password == null || hoTen == null) {
                map.put("status", "error");
                map.put("message", "Vui lòng nhập đầy đủ thông tin!");
                response.getWriter().write(new Gson().toJson(map));
                return;
            }

            // 1️⃣ Tạo nhân viên
            NhanVien nv = new NhanVien();
            nv.setHoTen(hoTen);
            nv.setMaPB(maPB);
            nv.setChucVu(chucVu);
            nv.setEmail(email);
            nv.setSDT(sdt);
            int maNV = nvdao.insert(nv);

            if (maNV == -1) {
                throw new Exception("Không thể thêm nhân viên!");
            }

            // 2️⃣ Tạo tài khoản
            TaiKhoan tk = new TaiKhoan();
            tk.setMaNV(maNV);
            tk.setTenDangNhap(tenDangNhap);
            tk.setMatKhau(password);
            tk.setVaiTro(vaiTro);
            tkdao.insert(tk);

            // 3️⃣ Lấy thông tin nhân viên đầy đủ
            NhanVien nvMoi = nvdao.getById(maNV);
            PhongBan pb = pbdao.getByID(nvMoi.getMaPB());

            Map<String, Object> nvJson = new HashMap<>();
            nvJson.put("maNV", nvMoi.getMaNV());
            nvJson.put("hoTen", nvMoi.getHoTen());
            nvJson.put("maPB", nvMoi.getMaPB());
            nvJson.put("tenPB", pb != null ? pb.getTenPB(): "");
            nvJson.put("chucVu", nvMoi.getChucVu());
            nvJson.put("email", nvMoi.getEmail());
            nvJson.put("sdt", nvMoi.getSDT());
            nvJson.put("trangThai", nvMoi.getTrangThai());

            map.put("status", "success");
            map.put("message", "Thêm nhân viên thành công!");
            map.put("nhanVien", nvJson);

        } catch (Exception ex) {
            ex.printStackTrace();
            map.put("status", "error");
            map.put("message", "Lỗi: " + ex.getMessage());
        }

        response.getWriter().write(new Gson().toJson(map));
    }

   private void xuliXoa(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    Map<String, Object> map = new HashMap<>();

    try {
        String maNV = request.getParameter("id");
        boolean success = nvdao.delete(maNV);

        if (success) {
            tkdao.delete(maNV);
            map.put("status", "success");
            map.put("message", "Xóa nhân viên thành công!");
        } else {
            map.put("status", "error");
            map.put("message", "Xóa nhân viên thất bại!");
        }

    } catch (SQLException ex) {
        if ("foreign_key_violation".equals(ex.getMessage())) {
            map.put("status", "error");
            map.put("message", "Không thể xóa nhân viên vì đang được tham chiếu ở bảng khác!");
        } else {
            ex.printStackTrace();
            map.put("status", "error");
            map.put("message", "Lỗi SQL: " + ex.getMessage());
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        map.put("status", "error");
        map.put("message", "Lỗi khi xóa: " + ex.getMessage());
    }

    response.getWriter().write(new Gson().toJson(map));
}


    private void GetById(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            NhanVien nv = nvdao.getById(id);
            if (nv != null) {
                String json = String.format(
                        "{\"status\":\"success\",\"data\":{"
                        + "\"maNV\":\"%s\",\"hoTen\":\"%s\",\"maPB\":\"%s\",\"chucVu\":\"%s\","
                        + "\"email\":\"%s\",\"sdt\":\"%s\",\"trangThai\":\"%s\"}}",
                        nv.getMaNV(), nv.getHoTen(), nv.getMaPB(), nv.getChucVu(),
                        nv.getEmail(), nv.getSDT(), nv.getTrangThai()
                );
                out.print(json);
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Không tìm thấy nhân viên!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"Lỗi server: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    private void xuliUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<>();

        try {
            int maNV = Integer.parseInt(request.getParameter("maNV"));
            String hoTen = request.getParameter("hoTen");
            String maPB = request.getParameter("phongban");
            String chucVu = request.getParameter("chucVu");
            String email = request.getParameter("email");
            String sdt = request.getParameter("sdt");
            String trangThai = request.getParameter("trangThai");

            NhanVien nv = nvdao.getById(maNV);
            if (nv != null) {
                nv.setHoTen(hoTen);
                nv.setMaPB(maPB);
                nv.setChucVu(chucVu);
                nv.setEmail(email);
                nv.setSDT(sdt);
                nv.setTrangThai(trangThai);

                boolean success = nvdao.update(nv);
                if (success) {
                    NhanVien nvMoi = nvdao.getById(maNV);
                    PhongBan pb = pbdao.getByID(nvMoi.getMaPB());

                    Map<String, Object> nvJson = new HashMap<>();
                    nvJson.put("maNV", nvMoi.getMaNV());
                    nvJson.put("hoTen", nvMoi.getHoTen());
                    nvJson.put("maPB", nvMoi.getMaPB());
                    nvJson.put("tenPB", pb != null ? pb.getTenPB(): "");
                    nvJson.put("chucVu", nvMoi.getChucVu());
                    nvJson.put("email", nvMoi.getEmail());
                    nvJson.put("sdt", nvMoi.getSDT());
                    nvJson.put("trangThai", nvMoi.getTrangThai());

                    map.put("status", "success");
                    map.put("message", "Cập nhật thành công!");
                    map.put("nhanVien", nvJson);
                } else {
                    map.put("status", "error");
                    map.put("message", "Cập nhật thất bại!");
                }
            } else {
                map.put("status", "error");
                map.put("message", "Không tìm thấy nhân viên!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");
            map.put("message", "Lỗi server: " + e.getMessage());
        }

        response.getWriter().write(new Gson().toJson(map));
    }

}
