package DAO;

import Model.Luong;
import Model.TrangThaiLuong;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class LuongDAO {

    public LuongDAO() {
    }

    public List<Luong> getAll() {
        List<Luong> list = new ArrayList<>();
        String sql = "SELECT * FROM Luong";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Luong l = mapResultSetToLuong(rs);
                list.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Luong> getByPage(int page, int pageSize) {
        List<Luong> list = new ArrayList<>();
        String sql = "SELECT * FROM Luong ORDER BY MaLuong OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int offset = (page - 1) * pageSize;
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Luong l = mapResultSetToLuong(rs);
                    list.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Luong l) {
        String sql = "INSERT INTO Luong (MaNV, Thang, Nam, LuongCoBan, SoNgayCong, TongPhuCap, Thuong, Phat, TongTangCa, TongKhauTru, TongThuNhap, ThucLinh, TrangThai,GhiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, l.getMaNV());
            ps.setInt(2, l.getThang());
            ps.setInt(3, l.getNam());
            ps.setDouble(4, l.getLuongCoBan());
            ps.setInt(5, l.getSoNgayCong());
            ps.setDouble(6, l.getTongPhuCap());
            ps.setDouble(7, l.getThuong());
            ps.setDouble(8, l.getPhat());
            ps.setDouble(9, l.getTongTangCa());
            ps.setDouble(10, l.getTongKhauTru());
            ps.setDouble(11, l.getTongThuNhap());
            ps.setDouble(12, l.getThucLinh());
            ps.setString(13, l.getTrangThai().name());
            ps.setString(14, l.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(int maLuong, TrangThaiLuong trangThai) {
        String sql = "UPDATE Luong SET TrangThai=? WHERE MaLuong=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai.name());
            ps.setInt(2, maLuong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int maLuong) {
        String sql = "DELETE FROM Luong WHERE MaLuong=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLuong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Luong> searchLuongByThangNam(int thang, int nam) {
        List<Luong> list = new ArrayList<>();
        String sql = "SELECT * FROM Luong WHERE Thang = ? AND Nam = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Luong l = mapResultSetToLuong(rs);
                    list.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Luong mapResultSetToLuong(ResultSet rs) throws SQLException {
        Luong l = new Luong();
        l.setMaLuong(rs.getInt("MaLuong"));
        l.setMaNV(rs.getInt("MaNV"));
        l.setThang(rs.getInt("Thang"));
        l.setNam(rs.getInt("Nam"));
        l.setLuongCoBan(rs.getDouble("LuongCoBan"));
        l.setSoNgayCong(rs.getInt("SoNgayCong"));
        l.setTongPhuCap(rs.getDouble("TongPhuCap"));
        l.setThuong(rs.getDouble("Thuong"));
        l.setPhat(rs.getDouble("Phat"));
        l.setTongTangCa(rs.getDouble("TongTangCa"));
        l.setTongKhauTru(rs.getDouble("TongKhauTru"));
        l.setTongThuNhap(rs.getDouble("TongThuNhap"));
        l.setThucLinh(rs.getDouble("ThucLinh"));
        l.setTrangThai(TrangThaiLuong.fromString(rs.getString("TrangThai")));
        l.setGhiChu(rs.getString("GhiChu"));
        return l;
    }

    public Luong getByMaNV(int maNV) {
        String sql = "SELECT * FROM Luong WHERE MaNV = ? ORDER BY Nam DESC, Thang DESC"; // Lấy mới nhất
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLuong(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Luong getByID(int maLuong) {
        String sql = "SELECT * FROM Luong WHERE MaLuong = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLuong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLuong(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean update(Luong l) {
    String sql = "UPDATE Luong SET Thang=?, Nam=?, LuongCoBan=?, TongPhuCap=?, Thuong=?, TongKhauTru=?, "
               + "TongThuNhap=?, ThucLinh=?, TrangThai=?, GhiChu=? WHERE MaLuong=?";
    try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, l.getThang());
        ps.setInt(2, l.getNam());
        ps.setDouble(3, l.getLuongCoBan());
        ps.setDouble(4, l.getTongPhuCap());
        ps.setDouble(5, l.getThuong());
        ps.setDouble(6, l.getTongKhauTru());
        ps.setDouble(7, l.getTongThuNhap());
        ps.setDouble(8, l.getThucLinh());
        ps.setString(9, l.getTrangThai().name());
        ps.setString(10, l.getGhiChu());
        ps.setInt(11, l.getMaLuong());

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


}
