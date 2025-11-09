package DAO;

import Model.ChiTietLuong;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class ChiTietLuongDAO {

    public ChiTietLuongDAO() {}

    public List<ChiTietLuong> getAll() {
        List<ChiTietLuong> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietLuong";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToChiTietLuong(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ChiTietLuong getById(int maChiTietLuong) {
        String sql = "SELECT * FROM ChiTietLuong WHERE MaChiTietLuong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChiTietLuong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChiTietLuong(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ChiTietLuong> getByMaLuong(int maLuong) {
        List<ChiTietLuong> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietLuong WHERE MaLuong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLuong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToChiTietLuong(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(ChiTietLuong ctl) {
        String sql = "INSERT INTO ChiTietLuong ( LoaiChiTiet, MoTa, SoTien) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(2, ctl.getLoaiChiTiet());
            ps.setString(3, ctl.getMoTa());
            ps.setBigDecimal(4, ctl.getSoTien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ChiTietLuong ctl) {
        String sql = "UPDATE ChiTietLuong SET MaLuong=?, LoaiChiTiet=?, MoTa=?, SoTien=? WHERE MaChiTietLuong=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ctl.getMaLuong());
            ps.setString(2, ctl.getLoaiChiTiet());
            ps.setString(3, ctl.getMoTa());
            ps.setBigDecimal(4, ctl.getSoTien());
            ps.setInt(5, ctl.getMaChiTietLuong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int maChiTietLuong) {
        String sql = "DELETE FROM ChiTietLuong WHERE MaChiTietLuong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChiTietLuong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ChiTietLuong mapResultSetToChiTietLuong(ResultSet rs) throws SQLException {
        ChiTietLuong ctl = new ChiTietLuong();
        ctl.setMaChiTietLuong(rs.getInt("MaChiTietLuong"));
        ctl.setMaLuong(rs.getInt("MaLuong"));
        ctl.setLoaiChiTiet(rs.getString("LoaiChiTiet"));
        ctl.setMoTa(rs.getString("MoTa"));
        ctl.setSoTien(rs.getBigDecimal("SoTien"));
        return ctl;
    }
}
