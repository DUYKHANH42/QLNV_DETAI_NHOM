package DAO;

import Model.NhanVien;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.*, pb.TenPB "
                + "FROM NhanVien nv "
                + "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB";
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NhanVien> getByPage(int pageIndex, int pageSize) {
        List<NhanVien> list = new ArrayList<>();
        int offset = (pageIndex - 1) * pageSize;
        String sql = "SELECT nv.*, pb.TenPB "
                + "FROM NhanVien nv "
                + "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB "
                + "ORDER BY nv.MaNV "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, offset);
            ps.setInt(2, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToNhanVien(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public NhanVien getById(int maNV) {
        String sql = "SELECT nv.*, pb.TenPB "
                + "FROM NhanVien nv "
                + "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB "
                + "WHERE nv.MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien(HoTen, NgaySinh, GioiTinh, SDT, DiaChi, ChucVu, NgayVaoLam, MaPB, TrangThai, Email) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int index = 1;
            ps.setString(index++, nv.getHoTen());
            ps.setDate(index++, nv.getNgaySinh() != null ? new java.sql.Date(nv.getNgaySinh().getTime()) : null);
            ps.setString(index++, nv.getGioiTinh());
            ps.setString(index++, nv.getSDT());
            ps.setString(index++, nv.getDiaChi());
            ps.setString(index++, nv.getChucVu());
            ps.setDate(index++, nv.getNgayVaoLam() != null ? new java.sql.Date(nv.getNgayVaoLam().getTime()) : null);
            ps.setString(index++, nv.getMaPB());
            ps.setString(index++, nv.getTrangThai() != null ? nv.getTrangThai() : "Đang làm");
            ps.setString(index++, nv.getEmail());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int maNV = rs.getInt(1);
                    nv.setMaNV(String.valueOf(maNV));
                    return maNV;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, NgaySinh=?, GioiTinh=?, SDT=?, DiaChi=?, ChucVu=?, NgayVaoLam=?, MaPB=?, TrangThai=?, Email=? "
                + "WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            setPreparedStatementFromNhanVien(ps, nv, true);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maNV) throws SQLException {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Nếu lỗi do ràng buộc khóa ngoại thì ném lỗi đặc biệt để servlet xử lý
            if (e.getErrorCode() == 1451 || e.getErrorCode() == 547) { // 1451 MySQL, 547 SQLServer
                throw new SQLException("foreign_key_violation");
            }
            throw e;
        }
    }

    public List<NhanVien> search(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.*, pb.TenPB "
                + "FROM NhanVien nv "
                + "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB "
                + "WHERE nv.MaNV LIKE ? OR nv.HoTen LIKE ? OR nv.MaPB LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToNhanVien(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NhanVien> searchAdvanced(String hoten, String phongban) {
        List<NhanVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT nv.*, pb.TenPB "
                + "FROM NhanVien nv "
                + "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB "
                + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (hoten != null && !hoten.isEmpty()) {
            sql.append(" AND nv.HoTen LIKE ? ");
            params.add("%" + hoten + "%");
        }
        if (phongban != null && !phongban.isEmpty()) {
            sql.append(" AND nv.MaPB = ? ");
            params.add(phongban);
        }

        sql.append(" ORDER BY nv.MaNV ");

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToNhanVien(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    // Đếm tổng nhân viên

    public int countAllNhanVien() {
        String sql = "SELECT COUNT(*) FROM NhanVien";
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int demNhanVienTheoPhong(int maPB) {
    String sql = "SELECT COUNT(*) FROM NhanVien WHERE MaPB = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, maPB);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}


// Đếm số nhân viên theo phòng
    public int countNhanVienTheoPhong(int maPB) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE MaPB = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPB);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

// Lấy tên trưởng phòng của một phòng
    public String getTruongPhongTheoPhong(int maPB) {
        String sql = "SELECT TOP 1 HoTen FROM NhanVien WHERE MaPB = ? AND ChucVu = N'Trưởng phòng'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPB);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("HoTen");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Chưa có";
    }

// Lấy phòng ban đông nhân viên nhất
    public String getPhongBanDongNhat() {
        String sql = "SELECT TOP 1 pb.TenPB, COUNT(nv.MaNV) AS SoLuong "
                + "FROM NhanVien nv "
                + "JOIN PhongBan pb ON nv.MaPB = pb.MaPB "
                + "GROUP BY pb.TenPB "
                + "ORDER BY SoLuong DESC";
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return "" + rs.getInt("SoLuong") + "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }
    public List<NhanVien> getNhanVienTheoPhongBan(String maPB) {
    List<NhanVien> list = new ArrayList<>();
    String sql = "SELECT nv.*, pb.TenPB "
               + "FROM NhanVien nv "
               + "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB "
               + "WHERE nv.MaPB = ? "
               + "ORDER BY nv.MaNV";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maPB);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        return new NhanVien(
                rs.getString("MaNV"),
                rs.getString("HoTen"),
                rs.getDate("NgaySinh"),
                rs.getString("GioiTinh"),
                rs.getString("SDT"),
                rs.getString("DiaChi"),
                rs.getString("ChucVu"),
                rs.getDate("NgayVaoLam"),
                rs.getString("MaPB"),
                rs.getString("TenPB"),
                rs.getString("Email"),
                rs.getString("TrangThai")
        );

    }

    private void setPreparedStatementFromNhanVien(PreparedStatement ps, NhanVien nv, boolean isUpdate) throws SQLException {
        // Nếu là insert thì không có MaNV
        int index = 1;

        ps.setString(index++, nv.getHoTen());

        if (nv.getNgaySinh() != null) {
            ps.setDate(index++, new java.sql.Date(nv.getNgaySinh().getTime()));
        } else {
            ps.setNull(index++, Types.DATE);
        }

        ps.setString(index++, nv.getGioiTinh());
        ps.setString(index++, nv.getSDT());
        ps.setString(index++, nv.getDiaChi());
        ps.setString(index++, nv.getChucVu());

        if (nv.getNgayVaoLam() != null) {
            ps.setDate(index++, new java.sql.Date(nv.getNgayVaoLam().getTime()));
        } else {
            ps.setNull(index++, Types.DATE);
        }

        ps.setString(index++, nv.getMaPB());
//        ps.setString(index++, nv.getMaTK());
        ps.setString(index++, nv.getTrangThai());
        ps.setString(index++, nv.getEmail());

        // Nếu là update thì thêm MaNV cuối cùng
        if (isUpdate) {
            ps.setString(index++, nv.getMaNV());
        }
    }

}
