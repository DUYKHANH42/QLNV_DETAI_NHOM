/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.ChamCongDAO;
import DAO.NhanVienDAO;
import DAO.PhongBanDAO;
import Model.ChamCong;
import Model.NhanVien;
import Model.PhongBan;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author PC
 */
@WebServlet(name = "CheckinServlet", urlPatterns = {"/checkin"})
public class CheckinServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    NhanVienDAO nvdao;
    PhongBanDAO pbdao;
    ChamCongDAO ccdao;

    @Override
    public void init()
            throws ServletException {
        nvdao = new NhanVienDAO();
        pbdao = new PhongBanDAO();
        ccdao = new ChamCongDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
//                System.out.println("HAHA");
//                xuLiThem(request, response);
                break;
            case "chamCong":
//                System.out.println("HAHA");
                chamCong(request, response);
                break;
            case "search":
                timKiem(request, response);
            case "searchAll":
                searchAll(request, response);
                break;
            case "detail":
                Detail(request, response);
                break;
            case "delete":
//                xuliXoa(request, response);
                break;
            case "update":
                xuliUpdate(request, response);
                break;
            case "getById":
//                GetById(request, response);
                break;
            case "exportExcelChiTietCC":
                exportExcelChiTietCC(request, response);
                break;
            case "exportExcelCC":
                exportExcelCC(request, response);
                break;
            case "exportPDFChiTietCC":
                exportPDFChiTietCC(request, response);
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
//         request.getRequestDispatcher("JSP/Admin/checkin.jsp").forward(request, response);
//        hienThiDanhSach(request, response);
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
            int pageSize = 5;

            String currentPage = request.getParameter("page");
            if (currentPage != null && !currentPage.isEmpty()) {
                try {
                    page = Integer.parseInt(currentPage);
                } catch (NumberFormatException e) {
                
                }
            }

            int totalCount = nvdao.getAll().size();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            List<NhanVien> dsNhanVien = nvdao.getByPage(page, pageSize);

            List<PhongBan> dsPhongBan = pbdao.getAllPhongBan();
            Map<String, String> phongBanMap = new HashMap<>();
            for (PhongBan pb : dsPhongBan) {
                phongBanMap.put(String.valueOf(pb.getMaPB()), pb.getTenPB());
            }

            for (NhanVien nv : dsNhanVien) {
                String tenPB = phongBanMap.get(String.valueOf(nv.getMaPB()));
                nv.setTenPB(tenPB != null ? tenPB : "Chưa có");
            }
            String dateStr = request.getParameter("date");
            LocalDate ngayLam = (dateStr != null && !dateStr.trim().isEmpty())
                    ? LocalDate.parse(dateStr.trim())
                    : LocalDate.now();

            Map<String, ChamCong> chamCongMap = new HashMap<>();
            for (NhanVien nv : dsNhanVien) {
                ChamCong cc = ccdao.getChamCongByDate(nv.getMaNV(), ngayLam); // bạn cần viết hàm này
                if (cc != null) {
                    chamCongMap.put(nv.getMaNV(), cc);
                }
            }
            request.setAttribute("chamCongMap", chamCongMap);

            Map<String, Integer> soNgayCongMap = new HashMap<>();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int thangHienTai = cal.get(java.util.Calendar.MONTH) + 1;
            int namHienTai = cal.get(java.util.Calendar.YEAR);

            for (NhanVien nv : dsNhanVien) {
                int soNgayCong = ccdao.countNgayCong(nv.getMaNV(), thangHienTai, namHienTai);
                soNgayCongMap.put(nv.getMaNV(), soNgayCong);
            }
            int daChamCong = ccdao.countDaChamCong(thangHienTai, namHienTai);
            int diMuon = ccdao.countDiMuon(thangHienTai, namHienTai);
            LocalDate homNay = LocalDate.now();
            List<NhanVien> dsTatCaNV = nvdao.getAll();
            int soNhanVienDaCham = 0;
            for (NhanVien nv : dsTatCaNV) {
                if (ccdao.daChamCong(nv.getMaNV(), homNay)) {
                    soNhanVienDaCham++;
                }
            }
            int vangHomNay = dsTatCaNV.size() - soNhanVienDaCham;
            request.setAttribute("vangHomNay", vangHomNay);

            int tongNV = nvdao.countAllNhanVien();
            double tyLe = tongNV > 0 ? (daChamCong * 100.0 / tongNV) : 0;
            LocalDate today = LocalDate.now();
            request.setAttribute("today", today.toString());
            Date ngayLam2 = Date.valueOf(today);
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                ngayLam2 = java.sql.Date.valueOf(dateStr.trim());
            }
//            System.out.println(ngayLam.toString());
            request.setAttribute("dateValue", ngayLam2.toString());
//            System.out.println("ds chấm công: " + dsChamCong);
//            System.out.println("số ngày công: " + soNgayCongMap);
            request.setAttribute("daChamCong", daChamCong);
            request.setAttribute("dsPhongBan", dsPhongBan);
            request.setAttribute("diMuon", diMuon);
            request.setAttribute("tyLe", String.format("%.0f%%", tyLe));
            request.setAttribute("soNgayCongMap", soNgayCongMap);
            request.setAttribute("chamCongMap", chamCongMap);
            request.setAttribute("dsNhanVien", dsNhanVien);
//            request.setAttribute("dsChamCong", dsChamCong);
            request.setAttribute("page", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("redirectUrl", "checkin?action=list");
            request.getRequestDispatcher("JSP/Admin/checkin.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timKiem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Lấy tham số từ form
        String dateStr = request.getParameter("date");
        String maPB = request.getParameter("phongban");
        String tenNV = request.getParameter("tenNV");
        LocalDate today = LocalDate.now();
        request.setAttribute("today", today.toString());
        Date ngayLam = Date.valueOf(today);
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            ngayLam = java.sql.Date.valueOf(dateStr.trim());
        }
//        System.out.println(ngayLam.toString());
        request.setAttribute("dateValue", ngayLam.toString());

        // DAO
        NhanVienDAO nvdao = new NhanVienDAO();
        ChamCongDAO ccdao = new ChamCongDAO();

        // --- BƯỚC 1: LẤY DANH SÁCH NHÂN VIÊN THEO FILTER ---
        List<NhanVien> dsNhanVien;
        if (tenNV != null && !tenNV.trim().isEmpty() && maPB != null && !maPB.trim().isEmpty()) {
            // Filter theo tên + phòng
            dsNhanVien = nvdao.searchAdvanced(tenNV.trim(), maPB.trim());
        } else if (tenNV != null && !tenNV.trim().isEmpty()) {
            // Chỉ filter theo tên
            dsNhanVien = nvdao.getByTen(tenNV.trim());
        } else if (maPB != null && !maPB.trim().isEmpty()) {
            // Chỉ filter theo phòng
            dsNhanVien = nvdao.getByPhongBan(maPB.trim());
        } else {
            // Không filter -> tất cả
            dsNhanVien = nvdao.getAll();
        }

        // --- BƯỚC 2: PHÂN TRANG ---
        int page = 1;
        int pageSize = 5;
        String currentPage = request.getParameter("page");
        if (currentPage != null && !currentPage.isEmpty()) {
            try {
                page = Integer.parseInt(currentPage);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int totalCount = dsNhanVien.size();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalCount);
        List<NhanVien> dsNhanVienPage = dsNhanVien.subList(fromIndex, toIndex);

        // --- BƯỚC 3: LẤY DANH SÁCH MA NV để search ChamCong ---
        List<String> maNVList = dsNhanVienPage.stream()
                .map(NhanVien::getMaNV)
                .collect(Collectors.toList());

        List<ChamCong> dsChamCong = ccdao.searchChamCongByMaNVList(maNVList, ngayLam);

        // --- BƯỚC 4: Map phòng ban ---
        List<PhongBan> dsPhongBan = pbdao.getAllPhongBan();
        Map<String, String> phongBanMap = new HashMap<>();
        for (PhongBan pb : dsPhongBan) {
            phongBanMap.put(String.valueOf(pb.getMaPB()), pb.getTenPB());
        }
        for (NhanVien nv : dsNhanVienPage) {
            nv.setTenPB(phongBanMap.getOrDefault(nv.getMaPB(), "Chưa có"));
        }

        // --- BƯỚC 5: Map ChamCong ---
        Map<String, ChamCong> chamCongMap = new HashMap<>();
        for (ChamCong cc : dsChamCong) {
            String key = cc.getMaNV(); // hoặc nếu muốn nhiều ngày, key = maNV+ngayLam
            if (!chamCongMap.containsKey(key)) {
                chamCongMap.put(key, cc);
            }
        }

        // --- BƯỚC 6: Map số ngày công ---
        Map<String, Integer> soNgayCongMap = new HashMap<>();
        int thangLoc = ngayLam.toLocalDate().getMonthValue();
        int namLoc = ngayLam.toLocalDate().getYear();
        for (NhanVien nv : dsNhanVienPage) {
            int soNgayCong = ccdao.countNgayCong(nv.getMaNV(), thangLoc, namLoc);
            soNgayCongMap.put(nv.getMaNV(), soNgayCong);
        }

        System.out.println(soNgayCongMap);

        // --- BƯỚC 7: Set attributes cho JSP ---
        request.setAttribute("dsPhongBan", dsPhongBan);
        request.setAttribute("dsNhanVien", dsNhanVienPage);
        request.setAttribute("chamCongMap", chamCongMap);
        request.setAttribute("soNgayCongMap", soNgayCongMap);
//        request.setAttribute("dateValue", dateStr);
        request.setAttribute("selectedPB", maPB);
        request.setAttribute("paramTenNV", tenNV);
        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("redirectUrl", "checkin?action=search&date=" + dateStr + "&phongban=" + maPB + "&tenNV=" + tenNV);
        request.getRequestDispatcher("JSP/Admin/checkin.jsp").forward(request, response);
    }

    private void searchAll(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            request.setCharacterEncoding("UTF-8");

            String dateStr = request.getParameter("date");
            String maPB = request.getParameter("phongban");
            String tenNV = request.getParameter("tenNV");

            Date ngayLam = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                ngayLam = java.sql.Date.valueOf(dateStr);
            }

            NhanVienDAO nvdao = new NhanVienDAO();
            List<NhanVien> dsNhanVien;

            // Tìm kiếm theo filter
            if (tenNV != null && !tenNV.trim().isEmpty() && maPB != null && !maPB.trim().isEmpty()) {
                dsNhanVien = nvdao.searchAdvanced(tenNV.trim(), maPB.trim());
            } else if (tenNV != null && !tenNV.trim().isEmpty()) {
                dsNhanVien = nvdao.getByTen(tenNV.trim());
            } else if (maPB != null && !maPB.trim().isEmpty()) {
                dsNhanVien = nvdao.getByPhongBan(maPB.trim());
            } else {
                dsNhanVien = nvdao.getAll();
            }

            // Trả về JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new Gson().toJson(dsNhanVien, response.getWriter());
            return;
        } catch (IOException ex) {
            Logger.getLogger(CheckinServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void chamCong(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();
        try {
            // JSON gửi lên gồm maNVList và date
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            List<String> maNVList = gson.fromJson(json.get("maNVList"), new TypeToken<List<String>>() {
            }.getType());
            LocalDate ngayLam = LocalDate.parse(json.get("date").getAsString());

            boolean saved = false;
            for (String maNV : maNVList) {
                saved |= ccdao.chamCongNhanVien(maNV, ngayLam);
//                System.out.println(ccdao.daChamCong(maNV, ngayLam));
            }

            result.put("success", saved);
            if (!saved) {
                result.put("message", "Ngày chấm công phải >= hôm nay, không lưu được bản ghi!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Có lỗi xảy ra!");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        gson.toJson(result, response.getWriter());
    }

    private void xuliUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try (BufferedReader reader = request.getReader()) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            String maNV = json.get("id").getAsString();
            Date ngay = Date.valueOf(json.get("ngay").getAsString());
            String trangThai = json.get("trangthai").getAsString();
            System.out.println("Trạng thái chỉnh sửa: " + trangThai);
            String gioVaoStr = json.has("gio_vao") ? json.get("gio_vao").getAsString() : null;
            String gioRaStr = json.has("gio_ra") ? json.get("gio_ra").getAsString() : null;

            Time gioVao = (gioVaoStr != null && !gioVaoStr.isEmpty()) ? Time.valueOf(gioVaoStr + ":00") : null;
            Time gioRa = (gioRaStr != null && !gioRaStr.isEmpty()) ? Time.valueOf(gioRaStr + ":00") : null;

            // Kiểm tra xem nhân viên đã có bản ghi chấm công hôm nay chưa
            ChamCong cc = ccdao.getByMaNVAndNgay(maNV, ngay);

            if (cc == null) {
                // Chưa có bản ghi → tạo mới
                cc = new ChamCong();
                cc.setMaNV(maNV);
                cc.setNgayLam(ngay);
                cc.setTrangThai(trangThai);
                cc.setGioVao(gioVao);
                cc.setGioRa(gioRa);

                boolean created = ccdao.insert(cc);
                result.put("success", created);
                if (!created) {
                    result.put("message", "Tạo bản ghi chấm công thất bại!");
                }
            } else {
                // Đã có bản ghi → update
                cc.setTrangThai(trangThai);
                cc.setGioVao(gioVao);
                cc.setGioRa(gioRa);

                boolean updated = ccdao.update(cc);
                result.put("success", updated);
                if (!updated) {
                    result.put("message", "Cập nhật chấm công thất bại!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Có lỗi xảy ra!");
        }

        gson.toJson(result, response.getWriter());
    }

    private void Detail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try {
            String thangStr = request.getParameter("thang");
            String namStr = request.getParameter("nam");

            int thang, nam;
            LocalDate today = LocalDate.now();

            try {
                thang = thangStr != null ? Integer.parseInt(thangStr) : today.getMonthValue();
                nam = namStr != null ? Integer.parseInt(namStr) : today.getYear();
            } catch (NumberFormatException e) {
                thang = today.getMonthValue();
                nam = today.getYear();
            }
            String maNV = request.getParameter("maNV");
            if (maNV == null || maNV.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Thiếu mã nhân viên");
                gson.toJson(result, response.getWriter());
                return;
            }
            NhanVien nv = nvdao.getById(Integer.parseInt(maNV));
            LocalDate now = LocalDate.now();
            List<ChamCong> dsChamCong = ccdao.getChamCongTheoThang(maNV, thang, nam);

            int coMat = 0, diMuon = 0, vang = 0, nghiPhep = 0;
            List<Map<String, Object>> dsChiTiet = new ArrayList<>();

            Map<LocalDate, ChamCong> mapChamCong = dsChamCong.stream()
                    .collect(Collectors.toMap(
                            cc -> cc.getNgayLam().toLocalDate(),
                            cc -> cc
                    ));

            YearMonth yearMonth = YearMonth.of(nam, thang);
            for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                LocalDate ngay = LocalDate.of(nam, thang, day);
                ChamCong c = mapChamCong.get(ngay);

                Map<String, Object> item = new HashMap<>();
                item.put("ngay", String.format("%02d/%02d", day, thang));
                item.put("thu", getThuTrongTuan(Date.valueOf(ngay)));

                boolean isSunday = ngay.getDayOfWeek() == DayOfWeek.SUNDAY;
                item.put("isSunday", isSunday); // gửi flag cho frontend

                if (c != null) {
                    // Có bản ghi => hiển thị bình thường
                    item.put("gioVao", c.getGioVao() != null ? c.getGioVao().toString() : "--:--");
                    item.put("gioRa", c.getGioRa() != null ? c.getGioRa().toString() : "--:--");
                    item.put("tongGio", c.getSogioLam());
                    item.put("trangThai", c.getTrangThai());

                    switch (c.getTrangThai()) {
                        case "present":
                            coMat++;
                            break;
                        case "late":
                            diMuon++;
                            break;
                        case "leave":
                            nghiPhep++;
                            break;
                        default:
                            if (!isSunday) {
                                vang++;
                            }
                            break;
                    }
                } else {
                    // Không có bản ghi
                    item.put("gioVao", "--:--");
                    item.put("gioRa", "--:--");
                    item.put("tongGio", 0);
                    item.put("trangThai", "absent");

                    if (!isSunday) {
                        vang++;
                    }
                }

                dsChiTiet.add(item);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("maNV", maNV);
            data.put("hoTen", nv.getHoTen());
            PhongBan pb = pbdao.getByID(nv.getMaPB());
            data.put("tenPB", pb != null ? pb.getTenPB() : "");
            data.put("thangNam", String.format("%02d/%d", thang, nam));
            data.put("coMat", coMat);
            data.put("diMuon", diMuon);
            data.put("vang", vang);
            data.put("nghiPhep", nghiPhep);
            data.put("soNgayCong", coMat + diMuon + nghiPhep);
            data.put("dsChiTiet", dsChiTiet);
            data.put("success", true);

            gson.toJson(data, response.getWriter());

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi khi lấy dữ liệu chi tiết chấm công!");
            gson.toJson(result, response.getWriter());
        }
    }

    private String getThuTrongTuan(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case java.util.Calendar.MONDAY:
                return "Thứ 2";
            case java.util.Calendar.TUESDAY:
                return "Thứ 3";
            case java.util.Calendar.WEDNESDAY:
                return "Thứ 4";
            case java.util.Calendar.THURSDAY:
                return "Thứ 5";
            case java.util.Calendar.FRIDAY:
                return "Thứ 6";
            case java.util.Calendar.SATURDAY:
                return "Thứ 7";
            default:
                return "Chủ nhật";
        }
    }

    private void exportExcelChiTietCC(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maNV = request.getParameter("maNV");
        if (maNV == null || maNV.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã nhân viên");
            return;
        }

        NhanVien nv = nvdao.getById(Integer.parseInt(maNV));
        LocalDate today = LocalDate.now();
        int nam = today.getYear();
        int thang = today.getMonthValue();
        List<ChamCong> dsChamCong = ccdao.getChamCongTheoThang(maNV, thang, nam);

        Map<LocalDate, ChamCong> mapChamCong = dsChamCong.stream()
                .collect(Collectors.toMap(cc -> cc.getNgayLam().toLocalDate(), cc -> cc));

        YearMonth yearMonth = YearMonth.of(nam, thang);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Chi Tiết Chấm Công");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Dữ liệu style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Header
            String[] headers = {"Ngày", "Thứ", "Giờ Vào", "Giờ Ra", "Tổng Giờ", "Trạng Thái"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Tạo style cho từng trạng thái
            CellStyle presentStyle = workbook.createCellStyle();
            presentStyle.cloneStyleFrom(dataStyle);
            presentStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            presentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle lateStyle = workbook.createCellStyle();
            lateStyle.cloneStyleFrom(dataStyle);
            lateStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
            lateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle absentStyle = workbook.createCellStyle();
            absentStyle.cloneStyleFrom(dataStyle);
            absentStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            absentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle leaveStyle = workbook.createCellStyle();
            leaveStyle.cloneStyleFrom(dataStyle);
            leaveStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            leaveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

// Dữ liệu chi tiết
            int rowNum = 1;
            int coMat = 0, diMuon = 0, vang = 0, nghiPhep = 0;
            for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                LocalDate ngay = LocalDate.of(nam, thang, day);
                ChamCong c = mapChamCong.get(ngay);

                Row row = sheet.createRow(rowNum++);
                int col = 0;

                boolean isSunday = ngay.getDayOfWeek() == DayOfWeek.SUNDAY;
                boolean isHoliday = (isSunday || (c != null && "leave".equals(c.getTrangThai()))) && c == null; // ngày nghỉ nguyên dòng

                // Ngày, thứ
                Cell cellNgay = row.createCell(col++);
                cellNgay.setCellValue(String.format("%02d/%02d", day, thang));

                Cell cellThu = row.createCell(col++);
                cellThu.setCellValue(getThuTrongTuan(Date.valueOf(ngay)));

                // Giờ vào, giờ ra, tổng giờ, trạng thái
                Cell cellVao = row.createCell(col++);
                Cell cellRa = row.createCell(col++);
                Cell cellTong = row.createCell(col++);
                Cell cellTrangThai = row.createCell(col++);

                String trangThaiText = "";
                CellStyle styleToUse = dataStyle;

                if (c != null) {
                    BigDecimal sogioLam = c.getSogioLam();
                    cellVao.setCellValue(c.getGioVao() != null ? c.getGioVao().toString() : "--:--");
                    cellRa.setCellValue(c.getGioRa() != null ? c.getGioRa().toString() : "--:--");
                    cellTong.setCellValue(sogioLam != null ? sogioLam.doubleValue() : 0);

                    switch (c.getTrangThai()) {
                        case "present":
                            trangThaiText = "Đi làm";
                            coMat++;
                            styleToUse = presentStyle;
                            break;
                        case "late":
                            trangThaiText = "Đi muộn";
                            diMuon++;
                            styleToUse = lateStyle;
                            break;
                        case "leave":
                            trangThaiText = "Nghỉ phép";
                            nghiPhep++;
                            styleToUse = leaveStyle;
                            break;
                        default:
                            trangThaiText = isSunday ? "Ngày nghỉ" : "Vắng";
                            if (!isSunday) {
                                vang++;
                            }
                            styleToUse = absentStyle;
                            break;
                    }
                } else if (isHoliday) {
                    trangThaiText = "Ngày nghỉ";
                    cellVao.setCellValue("-");
                    cellRa.setCellValue("-");
                    cellTong.setCellValue("-");
                    styleToUse = leaveStyle;
                } else {
                    trangThaiText = "Vắng";
                    cellVao.setCellValue("--:--");
                    cellRa.setCellValue("--:--");
                    cellTong.setCellValue(0);
                    vang++;
                    styleToUse = absentStyle;
                }

                cellTrangThai.setCellValue(trangThaiText);

                // Gán style cho tất cả các ô
                cellNgay.setCellStyle(styleToUse);
                cellThu.setCellStyle(styleToUse);
                cellVao.setCellStyle(styleToUse);
                cellRa.setCellStyle(styleToUse);
                cellTong.setCellStyle(styleToUse);
                cellTrangThai.setCellStyle(styleToUse);
            }
            // Giãn cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Xuất file
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=ChiTietChamCong_" + maNV + ".xlsx");
            try (ServletOutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xuất Excel chi tiết thất bại!");
        }
    }

    private void exportPDFChiTietCC(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maNV = request.getParameter("maNV");
        if (maNV == null || maNV.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã nhân viên");
            return;
        }

        NhanVien nv = nvdao.getById(Integer.parseInt(maNV));
        LocalDate today = LocalDate.now();
        int nam = today.getYear();
        int thang = today.getMonthValue();
        List<ChamCong> dsChamCong = ccdao.getChamCongTheoThang(maNV, thang, nam);

        // Mở PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ChiTietChamCong_" + maNV + ".pdf");
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // ⚙️ Font tiếng Việt
            String fontPath = request.getServletContext().getRealPath("/fonts/UTM Times.ttf");
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 16, com.itextpdf.text.Font.BOLD);

            // Tiêu đề
            Paragraph title = new Paragraph("CHI TIẾT CHẤM CÔNG NHÂN VIÊN: " + nv.getHoTen(), fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            // Thông tin NV
            document.add(new Paragraph("Mã NV: " + nv.getMaNV(), fontNormal));
            document.add(new Paragraph("Chức vụ: " + nv.getChucVu(), fontNormal));
            document.add(new Paragraph("Phòng ban: " + nv.getTenPB(), fontNormal));
            document.add(new Paragraph("Tháng: " + thang + "/" + nam, fontNormal));
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 3, 3, 3, 3, 4});
            String[] headers = {"Ngày", "Thứ", "Giờ Vào", "Giờ Ra", "Tổng Giờ", "Trạng Thái"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Dữ liệu
            Map<LocalDate, ChamCong> mapChamCong = dsChamCong.stream()
                    .collect(Collectors.toMap(cc -> cc.getNgayLam().toLocalDate(), cc -> cc));

            YearMonth yearMonth = YearMonth.of(nam, thang);
            for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                LocalDate ngay = LocalDate.of(nam, thang, day);
                ChamCong c = mapChamCong.get(ngay);

                String thu = getThuTrongTuan(Date.valueOf(ngay));
                String gioVao = "--:--", gioRa = "--:--";
                double tongGio = 0;
                String trangThaiText = "";
                BaseColor bgColor = BaseColor.WHITE;
                boolean isSunday = ngay.getDayOfWeek() == DayOfWeek.SUNDAY;
                if (c != null) {
                    gioVao = c.getGioVao() != null ? c.getGioVao().toString() : "--:--";
                    gioRa = c.getGioRa() != null ? c.getGioRa().toString() : "--:--";
                    tongGio = c.getSogioLam() != null ? c.getSogioLam().doubleValue() : 0;

                    switch (c.getTrangThai()) {
                        case "present":
                            trangThaiText = "Đi làm";
                            bgColor = new BaseColor(200, 255, 200);
                            break;
                        case "late":
                            trangThaiText = "Đi muộn";
                            bgColor = new BaseColor(255, 255, 200);
                            break;
                        case "leave":
                            trangThaiText = "Nghỉ phép";
                            bgColor = new BaseColor(255, 255, 180);
                            break;
                        default:
                            trangThaiText = isSunday ? "Ngày nghỉ" : "Vắng";
                            bgColor = new BaseColor(255, 200, 200);
                            break;
                    }
                } else if (isSunday) {
                    trangThaiText = "Ngày nghỉ";
                    bgColor = new BaseColor(255, 255, 180);
                } else {
                    trangThaiText = "Vắng";
                    bgColor = new BaseColor(255, 200, 200);
                }
                PdfPCell cellNgay = new PdfPCell(new Phrase(String.format("%02d/%02d", day, thang), fontNormal));
                cellNgay.setBackgroundColor(bgColor);
                cellNgay.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellNgay);

                PdfPCell cellThu = new PdfPCell(new Phrase(thu, fontNormal));
                cellThu.setBackgroundColor(bgColor);
                cellThu.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellThu);

                PdfPCell cellVao = new PdfPCell(new Phrase(gioVao, fontNormal));
                cellVao.setBackgroundColor(bgColor);
                cellVao.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellVao);

                PdfPCell cellRa = new PdfPCell(new Phrase(gioRa, fontNormal));
                cellRa.setBackgroundColor(bgColor);
                cellRa.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellRa);

                PdfPCell cellTong = new PdfPCell(new Phrase(String.valueOf(tongGio), fontNormal));
                cellTong.setBackgroundColor(bgColor);
                cellTong.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTong);

                PdfPCell cellTrangThai = new PdfPCell(new Phrase(trangThaiText, fontNormal));
                cellTrangThai.setBackgroundColor(bgColor);
                cellTrangThai.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTrangThai);
            }

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xuất PDF thất bại!");
        } finally {
            document.close();
        }
    }

    private void exportExcelCC(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateStr = request.getParameter("date");
        LocalDate date = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : LocalDate.now();

        String phongBan = request.getParameter("phongban");
        String tenNV = request.getParameter("tenNV");

        List<NhanVien> dsNhanVien = nvdao.searchAdvanced(tenNV, phongBan);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bảng Chấm Công");
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Bảng Chấm Công - Ngày " + date);
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        Row headerRow = sheet.createRow(1);
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] headers = {"Mã NV", "Họ Tên", "Phòng Ban", "Giờ Vào", "Giờ Ra", "Trạng Thái", "Số Ngày Công"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }
        int rowNum = 2;
        for (NhanVien nv : dsNhanVien) {
            List<ChamCong> dsCC = ccdao.getChamCongTheoThang(nv.getMaNV(), date.getMonthValue(), date.getYear());

            int coMat = 0;
            for (ChamCong cc : dsCC) {
                String tt = cc.getTrangThai() == null ? "" : cc.getTrangThai();
                if (tt.equals("present") || tt.equals("late")) {
                    coMat++;
                }
            }

            String gioVao = dsCC.size() > 0 && dsCC.get(0).getGioVao() != null ? dsCC.get(0).getGioVao().toString() : "--:--";
            String gioRa = dsCC.size() > 0 && dsCC.get(0).getGioRa() != null ? dsCC.get(0).getGioRa().toString() : "--:--";
            String trangThai = coMat > 0 ? "Đi làm" : "Vắng";

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(nv.getMaNV());
            row.createCell(1).setCellValue(nv.getHoTen());
            row.createCell(2).setCellValue(nv.getTenPB());
            row.createCell(3).setCellValue(gioVao);
            row.createCell(4).setCellValue(gioRa);

            Cell statusCell = row.createCell(5);
            statusCell.setCellValue(trangThai);

            CellStyle statusStyle = workbook.createCellStyle();
            statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            statusStyle.setFillForegroundColor(trangThai.equals("Đi làm") ? IndexedColors.LIGHT_GREEN.getIndex() : IndexedColors.RED.getIndex());
            statusCell.setCellStyle(statusStyle);

            row.createCell(6).setCellValue(coMat);
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=BangChamCong_" + date + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
