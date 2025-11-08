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
            case "exportExcel":
                exportExcelPhongBan(request, response);
                break;
            case "exportPDF":
                exportPDF(request, response);
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

    private void exportExcelPhongBan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PhongBan> list = pbdao.getAllPhongBan();
        int tongPhongBan = pbdao.countAllPhongBan();
        int tongNhanVien = nvdao.countAllNhanVien();
        double tbNhanVienPB = tongPhongBan > 0 ? (double) tongNhanVien / tongPhongBan : 0;
        String phongDongNhat = nvdao.getPhongBanDongNhat();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách phòng ban");

            // 🎨 Style header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // 🎨 Header đặc biệt cho “Mã PB”, “Tên PB”
            CellStyle blueHeaderStyle = workbook.createCellStyle();
            blueHeaderStyle.cloneStyleFrom(headerStyle);
            blueHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            blueHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 🎨 Style dữ liệu
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 🎨 Style tổng kết (footer)
            CellStyle summaryStyle = workbook.createCellStyle();
            Font summaryFont = workbook.createFont();
            summaryFont.setBold(true);
            summaryStyle.setFont(summaryFont);
            summaryStyle.setBorderTop(BorderStyle.THIN);
            summaryStyle.setBorderBottom(BorderStyle.THIN);
            summaryStyle.setBorderLeft(BorderStyle.THIN);
            summaryStyle.setBorderRight(BorderStyle.THIN);
            summaryStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
            summaryStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 🧱 Header
            String[] headers = {"Mã PB", "Tên Phòng Ban", "Trưởng Phòng", "Số Nhân Viên", "Mô Tả"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(i <= 1 ? blueHeaderStyle : headerStyle);
            }

            // 🧾 Dữ liệu phòng ban
            int rowNum = 1;
            for (PhongBan pb : list) {
                Row row = sheet.createRow(rowNum++);
                int col = 0;

                row.createCell(col++).setCellValue(pb.getMaPB());
                row.createCell(col++).setCellValue(pb.getTenPB());

                // ✅ Lấy tên trưởng phòng (nếu có)
                String truongPhongTen = "";
                if (pb.getTruongPhong() != null) {
                    NhanVien nv = nvdao.getById(pb.getTruongPhong());
                    truongPhongTen = nv != null ? nv.getHoTen() : "(Không có)";
                }
                row.createCell(col++).setCellValue(truongPhongTen);

                // ✅ Đếm số nhân viên trong phòng
                int soNhanVien = nvdao.countNhanVienTheoPhong(pb.getMaPB());
                row.createCell(col++).setCellValue(soNhanVien);

                row.createCell(col++).setCellValue(pb.getMoTa() == null ? "" : pb.getMoTa());

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // 📊 Thêm thống kê tổng hợp ở cuối
            int summaryStart = rowNum + 1;
            String[][] summaries = {
                {"Tổng Phòng Ban", String.valueOf(tongPhongBan)},
                {"Tổng Nhân Viên", String.valueOf(tongNhanVien)},
                {"TB Nhân Viên/PB", String.format("%.1f", tbNhanVienPB)},
                {"Phòng Ban Đông Nhất", phongDongNhat}
            };

            for (String[] s : summaries) {
                Row r = sheet.createRow(summaryStart++);
                Cell c1 = r.createCell(0);
                Cell c2 = r.createCell(1);
                c1.setCellValue(s[0]);
                c2.setCellValue(s[1]);
                c1.setCellStyle(summaryStyle);
                c2.setCellStyle(summaryStyle);
            }

            // ✨ Giãn độ rộng cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 📥 Xuất file
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=DanhSachPhongBan.xlsx");

            try (ServletOutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xuất Excel thất bại!");
        }
    }

    private void exportPDF(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
    String maPB = request.getParameter("id");
    PhongBan pb = pbdao.getByID(maPB);
    List<NhanVien> dsNhanVien = nvdao.getNhanVienTheoPhongBan(maPB);

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=PhongBan_" + maPB + ".pdf");

    Document document = new Document(PageSize.A4.rotate());
    try {
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // ⚙️ Tạo font Unicode có dấu tiếng Việt
        String fontPath = request.getServletContext().getRealPath("/fonts/UTM Times.ttf");
        BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
        com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);

        // 🏷️ Tiêu đề
        Paragraph title = new Paragraph("CHI TIẾT PHÒNG BAN: " + pb.getTenPB(), fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // 📋 Thông tin phòng ban
        document.add(new Paragraph("Mã phòng ban: " + pb.getMaPB(), fontNormal));
        document.add(new Paragraph("Trưởng phòng: " + pb.getTruongPhong(), fontNormal));
        document.add(new Paragraph("Tổng nhân viên: " + dsNhanVien.size(), fontNormal));
        document.add(Chunk.NEWLINE);

        // 🧾 Bảng danh sách nhân viên
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 4, 6, 5, 8, 6, 5});

        // 🔹 Header
        String[] headers = {"STT", "Mã NV", "Họ Tên", "Chức Vụ", "Email", "SĐT", "Trạng Thái"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fontBold));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // 🔸 Dữ liệu
        int stt = 1;
        for (NhanVien nv : dsNhanVien) {
            table.addCell(new Phrase(String.valueOf(stt++), fontNormal));
            table.addCell(new Phrase(nv.getMaNV(), fontNormal));
            table.addCell(new Phrase(nv.getHoTen(), fontNormal));
            table.addCell(new Phrase(nv.getChucVu(), fontNormal));
            table.addCell(new Phrase(nv.getEmail(), fontNormal));
            table.addCell(new Phrase(nv.getSDT(), fontNormal));

            PdfPCell trangThaiCell = new PdfPCell(new Phrase(nv.getTrangThai(), fontNormal));
            if (nv.getTrangThai().equalsIgnoreCase("Đang làm")) {
                trangThaiCell.setBackgroundColor(new BaseColor(200, 255, 200)); // xanh nhạt
            } else {
                trangThaiCell.setBackgroundColor(new BaseColor(255, 200, 200)); // đỏ nhạt
            }
            table.addCell(trangThaiCell);
        }

        document.add(table);

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        document.close();
    }
}

}
