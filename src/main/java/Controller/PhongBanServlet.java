/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.NhanVienDAO;
import DAO.PhongBanDAO;
import Model.NhanVien;
import Model.PhongBan;
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
@WebServlet(name = "PhongBanServlet", urlPatterns = {"/department"})
public class PhongBanServlet extends HttpServlet {

    PhongBanDAO pbdao;

    NhanVienDAO nvdao;

    @Override
    public void init() throws ServletException {
        pbdao = new PhongBanDAO();
        nvdao = new NhanVienDAO();
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
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "list":
                response.setContentType("text/html;charset=UTF-8");
                hienThiDanhSach(request, response);
                break;
            case "add":
                xuLiThem(request, response);
                break;
            case "delete":
                xuliXoa(request, response);
                break;
            case "update":
                xuliUpdate(request, response);
                break;
            case "getByID":
                GetByID(request, response);
                break;
            case "detail":
                getDepartmentDetail(request, response);
                break;

            default:
                response.setContentType("text/html;charset=UTF-8");
                hienThiDanhSach(request, response);
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
        processRequest(request, response);
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

    private void hienThiDanhSach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<PhongBan> dsPhongBan = pbdao.getAllPhongBan();

            int tongPhongBan = pbdao.countAllPhongBan();
            int tongNhanVien = nvdao.countAllNhanVien();
            double tbNhanVienPB = tongPhongBan > 0 ? (double) tongNhanVien / tongPhongBan : 0;
            String phongDongNhat = nvdao.getPhongBanDongNhat();

            Map<Integer, Integer> nhanVienTheoPhongMap = new HashMap<>();
            for (PhongBan pb : dsPhongBan) {
                int soNV = nvdao.countNhanVienTheoPhong(pb.getMaPB());
                nhanVienTheoPhongMap.put(pb.getMaPB(), soNV);
            }
            Map<String, String> nhanVienMap = new HashMap<>();
            for (NhanVien nv : nvdao.getAll()) {
                nhanVienMap.put(nv.getMaNV(), nv.getHoTen());
            }
            List<NhanVien> dsNhanVien = nvdao.getAll();
            request.setAttribute("nhanVienMap", nhanVienMap);
            request.setAttribute("dsNhanVien", dsNhanVien);
            request.setAttribute("dsPhongBan", dsPhongBan);
            request.setAttribute("tongPhongBan", tongPhongBan);
            request.setAttribute("tongNhanVien", tongNhanVien);
            request.setAttribute("tbNhanVienPB", String.format("%.1f", tbNhanVienPB));
            request.setAttribute("phongDongNhat", phongDongNhat);
            request.setAttribute("nhanVienTheoPhongMap", nhanVienTheoPhongMap);

            request.getRequestDispatcher("JSP/Admin/department.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(PhongBanServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

    protected void xuLiThem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        Map<String, Object> map = new HashMap<>();
        Gson gson = new Gson();

        try {
            // Lấy dữ liệu từ form
            String tenPB = request.getParameter("tenpb");
            String truongPhongStr = request.getParameter("truongphong"); // Có thể null
            String moTa = request.getParameter("mota");

            // Kiểm tra dữ liệu bắt buộc
            if (tenPB == null || tenPB.isEmpty()) {
                map.put("status", "error");
                map.put("message", "Vui lòng nhập tên phòng ban!");
                response.getWriter().write(gson.toJson(map));
                return;
            }

            PhongBan pb = new PhongBan();
            pb.setTenPB(tenPB);
            pb.setMoTa(moTa);
            pb.setTruongPhong(truongPhongStr != null && !truongPhongStr.isEmpty() ? Integer.parseInt(truongPhongStr) : null);

            PhongBanDAO pbdao = new PhongBanDAO();
            boolean newId = pbdao.insert(pb);

            if (!newId) {
                throw new Exception("Không thể thêm phòng ban!");
            }
            Map<String, Object> pbJson = new HashMap<>();
            pbJson.put("maPB", newId);
            pbJson.put("tenPB", pb.getTenPB());
            pbJson.put("moTa", pb.getMoTa());
            pbJson.put("truongPhong", pb.getTruongPhong());

            map.put("status", "success");
            map.put("message", "Thêm phòng ban thành công!");
            map.put("phongBan", pbJson);

        } catch (Exception ex) {
            ex.printStackTrace();
            map.put("status", "error");
            map.put("message", "Lỗi: " + ex.getMessage());
        }

        response.getWriter().write(gson.toJson(map));
    }

    protected void xuliXoa(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<>();
        Gson gson = new Gson();

        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                map.put("status", "error");
                map.put("message", "Không xác định được phòng ban cần xóa!");
                response.getWriter().write(gson.toJson(map));
                return;
            }

            int maPB = Integer.parseInt(idStr);
            PhongBanDAO pbdao = new PhongBanDAO();
            try {
                boolean deleted = pbdao.deletePhongBan(maPB);
                if (deleted) {
                    map.put("status", "success");
                    map.put("message", "Xóa phòng ban thành công!");
                } else {
                    map.put("status", "error");
                    map.put("message", "Không thể xóa phòng ban này!");
                }
            } catch (SQLException ex) {
                // Kiểm tra xem có phải lỗi khóa ngoại
                String msg = ex.getMessage();
                if (msg != null && msg.contains("FK_NhanVien_PhongBan")) {
                    map.put("status", "error");
                    map.put("message", "Không thể xóa phòng ban này vì còn nhân viên trong phòng ban!");
                } else {
                    map.put("status", "error");
                    map.put("message", "Lỗi cơ sở dữ liệu: " + msg);
                }
            }

        } catch (NumberFormatException ex) {
            map.put("status", "error");
            map.put("message", "ID phòng ban không hợp lệ!");
        } catch (Exception ex) {
            map.put("status", "error");
            map.put("message", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }

        response.getWriter().write(gson.toJson(map));
    }

    private void xuliUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String maPB = request.getParameter("mapb");
        String tenPB = request.getParameter("tenpb");
        String truongPhong = request.getParameter("truongphong");
        int maTruongPhong = Integer.parseInt(truongPhong);
        String moTa = request.getParameter("mota");

        Map<String, String> res = new HashMap<>();

        try {
            PhongBan pb = pbdao.getByID(maPB);
            if (pb != null) {
                pb.setTenPB(tenPB);
                pb.setTruongPhong(maTruongPhong);
                pb.setMoTa(moTa);

                boolean updated = pbdao.update(pb);
                if (updated) {
                    res.put("status", "success");
                    res.put("message", "Cập nhật phòng ban thành công!");
                } else {
                    res.put("status", "error");
                    res.put("message", "Cập nhật thất bại!");
                }
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy phòng ban!");
            }
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi server: " + e.getMessage());
        }

        out.print(new Gson().toJson(res));
        out.flush();
    }

    private void GetByID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String maPB = request.getParameter("id");
        Map<String, Object> res = new HashMap<>();

        try {
            PhongBan pb = pbdao.getByID(maPB); // trả về phòng ban
            List<NhanVien> dsNhanVien = nvdao.getAll(); // danh sách nhân viên để chọn trưởng phòng

            if (pb != null) {
                res.put("status", "success");
                res.put("data", pb);
                res.put("dsNhanVien", dsNhanVien);
//                System.out.println("phòng ban: "+pb+" dsnhanvien " +dsNhanVien);
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy phòng ban!");
            }
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi server: " + e.getMessage());
        }

        out.print(new Gson().toJson(res));
        out.flush();
    }
    private void getDepartmentDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> res = new HashMap<>();

        try {
            String maPB = request.getParameter("id");
            PhongBan pb = pbdao.getByID(maPB);
            List<NhanVien> dsNhanVien = nvdao.getNhanVienTheoPhongBan(maPB); 

            if (pb != null) {
                String tenTruongPhong = "";
                if (pb.getTruongPhong() != null) {
                    NhanVien nv = nvdao.getById(pb.getTruongPhong());
                    tenTruongPhong = nv != null ? nv.getHoTen() : "";
                }

                Map<String, Object> pbJson = new HashMap<>();
                pbJson.put("maPB", pb.getMaPB());
                pbJson.put("tenPB", pb.getTenPB());
                pbJson.put("moTa", pb.getMoTa());
                pbJson.put("truongPhongTen", tenTruongPhong);

                res.put("status", "success");
                res.put("data", pbJson);
                res.put("dsNhanVien", dsNhanVien);
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy phòng ban!");
            }
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi server: " + e.getMessage());
        }

        out.print(new Gson().toJson(res));
        out.flush();
    }
}

