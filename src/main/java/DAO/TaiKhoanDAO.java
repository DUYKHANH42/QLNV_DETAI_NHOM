/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

/**
 *
 * @author PC
 */
public class TaiKhoanDAO {

    public TaiKhoan DangNhap(String user, String pass) throws Exception {
        String sql = "SELECT* FROM TAIKHOAN WHERE TenDangNhap=? AND MatKhau=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getInt("MaNV"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("VaiTro")
                );
            }
        }
        return null;
    }

    public List<TaiKhoan> getAll() throws Exception {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getInt("MaNV"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("VaiTro")
                );
                list.add(tk);
            }
        }
        return list;
    }

    public TaiKhoan getByID(String maNV) {
        String sql = "SELECT * FROM TAIKHOAN WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getInt("MaNV"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getString("VaiTro")
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insert(TaiKhoan tk) {
    String checkSql = "SELECT COUNT(*) FROM TaiKhoan WHERE TenDangNhap = ?";
    String insertSql = "INSERT INTO TaiKhoan (MaNV, TenDangNhap, MatKhau, VaiTro) VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection()) {

        // 1️⃣ Kiểm tra tồn tại
        try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
            psCheck.setString(1, tk.getTenDangNhap());
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;  
            }
        }

        // 2️⃣ Insert
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setInt(1, tk.getMaNV());
            ps.setString(2, tk.getTenDangNhap());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getVaiTro());
            return ps.executeUpdate() > 0;
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return false;
}


    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TAIKHOAN SET TenDangNhap=?, MatKhau=?, VaiTro=? WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro());
            ps.setInt(4, tk.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maNV) {
        String sql = "DELETE FROM TAIKHOAN WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    // Lưu token vào DB
public void saveRememberToken(int maNV, String token) {
    String sql = "UPDATE TAIKHOAN SET rememberToken=? WHERE MaNV=?";
    try (java.sql.Connection conn = util.DBConnection.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, token);
        ps.setInt(2, maNV);
        ps.executeUpdate();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

// Lấy user theo token
public TaiKhoan getByRememberToken(String username, String token) {
    String sql = "SELECT * FROM TAIKHOAN WHERE TenDangNhap=? AND rememberToken=?";
    try (java.sql.Connection conn = util.DBConnection.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, token);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new TaiKhoan(
                rs.getInt("MaNV"),
                rs.getString("TenDangNhap"),
                rs.getString("MatKhau"),
                rs.getString("VaiTro")
            );
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

}
