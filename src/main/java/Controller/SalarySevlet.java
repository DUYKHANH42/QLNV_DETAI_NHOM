/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.ChiTietLuongDAO;
import Model.Luong;
import DAO.LuongDAO;
import DAO.NhanVienDAO;
import DAO.PhongBanDAO;
import Model.ChiTietLuong;
import Model.NhanVien;
import Model.PhongBan;
import Model.TrangThaiLuong;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author PC
 */
@WebServlet(name = "SalarySevlet", urlPatterns = {"/salary"})
public class SalarySevlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    LuongDAO ldao;
    NhanVienDAO nvDAO;
    PhongBanDAO pbDAO;

    public void init()
            throws ServletException {
        ldao = new LuongDAO();
        nvDAO = new NhanVienDAO();
        pbDAO = new PhongBanDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DocumentException {
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
                List<NhanVien> dsNhanVien = nvDAO.getAll(); // tất cả nhân viên
                request.setAttribute("dsNhanVien", dsNhanVien);
                List<PhongBan> dsPhongBan = pbDAO.getAllPhongBan();
                request.setAttribute("dsPhongBan", dsPhongBan);
                break;
            case "delete":
//                xuliXoa(request, response);
                break;
            case "getEmployeeData":
                getEmployeeData(request, response);
                break;
            case "update":
                xuliUpdate(request, response);
                break;
            case "getByID":
                getByID(request, response);
                break;
            case "detail":
                getSalaryDetail(request, response);
                break;
            case "exportExcel":
                exportExcelSalaryDetail(request, response);
                break;
            case "exportPDF":
                exportPdfSalaryDetail(request, response);
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
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(SalarySevlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            processRequest(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(SalarySevlet.class.getName()).log(Level.SEVERE, null, ex);
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

    protected void hienThiDanhSach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Lấy filter từ request
        String monthYear = request.getParameter("filterMonth");
        String departmentFilter = request.getParameter("filterDepartment");
        String search = request.getParameter("search");
        if (search != null) {
            search = search.trim().toLowerCase();
        }

        int month = 0, year = 0;
        if (monthYear != null && !monthYear.isEmpty() && !monthYear.equals("undefined")) {
            String[] parts = monthYear.split("/");
            month = Integer.parseInt(parts[0]);
            year = Integer.parseInt(parts[1]);
        }

        // Lấy danh sách lương theo tháng/năm nếu có
        List<Luong> luongList;
        if (month > 0 && year > 0) {
            luongList = ldao.searchLuongByThangNam(month, year);
        } else {
            luongList = ldao.getAll();
        }

        // Lọc theo search + phòng ban
        List<Map<String, Object>> salaryListFiltered = new ArrayList<>();
        for (Luong l : luongList) {
            NhanVien nv = nvDAO.getById(l.getMaNV());
            PhongBan pb = pbDAO.getByID(nv.getMaPB());

            // Lọc theo phòng ban
            if (departmentFilter != null && !departmentFilter.isEmpty() && !departmentFilter.equals("undefined")) {
                if (!String.valueOf(pb.getMaPB()).equals(departmentFilter)) {
                    continue;
                }
            }

            // Lọc theo tên hoặc mã NV
            if (search != null && !search.isEmpty()) {
                if (!nv.getHoTen().toLowerCase().contains(search)
                        && !nv.getMaNV().toLowerCase().contains(search)) {
                    continue;
                }
            }

            Map<String, Object> row = new HashMap<>();
            row.put("maNV", nv.getMaNV());
            row.put("hoTen", nv.getHoTen());
            row.put("phongBan", pb.getTenPB());
            row.put("chucVu", nv.getChucVu());
            row.put("dsPhongBan", pbDAO.getAllPhongBan());
            row.put("luongCoBan", l.getLuongCoBan());
            row.put("phuCap", l.getTongPhuCap());
            row.put("thuong", l.getThuong());
            row.put("tongLuong", l.getThucLinh());
            row.put("trangThai", l.getTrangThai().toString());

            salaryListFiltered.add(row);
        }

        // Phân trang
        int page = 1;
        int pageSize = 5;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalRecords = salaryListFiltered.size();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecords);
        List<Map<String, Object>> salaryListPaged = salaryListFiltered.subList(start, end);

        // Thống kê
        double tongLuongThang = 0;
        int daThanhToan = 0;
        int choThanhToan = 0;
        for (Luong l : luongList) {
            NhanVien nv = nvDAO.getById(l.getMaNV());
            PhongBan pb = pbDAO.getByID(nv.getMaPB());

            if (departmentFilter != null && !departmentFilter.isEmpty() && !departmentFilter.equals("undefined")) {
                if (!String.valueOf(pb.getMaPB()).equals(departmentFilter)) {
                    continue;
                }
            }
            if (search != null && !search.isEmpty()) {
                if (!nv.getHoTen().toLowerCase().contains(search)
                        && !nv.getMaNV().toLowerCase().contains(search)) {
                    continue;
                }
            }

            tongLuongThang += l.getThucLinh();
            if ("Paid".equalsIgnoreCase(l.getTrangThai().toString())) {
                daThanhToan++;
            } else {
                choThanhToan++;
            }
        }

        double luongTrungBinh = totalRecords > 0 ? tongLuongThang / totalRecords : 0;
        List<NhanVien> dsNhanVien = nvDAO.getAll();
        // Set attribute trả về JSP
        request.setAttribute("salaryList", salaryListPaged);
        request.setAttribute("dsNhanVien", dsNhanVien);
        request.setAttribute("currentPageLuong", page);
        request.setAttribute("totalLuongPages", totalPages);
        request.setAttribute("tongLuongThang", tongLuongThang);
        request.setAttribute("luongTrungBinh", luongTrungBinh);
        request.setAttribute("daThanhToan", daThanhToan);
        request.setAttribute("choThanhToan", choThanhToan);

        // Chuyển tới JSP hiển thị
        request.getRequestDispatcher("JSP/Admin/salary.jsp").forward(request, response);
    }

    private void getEmployeeData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maNV = request.getParameter("maNV");
        NhanVien nv = nvDAO.getById(Integer.parseInt(maNV));
        Luong l = ldao.getByMaNV(Integer.parseInt(maNV));
        PhongBan pb = pbDAO.getByID(nv.getMaPB());
        Map<String, Object> result = new HashMap<>();
        result.put("chucVu", nv.getChucVu());
        result.put("maPB", pb.getMaPB());
        result.put("tenPB", pb.getTenPB());
        result.put("luongCoBan", l != null ? l.getLuongCoBan() : 0);

        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().write(json);
        response.getWriter().flush();
        return;
    }

    private void xuLiThem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        int maNV = Integer.parseInt(request.getParameter("maNV"));
        String thangNam = request.getParameter("thangNam"); // "2025-10"
        String[] parts = thangNam.split("-");
        int thang = Integer.parseInt(parts[1]);
        int nam = Integer.parseInt(parts[0]);

        double luongCoBan = Double.parseDouble(request.getParameter("luongCoBan"));
        double phuCap = Double.parseDouble(request.getParameter("phuCap"));
        double thuong = Double.parseDouble(request.getParameter("thuong"));
        double khauTru = Double.parseDouble(request.getParameter("khauTru"));
        String trangThai = request.getParameter("trangThai");
        String ghiChu = request.getParameter("ghiChu");

        LuongDAO ldao = new LuongDAO();
        ChiTietLuongDAO ctlDAO = new ChiTietLuongDAO();

        // Kiểm tra đã có bảng lương chưa
        List<Luong> existing = ldao.searchLuongByThangNam(thang, nam);
        boolean exists = existing.stream().anyMatch(l -> l.getMaNV() == maNV);
        if (exists) {
            result.put("success", false);
            result.put("message", "Nhân viên này đã có bảng lương tháng " + thang + "/" + nam);
        } else {
            // Thêm bảng lương
            Luong l = new Luong();
            l.setMaNV(maNV);
            l.setThang(thang);
            l.setNam(nam);
            l.setLuongCoBan(luongCoBan);
            l.setTongPhuCap(phuCap);
            l.setThuong(thuong);
            l.setTongKhauTru(khauTru);
            l.setTongThuNhap(luongCoBan + phuCap + thuong);
            l.setThucLinh(luongCoBan + phuCap + thuong - khauTru);
            l.setTrangThai(TrangThaiLuong.fromString(trangThai));
            l.setGhiChu(ghiChu);
            if (ldao.insert(l)) {
                List<Luong> allLuong = ldao.getAll();
                Luong luongMoi = allLuong.get(allLuong.size() - 1); // lấy bản mới nhất

                String moTaThang = "Tháng " + thang + "/" + nam;
                ChiTietLuong ctl1 = new ChiTietLuong();
                ctl1.setMaLuong(luongMoi.getMaLuong());
                ctl1.setLoaiChiTiet("LuongCoBan");
                ctl1.setMoTa("Lương cơ bản - " + moTaThang);
                ctl1.setSoTien(BigDecimal.valueOf(luongCoBan));
                ctlDAO.insert(ctl1);
                ChiTietLuong ctl2 = new ChiTietLuong();
                ctl2.setMaLuong(luongMoi.getMaLuong());
                ctl2.setLoaiChiTiet("PhuCap");
                ctl2.setMoTa("Phụ cấp - " + moTaThang);
                ctl2.setSoTien(BigDecimal.valueOf(phuCap));
                ctlDAO.insert(ctl2);
                ChiTietLuong ctl3 = new ChiTietLuong();
                ctl2.setMaLuong(luongMoi.getMaLuong());
                ctl3.setLoaiChiTiet("Thuong");
                ctl3.setMoTa("Thưởng - " + moTaThang);
                ctl3.setSoTien(BigDecimal.valueOf(thuong));
                ctlDAO.insert(ctl3);
                ChiTietLuong ctl4 = new ChiTietLuong();
                ctl2.setMaLuong(luongMoi.getMaLuong());
                ctl4.setLoaiChiTiet("KhauTru");
                ctl4.setMoTa("Khấu trừ - " + moTaThang);
                ctl4.setSoTien(BigDecimal.valueOf(khauTru));
                ctlDAO.insert(ctl4);
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("message", "Không thể thêm bảng lương");
            }
        }
        gson.toJson(result, response.getWriter());
    }

    private void getByID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maNVStr = request.getParameter("maNV");
        int maNV = Integer.parseInt(maNVStr);

        Luong l = ldao.getByMaNV(maNV);
        NhanVien nv = nvDAO.getById(maNV);
        PhongBan pb = pbDAO.getByID(nv.getMaPB());

        Map<String, Object> result = new HashMap<>();
        result.put("id", l != null ? l.getMaLuong() : 0);
        result.put("maNV", nv.getMaNV());
        result.put("hoTen", nv.getHoTen());
        result.put("phongBan", pb.getTenPB());
        result.put("maPB", pb.getMaPB());
        result.put("chucVu", nv.getChucVu());
        result.put("thang", l != null ? String.format("%04d-%02d", l.getNam(), l.getThang()) : "");
        result.put("luongCoBan", l != null ? l.getLuongCoBan() : 0);
        result.put("phuCap", l != null ? l.getTongPhuCap() : 0);
        result.put("thuong", l != null ? l.getThuong() : 0);
        result.put("khauTru", l != null ? l.getTongKhauTru() : 0);
        result.put("trangThai", l != null ? l.getTrangThai().toString() : "UnPaid");
        result.put("ghiChu", l != null ? l.getGhiChu() : "");

        response.setContentType("application/json; charset=UTF-8");
        new Gson().toJson(result, response.getWriter());
    }

    private void xuliUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try {
            int id = Integer.parseInt(request.getParameter("salaryId"));
            int maNV = Integer.parseInt(request.getParameter("maNV"));
            int phongBanId = Integer.parseInt(request.getParameter("phongBanId"));
            String chucVu = request.getParameter("chucVu");
            String thangNam = request.getParameter("thang"); // "2025-10"
            String[] parts = thangNam.split("-");
            int nam = Integer.parseInt(parts[0]);
            int thang = Integer.parseInt(parts[1]);

            double luongCoBan = Double.parseDouble(request.getParameter("luongCoBan"));
            double phuCap = Double.parseDouble(request.getParameter("phuCap"));
            double thuong = Double.parseDouble(request.getParameter("thuong"));
            double khauTru = Double.parseDouble(request.getParameter("khauTru"));
            String trangThai = request.getParameter("trangThai");
            String ghiChu = request.getParameter("ghiChu");

            Luong l = ldao.getByID(id);
            if (l != null) {
                l.setThang(thang);
                l.setNam(nam);
                l.setLuongCoBan(luongCoBan);
                l.setTongPhuCap(phuCap);
                l.setThuong(thuong);
                l.setTongKhauTru(khauTru);
                l.setTongThuNhap(luongCoBan + phuCap + thuong);
                l.setThucLinh(luongCoBan + phuCap + thuong - khauTru);
                l.setTrangThai(TrangThaiLuong.fromString(trangThai));
                l.setGhiChu(ghiChu);

                boolean updated = ldao.update(l);
                if (updated) {
                    NhanVien nv = nvDAO.getById(maNV);
                    if (nv != null) {
                        nv.setChucVu(chucVu);
                        nv.setMaPB(String.valueOf(phongBanId));
                        nvDAO.update(nv);
                    }

                    result.put("success", true);
                    result.put("message", "Cập nhật bảng lương thành công!");
                } else {
                    result.put("success", false);
                    result.put("message", "Cập nhật bảng lương thất bại.");
                }
            } else {
                result.put("success", false);
                result.put("message", "Không tìm thấy bảng lương với ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // log lỗi chi tiết server
            result.put("success", false);
            result.put("message", "Lỗi khi cập nhật: " + e.getMessage());
        }

        gson.toJson(result, response.getWriter());
    }

    private void getSalaryDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maNVStr = request.getParameter("maNV");
        int maNV = Integer.parseInt(maNVStr);

        Luong l = ldao.getByMaNV(maNV);
        NhanVien nv = nvDAO.getById(maNV);
        PhongBan pb = pbDAO.getByID(nv.getMaPB());

        Map<String, Object> result = new HashMap<>();
        result.put("id", l != null ? l.getMaLuong() : 0);
        result.put("maNV", nv.getMaNV());
        result.put("hoTen", nv.getHoTen());
        result.put("phongBan", pb.getTenPB());
        result.put("chucVu", nv.getChucVu());
        result.put("thang", l != null ? String.format("%02d/%04d", l.getThang(), l.getNam()) : "");
        result.put("luongCoBan", l != null ? l.getLuongCoBan() : 0);
        result.put("phuCap", l != null ? l.getTongPhuCap() : 0);
        result.put("thuong", l != null ? l.getThuong() : 0);
        result.put("khauTru", l != null ? l.getTongKhauTru() : 0);
        result.put("thucLinh", l != null ? l.getThucLinh() : 0);
        result.put("trangThai", l != null ? l.getTrangThai().toString() : "UnPaid");
        result.put("ghiChu", l != null ? l.getGhiChu() : "");

        // Lấy chi tiết từng khoản lương
        List<ChiTietLuong> chiTietList = new ArrayList<>();
        if (l != null) {
            ChiTietLuongDAO ctlDAO = new ChiTietLuongDAO();
            chiTietList = ctlDAO.getByMaLuong(l.getMaLuong());
        }
        result.put("chiTietLuong", chiTietList);

        response.setContentType("application/json; charset=UTF-8");
        new Gson().toJson(result, response.getWriter());
    }

    private void exportExcelSalaryDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int maNV = Integer.parseInt(request.getParameter("maNV"));

        Luong l = ldao.getByMaNV(maNV);
        NhanVien nv = nvDAO.getById(maNV);
        PhongBan pb = pbDAO.getByID(nv.getMaPB());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=ChiTietLuong_" + maNV + ".xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Chi Tiết Lương");

        int rownum = 0;

        // =================== Style ===================
        // Header style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Money style
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
        moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

        // Label style
        CellStyle labelStyle = workbook.createCellStyle();
        labelStyle.setAlignment(HorizontalAlignment.LEFT);
        labelStyle.setFont(headerFont);

        // Bold + right alignment (for total)
        CellStyle boldRightStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldRightStyle.setFont(boldFont);
        boldRightStyle.setAlignment(HorizontalAlignment.RIGHT);

        // =================== Thông tin nhân viên ===================
        Row row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("THÔNG TIN NHÂN VIÊN");
        row.getCell(0).setCellStyle(headerStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum - 1, rownum - 1, 0, 1));

        String[][] nvInfo = {
            {"Mã NV:", String.valueOf(nv.getMaNV())},
            {"Họ Tên:", nv.getHoTen()},
            {"Phòng Ban:", pb.getTenPB()},
            {"Chức Vụ:", nv.getChucVu()}
        };
        for (String[] info : nvInfo) {
            row = sheet.createRow(rownum++);
            Cell c0 = row.createCell(0);
            c0.setCellValue(info[0]);
            c0.setCellStyle(labelStyle);
            Cell c1 = row.createCell(1);
            c1.setCellValue(info[1]);
            c1.setCellStyle(moneyStyle);
        }
        rownum++;

        // =================== Chi tiết lương ===================
        row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("CHI TIẾT LƯƠNG");
        row.getCell(0).setCellStyle(headerStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum - 1, rownum - 1, 0, 1));

        row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("Tháng:");
        row.getCell(0).setCellStyle(labelStyle);
        row.createCell(1).setCellValue(String.format("%02d/%04d", l.getThang(), l.getNam()));
        row.getCell(1).setCellStyle(moneyStyle);

        row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("Trạng Thái:");
        row.getCell(0).setCellStyle(labelStyle);
        row.createCell(1).setCellValue(l.getTrangThai() != null && l.getTrangThai() == TrangThaiLuong.Paid ? "Đã thanh toán" : "Chờ thanh toán");
        row.getCell(1).setCellStyle(moneyStyle);
        rownum++;

        // =================== Bảng tính lương ===================
        row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("BẢNG TÍNH LƯƠNG");
        row.getCell(0).setCellStyle(headerStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum - 1, rownum - 1, 0, 1));

        row = sheet.createRow(rownum++);
        Cell c0 = row.createCell(0);
        c0.setCellValue("Khoản Mục");
        c0.setCellStyle(headerStyle);
        Cell c1 = row.createCell(1);
        c1.setCellValue("Số Tiền (VNĐ)");
        c1.setCellStyle(headerStyle);

        String[] labels = {"Lương cơ bản", "Tổng phụ cấp", "Thưởng", "Phạt", "Tổng tăng ca", "Tổng khấu trừ", "Lương Thực Nhận"};
        double[] values = {
            l.getLuongCoBan(),
            l.getTongPhuCap(),
            l.getThuong(),
            0,
            0,
            l.getTongKhauTru(),
            l.getThucLinh()
        };

        for (int i = 0; i < labels.length; i++) {
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue(labels[i]);
            Cell valCell = row.createCell(1);
            valCell.setCellValue(values[i]);
            valCell.setCellStyle(moneyStyle);
            row.getCell(0).setCellStyle(labelStyle);
            if (i == labels.length - 1) {
                valCell.setCellStyle(boldRightStyle); // Lương thực nhận in đậm
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void exportPdfSalaryDetail(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        int maNV = Integer.parseInt(request.getParameter("maNV"));

        // Lấy dữ liệu giống modal
        Luong l = ldao.getByMaNV(maNV);
        NhanVien nv = nvDAO.getById(maNV);
        PhongBan pb = pbDAO.getByID(nv.getMaPB());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ChiTietLuong_" + maNV + ".pdf");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        String fontPath = request.getServletContext().getRealPath("/fonts/UTM Times.ttf");
        com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(
              fontPath,
                com.itextpdf.text.pdf.BaseFont.IDENTITY_H,
                com.itextpdf.text.pdf.BaseFont.EMBEDDED
        );
        com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
        com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

        document.add(new com.itextpdf.text.Paragraph("THÔNG TIN NHÂN VIÊN", fontBold));
        document.add(new com.itextpdf.text.Paragraph("Mã NV: " + nv.getMaNV(), fontNormal));
        document.add(new com.itextpdf.text.Paragraph("Họ Tên: " + nv.getHoTen(), fontNormal));
        document.add(new com.itextpdf.text.Paragraph("Phòng Ban: " + pb.getTenPB(), fontNormal));
        document.add(new com.itextpdf.text.Paragraph("Chức Vụ: " + nv.getChucVu(), fontNormal));
        document.add(com.itextpdf.text.Chunk.NEWLINE);

        document.add(new com.itextpdf.text.Paragraph("CHI TIẾT LƯƠNG", fontBold));
        document.add(new com.itextpdf.text.Paragraph("Tháng: " + String.format("%02d/%04d", l.getThang(), l.getNam()), fontNormal));
        document.add(new com.itextpdf.text.Paragraph("Trạng Thái: " + (l.getTrangThai() != null && l.getTrangThai() == TrangThaiLuong.Paid ? "Đã thanh toán" : "Chờ thanh toán"), fontNormal));
        document.add(com.itextpdf.text.Chunk.NEWLINE);

        com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{3f, 2f});
        com.itextpdf.text.pdf.PdfPCell cell;
        cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Paragraph("Khoản Mục", fontBold));
        cell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Paragraph("Số Tiền (VNĐ)", fontBold));
        cell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        table.addCell(cell);

        String[] labels = {"Lương cơ bản", "Tổng phụ cấp", "Thưởng", "Phạt", "Tổng tăng ca", "Tổng khấu trừ", "Lương Thực Nhận"};
        double[] values = {
            l.getLuongCoBan(),
            l.getTongPhuCap(),
            l.getThuong(),
            0,
            0,
            l.getTongKhauTru(),
            l.getThucLinh()
        };

        for (int i = 0; i < labels.length; i++) {
            cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Paragraph(labels[i], fontNormal));
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Paragraph(String.format("%,.0f", values[i]), fontNormal));
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table.addCell(cell);
        }
        System.out.println("Export PDF maNV=" + maNV);
        System.out.println("Luong: " + l);
        System.out.println("NhanVien: " + nv);
        System.out.println("PhongBan: " + pb);
        document.add(table);
        document.close();
    }

}
