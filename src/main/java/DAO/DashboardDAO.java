/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.*;
import util.DBConnection;

public class DashboardDAO {

    // Tổng quan dashboard
    public Map<String, Object> getDashboardTongQuan() {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT * FROM vw_Dashboard_TongQuan";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                map.put("tongNV", rs.getInt("TongNhanVien"));
                map.put("dangLam", rs.getInt("NhanVienDangLam"));
                map.put("soPhongBan", rs.getInt("TongPhongBan"));
                map.put("tyLeChuyenCan", rs.getDouble("TyleChuyenCan"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // Nhân viên mới nhất
    public List<Map<String, Object>> getNhanVienMoiNhat() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT * FROM vw_NhanVienMoiNhat";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("maNV", rs.getString("MaNV"));
                map.put("hoTen", rs.getString("HoTen"));
                map.put("phongBan", rs.getString("PhongBan"));
                map.put("chucVu", rs.getString("ChucVu"));
                map.put("email", rs.getString("Email"));
                map.put("trangThai", rs.getString("TrangThai"));
                list.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Nhân viên theo phòng ban
    public Map<String, Integer> getNhanVienTheoPhongBan() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT * FROM vw_NhanVienTheoPhongBan";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("TenPB"), rs.getInt("SoLuongNhanVien"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    public Map<String, Integer> getTyLeGioiTinh() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT * FROM vw_TyLeGioiTinh";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("GioiTinh"), rs.getInt("SoLuong"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
