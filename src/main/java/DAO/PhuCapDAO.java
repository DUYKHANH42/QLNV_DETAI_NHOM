package DAO;

import Model.PhuCap;
import java.math.BigDecimal;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhuCapDAO {

    // Lấy tất cả phụ cấp
    public List<PhuCap> getAllPhuCap() {
        List<PhuCap> list = new ArrayList<>();
        String sql = "SELECT MaPC, MaNV, Thang, Nam, LoaiPhuCap, SoTien FROM PHUCAP";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new PhuCap(
                        rs.getString("MaPC"),
                        rs.getString("MaNV"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getString("LoaiPhuCap"),
                        rs.getDouble("SoTien")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm phụ cấp
    public boolean insertPhuCap(PhuCap pc) {
        String sql = "INSERT INTO PHUCAP(MaPC, MaNV, Thang, Nam, LoaiPhuCap, SoTien) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pc.getMaPC());
            ps.setString(2, pc.getMaNV());
            ps.setInt(3, pc.getThang());
            ps.setInt(4, pc.getNam());
            ps.setString(5, pc.getLoaiPhuCap());
            ps.setDouble(6, pc.getSoTien());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật phụ cấp
    public boolean updatePhuCap(PhuCap pc) {
        String sql = "UPDATE PHUCAP SET MaNV=?, Thang=?, Nam=?, LoaiPhuCap=?, SoTien=? WHERE MaPC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pc.getMaNV());
            ps.setInt(2, pc.getThang());
            ps.setInt(3, pc.getNam());
            ps.setString(4, pc.getLoaiPhuCap());
            ps.setDouble(5, pc.getSoTien());
            ps.setString(6, pc.getMaPC());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa phụ cấp
    public boolean deletePhuCap(String maPC) {
        String sql = "DELETE FROM PHUCAP WHERE MaPC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPC);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<PhuCap> getPhuCapByMaNV(String maNV, int thang, int nam) {
    List<PhuCap> list = new ArrayList<>();
    String sql = "SELECT MaPC, MaNV, Thang, Nam, LoaiPhuCap, SoTien " +
                 "FROM PHUCAP " +
                 "WHERE MaNV = ? AND Thang = ? AND Nam = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, maNV);
        ps.setInt(2, thang);
        ps.setInt(3, nam);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PhuCap(
                        rs.getString("MaPC"),
                        rs.getString("MaNV"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getString("LoaiPhuCap"),
                        rs.getDouble("SoTien")
                ));
            }
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return list;
}
    public BigDecimal getTongPhuCapByMaNV(String maNV, int thang, int nam) {
    BigDecimal tongPhuCap = BigDecimal.ZERO;

    String sql = "SELECT ISNULL(SUM(SoTien),0) AS TongPhuCap " +
                 "FROM PHUCAP " +
                 "WHERE MaNV = ? AND Thang = ? AND Nam = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maNV);
        ps.setInt(2, thang);
        ps.setInt(3, nam);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                tongPhuCap = rs.getBigDecimal("TongPhuCap");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return tongPhuCap != null ? tongPhuCap : BigDecimal.ZERO;
}

}
