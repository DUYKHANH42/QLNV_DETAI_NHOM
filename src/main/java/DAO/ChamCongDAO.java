/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ChamCong;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO CHAMCONG(MaNV, NgayLam, TrangThai, GioVao, GioRa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cc.getMaNV());
            ps.setDate(2, new java.sql.Date(cc.getNgayLam().getTime()));
            ps.setString(3, cc.getTrangThai());
            ps.setTime(4, cc.getGioVao());
            ps.setTime(5, cc.getGioRa());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ChamCong cc) {
        String sql = "UPDATE CHAMCONG SET NgayLam=?, TrangThai=? WHERE MaCC=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(cc.getNgayLam().getTime()));
            ps.setString(2, cc.getTrangThai());
            ps.setInt(3, cc.getMaCC());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
                + "AND TrangThai = N'Đã Chấm Công'";
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

    public ChamCong getByNgay(String maNV, java.sql.Date ngayLam) {
        String sql = "SELECT MaCC, MaNV, NgayLam, TrangThai, GioVao, GioRa "
                + "FROM CHAMCONG WHERE MaNV=? AND NgayLam=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ps.setDate(2, ngayLam);
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

}
