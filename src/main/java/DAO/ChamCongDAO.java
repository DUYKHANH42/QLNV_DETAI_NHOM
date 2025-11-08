/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ChamCong;
import Model.NhanVien;
import java.math.BigDecimal;
import util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author PC
 */
public class ChamCongDAO {

    public List<ChamCong> getAll() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT MaCC, MaNV, NgayLam, TrangThai, GioVao, GioRa FROM CHAMCONG";

        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ChamCong cc = new ChamCong(
                        rs.getInt("MaCC"),
                        rs.getString("MaNV"),
                        rs.getDate("NgayLam"),
                        rs.getString("TrangThai"),
                        rs.getTime("GioVao"),
                        rs.getTime("GioRa")
                );
                list.add(cc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

   public boolean insert(ChamCong cc) {
    String sql = "INSERT INTO CHAMCONG(MaNV, NgayLam, TrangThai, GioVao, GioRa, SoGioLam) "
               + "VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, cc.getMaNV());
        ps.setDate(2, new java.sql.Date(cc.getNgayLam().getTime()));
        ps.setString(3, cc.getTrangThai());
        ps.setTime(4, cc.getGioVao());
        ps.setTime(5, cc.getGioRa());

        // Tính số giờ làm
        double soGioLam = tinhSoGioLam(cc.getGioVao(), cc.getGioRa());
        ps.setBigDecimal(6, BigDecimal.valueOf(soGioLam));

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean chamCongNhanVien(String maNV, LocalDate ngayLam) {
        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra đã có chưa
            String checkSql = "SELECT COUNT(*) FROM ChamCong WHERE MaNV = ? AND NgayLam = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setString(1, maNV);
                psCheck.setDate(2, Date.valueOf(ngayLam));
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // đã có => bỏ qua
                    }
                }
            }

            // Thêm mới
            String insertSql = "INSERT INTO ChamCong(MaNV, NgayLam, TrangThai, GioVao, GioRa) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setString(1, maNV);
                psInsert.setDate(2, Date.valueOf(ngayLam));
                psInsert.setString(3, "Đi làm");
                psInsert.setTime(4, Time.valueOf(LocalTime.now()));
                psInsert.setTime(5, null);
                return psInsert.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

  
public boolean update(ChamCong cc) {
    String sql = "UPDATE CHAMCONG SET NgayLam=?, TrangThai=?, GioVao=?, GioRa=?, SoGioLam=? "
               + "WHERE MaCC=?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, new java.sql.Date(cc.getNgayLam().getTime()));
        ps.setString(2, cc.getTrangThai());
        ps.setTime(3, cc.getGioVao());
        ps.setTime(4, cc.getGioRa());

        // Tính số giờ làm
        double soGioLam = tinhSoGioLam(cc.getGioVao(), cc.getGioRa());
        ps.setBigDecimal(5, BigDecimal.valueOf(soGioLam));

        ps.setInt(6, cc.getMaCC());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    public ChamCong getByMaNVAndNgay(String maNV, Date ngay) {
    String sql = "SELECT * FROM CHAMCONG WHERE MaNV=? AND NgayLam=?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maNV);
        ps.setDate(2, ngay);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ChamCong cc = new ChamCong();
            cc.setMaCC(rs.getInt("MaCC"));
            cc.setMaNV(rs.getString("MaNV"));
            cc.setNgayLam(rs.getDate("NgayLam"));
            cc.setTrangThai(rs.getString("TrangThai"));
            cc.setGioVao(rs.getTime("GioVao"));
            cc.setGioRa(rs.getTime("GioRa"));
            return cc;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    public boolean delete(int maCC) {
        String sql = "DELETE FROM CHAMCONG WHERE MaCC=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maCC);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ChamCong getById(int maCC) {
        String sql = "SELECT MaCC, MaNV, NgayLam, TrangThai, GioVao, GioRa FROM CHAMCONG WHERE MaCC=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maCC);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ChamCong(
                        rs.getInt("MaCC"),
                        rs.getString("MaNV"),
                        rs.getDate("NgayLam"),
                        rs.getString("TrangThai"),
                        rs.getTime("GioVao"),
                        rs.getTime("GioRa")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countNgayCong(String maNV, int thang, int nam) {
        String sql = "SELECT COUNT(*) AS SoNgayCong "
                + "FROM CHAMCONG "
                + "WHERE MaNV = ? AND MONTH(NgayLam) = ? AND YEAR(NgayLam) = ? "
                + "AND TrangThai = N'present'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoNgayCong");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ChamCong> getByMaNV(String maNV) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT * FROM CHAMCONG WHERE MaNV=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChamCong cc = new ChamCong(
                        rs.getInt("MaCC"),
                        rs.getString("MaNV"),
                        rs.getDate("NgayLam"),
                        rs.getString("TrangThai"),
                        rs.getTime("GioVao"),
                        rs.getTime("GioRa")
                );
                list.add(cc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ChamCong> searchChamCongByMaNVList(List<String> maNVList, Date ngayLam) {
        List<ChamCong> list = new ArrayList<>();
        if (maNVList == null || maNVList.isEmpty()) {
            return list;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM ChamCong WHERE MaNV IN ("
        );
        for (int i = 0; i < maNVList.size(); i++) {
            sql.append("?");
            if (i < maNVList.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        if (ngayLam != null) {
            sql.append(" AND NgayLam = ?");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (String ma : maNVList) {
                ps.setString(idx++, ma);
            }
            if (ngayLam != null) {
                ps.setDate(idx, new java.sql.Date(ngayLam.getTime()));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChamCong cc = new ChamCong(
                        rs.getInt("MaCC"),
                        rs.getString("MaNV"),
                        rs.getDate("NgayLam"),
                        rs.getString("TrangThai"),
                        rs.getTime("GioVao"),
                        rs.getTime("GioRa")
                );
                list.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Chuyển List<ChamCong> -> Map<MaNV, ChamCong> để JSP dùng
    public Map<String, ChamCong> toMap(List<ChamCong> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        ChamCong::getMaNV,
                        cc -> cc,
                        (existing, replacement) -> replacement
                ));
    }

    // Đếm tổng nhân viên đã chấm công trong tháng/năm
    public int countDaChamCong(int thang, int nam) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT MaNV) FROM ChamCong "
                + "WHERE MONTH(NgayLam) = ? AND YEAR(NgayLam) = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm số NV đi muộn (ví dụ GioVao > '08:00')
    public int countDiMuon(int thang, int nam) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChamCong WHERE MONTH(NgayLam)=? AND YEAR(NgayLam)=? AND GioVao > '08:00'";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public boolean daChamCong(String maNV, LocalDate ngay) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChamCong WHERE MaNV=? AND NgayLam=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setDate(2, Date.valueOf(ngay));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Đếm số NV vắng mặt (TrangThai = 'Vắng')
    public Map<LocalDate, Integer> getSoVangMatTheoNgay(List<NhanVien> dsNhanVien, List<LocalDate> danhSachNgayLam) throws SQLException {
        Map<LocalDate, Integer> soVangMatTheoNgay = new HashMap<>();
        int tongNhanVien = dsNhanVien.size();

        for (LocalDate ngay : danhSachNgayLam) {
            int daChamCong = 0;
            for (NhanVien nv : dsNhanVien) {
                if (daChamCong(nv.getMaNV(), ngay)) {
                    daChamCong++;
                }
            }
            int vangMat = tongNhanVien - daChamCong;
            soVangMatTheoNgay.put(ngay, vangMat);
        }

        return soVangMatTheoNgay;
    }
    public List<ChamCong> getChamCongTheoThang(String maNV, int thang, int nam) {
    List<ChamCong> list = new ArrayList<>();
    String sql = "SELECT MaCC, MaNV, NgayLam, TrangThai, GioVao, GioRa, SoGioLam "
               + "FROM ChamCong "
               + "WHERE MaNV = ? AND MONTH(NgayLam) = ? AND YEAR(NgayLam) = ? "
               + "ORDER BY NgayLam ASC";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maNV);
        ps.setInt(2, thang);
        ps.setInt(3, nam);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ChamCong cc = new ChamCong();
            cc.setMaCC(rs.getInt("MaCC"));
            cc.setMaNV(rs.getString("MaNV"));
            cc.setNgayLam(rs.getDate("NgayLam"));
            cc.setTrangThai(rs.getString("TrangThai"));
            cc.setGioVao(rs.getTime("GioVao"));
            cc.setGioRa(rs.getTime("GioRa"));
            cc.setSogioLam(rs.getBigDecimal("SoGioLam"));
            list.add(cc);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

    public double tinhSoGioLam(Time gioVao, Time gioRa) {
    if (gioVao == null || gioRa == null) {
        return 0;
    }
    long diffMillis = gioRa.getTime() - gioVao.getTime()-3600000;
    return diffMillis / 3600000.0;
}
}
