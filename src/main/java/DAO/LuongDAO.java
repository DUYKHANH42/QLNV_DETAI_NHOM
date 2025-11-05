/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Luong;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

/**
 *
 * @author PC
 */
public class LuongDAO {

    public double getTongLuongThucLinh(int thang, int nam) {
        String sql = "SELECT SUM(ISNULL(ThucLinh,0)) AS TongLuong FROM LUONG WHERE Thang=? AND Nam=?";
        double tong = 0;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tong = rs.getDouble("TongLuong"); // đúng alias
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }

    public Luong getLuongByMaNV_ThangNam(String maNV, int thang, int nam) {
        Luong luong = null;
        String sql = "SELECT nv.MaNV, nv.HoTen, pb.TenPhong, "
                + "ISNULL(l.LuongCoBan, nv.LuongCoBan) AS LuongCoBan, l.TongThuNhap, "
                + "ISNULL(cc.SoNgayCong, 0) AS SoNgayCong "
                + "FROM NHANVIEN nv "
                + "JOIN PHONGBAN pb ON nv.MaPhong = pb.MaPhong "
                + "LEFT JOIN LUONG l ON l.MaNV = nv.MaNV AND l.Thang = ? AND l.Nam = ? "
                + "LEFT JOIN ( "
                + "   SELECT MaNV, COUNT(*) AS SoNgayCong "
                + "   FROM CHAMCONG "
                + "   WHERE MONTH(NgayLam) = ? AND YEAR(NgayLam) = ? "
                + "         AND TrangThai = N'Đã Chấm Công' "
                + "   GROUP BY MaNV "
                + ") cc ON cc.MaNV = nv.MaNV "
                + "WHERE nv.MaNV = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ps.setInt(3, thang);
            ps.setInt(4, nam);
            ps.setString(5, maNV);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int soNgayCong = rs.getInt("SoNgayCong");
                BigDecimal luongCoBan = rs.getBigDecimal("LuongCoBan");
                BigDecimal tongThuNhap = rs.getBigDecimal("TongThuNhap"); // có thể null nếu chưa tính

                luong = new Luong(maNV, thang, nam, soNgayCong, luongCoBan, tongThuNhap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return luong;
    }

    public List<Luong> getAll() {
        List<Luong> list = new ArrayList<>();
        String sql = "SELECT MaNV, Thang, Nam, SoNgayCong, LuongCoBan, ThucLinh AS TongLuong FROM LUONG";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                int thang = rs.getInt("Thang");
                int nam = rs.getInt("Nam");
                int soNgayCong = rs.getInt("SoNgayCong");
                BigDecimal luongCoBan = rs.getBigDecimal("LuongCoBan");
                BigDecimal thucLinh = rs.getBigDecimal("ThucLinh");

                Luong l = new Luong(maNV, thang, nam, soNgayCong, luongCoBan, thucLinh);
                list.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Luong getLuongByNV(String maNV, int thang, int nam) {
        String sql = "SELECT SoNgayCong, LuongCoBan, ThucLinh FROM LUONG WHERE MaNV=? AND Thang=? AND Nam=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ps.setInt(2, thang);
            ps.setInt(3, nam);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int soNgayCong = rs.getInt("SoNgayCong");
                BigDecimal luongCoBan = rs.getBigDecimal("LuongCoBan");
                BigDecimal thucLinh = rs.getBigDecimal("ThucLinh");
                Luong l = new Luong(maNV, thang, nam, soNgayCong, luongCoBan, thucLinh);
                return l;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean updateSoNgayCong(String maNV, int thang, int nam, int soNgayCong) {
        String sql = "UPDATE LUONG SET SoNgayCong = ? WHERE MaNV = ? AND Thang = ? AND Nam = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soNgayCong);
            ps.setString(2, maNV);
            ps.setInt(3, thang);
            ps.setInt(4, nam);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Luong> getByMaNV(String maNV) {
        List<Luong> list = new ArrayList<>();
        String sql = "SELECT MaNV, Thang, Nam, SoNgayCong, LuongCoBan,Thuong,ThucLinh,TongThuNhap"
                + " FROM LUONG WHERE MaNV=? ORDER BY Nam DESC, Thang DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int thang = rs.getInt("Thang");
                    int nam = rs.getInt("Nam");
                    int soNgayCong = rs.getInt("SoNgayCong");
                    BigDecimal luongCoBan = rs.getBigDecimal("LuongCoBan");   
                    BigDecimal thuong = rs.getBigDecimal("Thuong");
                    BigDecimal thucLinh = rs.getBigDecimal("ThucLinh");
                      BigDecimal tongThuNhap = rs.getBigDecimal("TongThuNhap");
                    Luong l = new Luong(maNV, thang, nam, soNgayCong,luongCoBan,thuong,thucLinh,tongThuNhap);
                    list.add(l);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
