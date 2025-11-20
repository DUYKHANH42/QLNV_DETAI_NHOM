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
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
            case "export":
                exportExcel(request, response);
                break;
            default:
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
//        HttpSession session = request.getSession(false);
//        TaiKhoan user = (session != null) ? (TaiKhoan) session.getAttribute("account") : null;
//        if (user == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }     
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
            String luongCbStr = request.getParameter("luongcb");
            String heSoLuongStr = request.getParameter("hsluong");

            java.util.Date today = new java.util.Date();
            java.sql.Date ngayVaoLam = new java.sql.Date(today.getTime());
            double luongCb = (luongCbStr != null && !luongCbStr.isEmpty()) ? Double.parseDouble(luongCbStr) : 0.0;
            double heSoLuong = (heSoLuongStr != null && !heSoLuongStr.isEmpty()) ? Double.parseDouble(heSoLuongStr) : 0.0;
            System.out.println("luongcb: " + luongCb);
            System.out.println("hsluong: " + heSoLuong);
            if (tenDangNhap == null || password == null || hoTen == null) {
                map.put("status", "error");
                map.put("message", "Vui lòng nhập đầy đủ thông tin!");
                response.getWriter().write(new Gson().toJson(map));
                return;
            }
            NhanVien nv = new NhanVien();
            nv.setHoTen(hoTen);
            nv.setMaPB(maPB);
            nv.setChucVu(chucVu);
            nv.setEmail(email);
            nv.setSDT(sdt);
            nv.setLuongCoBan(luongCb);
            nv.setHeSoLuong(heSoLuong);
            nv.setNgayVaoLam(ngayVaoLam);
            int maNV = nvdao.insert(nv);

            if (maNV == -1) {
                throw new Exception("Không thể thêm nhân viên!");
            }
            TaiKhoan tk = new TaiKhoan();
            tk.setMaNV(maNV);
            tk.setTenDangNhap(tenDangNhap);
            tk.setMatKhau(password);
            tk.setVaiTro(vaiTro);
            if (!tkdao.insert(tk)) {
                map.put("status", "error");
                map.put("message", "Tên đăng nhập đã tồn tại!");
                response.getWriter().write(new Gson().toJson(map));
                return;
            }
            NhanVien nvMoi = nvdao.getById(maNV);
            PhongBan pb = pbdao.getByID(nvMoi.getMaPB());

            Map<String, Object> nvJson = new HashMap<>();
            nvJson.put("maNV", nvMoi.getMaNV());
            nvJson.put("hoTen", nvMoi.getHoTen());
            nvJson.put("maPB", nvMoi.getMaPB());
            nvJson.put("tenPB", pb != null ? pb.getTenPB() : "");
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
            boolean success = tkdao.delete(maNV);

            if (success) {
                nvdao.delete(maNV);
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
                        + "\"maNV\":\"%s\","
                        + "\"hoTen\":\"%s\","
                        + "\"maPB\":\"%s\","
                        + "\"chucVu\":\"%s\","
                        + "\"email\":\"%s\","
                        + "\"sdt\":\"%s\","
                        + "\"trangThai\":\"%s\","
                        + "\"luongCoBan\":%f,"
                        + "\"heSoLuong\":%f"
                        + "}}",
                        nv.getMaNV(), nv.getHoTen(), nv.getMaPB(), nv.getChucVu(),
                        nv.getEmail(), nv.getSDT(), nv.getTrangThai(),
                        nv.getLuongCoBan(), nv.getHeSoLuong()
                );
                out.print(json);
            } 
            else {
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
            String luongCbStr = request.getParameter("luongcb");
            String heSoLuongStr = request.getParameter("hsluong");
            double luongCb = (luongCbStr != null && !luongCbStr.isEmpty()) ? Double.parseDouble(luongCbStr) : 0.0;
            double heSoLuong = (heSoLuongStr != null && !heSoLuongStr.isEmpty()) ? Double.parseDouble(heSoLuongStr) : 0.0;
            NhanVien nv = nvdao.getById(maNV);
            if (nv != null) {
                nv.setHoTen(hoTen);
                nv.setMaPB(maPB);
                nv.setChucVu(chucVu);
                nv.setEmail(email);
                nv.setSDT(sdt);
                nv.setTrangThai(trangThai);
                nv.setLuongCoBan(luongCb);
                nv.setHeSoLuong(heSoLuong);

                boolean success = nvdao.update(nv);
                if (success) {
                    NhanVien nvMoi = nvdao.getById(maNV);
                    PhongBan pb = pbdao.getByID(nvMoi.getMaPB());

                    Map<String, Object> nvJson = new HashMap<>();
                    nvJson.put("maNV", nvMoi.getMaNV());
                    nvJson.put("hoTen", nvMoi.getHoTen());
                    nvJson.put("maPB", nvMoi.getMaPB());
                    nvJson.put("tenPB", pb != null ? pb.getTenPB() : "");
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

    private void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hoten = request.getParameter("hoten");
        String phongban = request.getParameter("phongban");
        List<NhanVien> list;

        if ((hoten != null && !hoten.isEmpty()) || (phongban != null && !phongban.isEmpty())) {
            list = nvdao.searchAdvanced(hoten, phongban);
        } else {
            list = nvdao.getAll();
        }

        System.out.println("Tổng nhân viên xuất Excel: " + list.size());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách nhân viên");

            // 🟩 Tạo style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // 🟦 Style cho “Mã NV” và “Họ Tên”
            CellStyle highlightStyle = workbook.createCellStyle();
            highlightStyle.cloneStyleFrom(headerStyle);
            highlightStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            highlightStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 🟨 Style cho dữ liệu chung
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 🟢 Style cho trạng thái "Đang làm"
            CellStyle workingStyle = workbook.createCellStyle();
            workingStyle.cloneStyleFrom(dataStyle);
            workingStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            workingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 🔴 Style cho trạng thái "Đã nghỉ"
            CellStyle quitStyle = workbook.createCellStyle();
            quitStyle.cloneStyleFrom(dataStyle);
            quitStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            quitStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Row header = sheet.createRow(0);
            String[] headers = {"Mã NV", "Họ Tên", "Phòng Ban", "Chức Vụ", "Email", "SĐT", "Trạng Thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                // 2 cột đầu (Mã NV, Họ Tên) dùng màu khác
                if (i <= 1) {
                    cell.setCellStyle(highlightStyle);
                } else {
                    cell.setCellStyle(headerStyle);
                }
            }

            // 📄 Ghi dữ liệu
            int rowNum = 1;
            for (NhanVien nv : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(nv.getMaNV());
                row.createCell(1).setCellValue(nv.getHoTen());
                row.createCell(2).setCellValue(nv.getTenPB());
                row.createCell(3).setCellValue(nv.getChucVu());
                row.createCell(4).setCellValue(nv.getEmail());
                row.createCell(5).setCellValue(nv.getSDT());
                row.createCell(6).setCellValue(nv.getTrangThai());

                // Áp style cho các cell dữ liệu
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        continue;
                    }
                    if (i == 6) { // Cột trạng thái
                        String trangThai = nv.getTrangThai().toLowerCase();
                        if (trangThai.contains("đang") || trangThai.contains("lam") || trangThai.contains("làm")) {
                            cell.setCellStyle(workingStyle); // Đang làm → xanh
                        } else {
                            cell.setCellStyle(quitStyle); // Đã nghỉ → đỏ
                        }
                    } else {
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            // 🔄 Tự động giãn độ rộng cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 📥 Gửi file về client
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=DanhSachNhanVien.xlsx");

            try (ServletOutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xuất Excel thất bại!");
        }
    }

}
