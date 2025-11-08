    package DAO;

    import Model.PhongBan;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.sql.Types;
    import java.util.ArrayList;
    import java.util.List;
    import util.DBConnection;

    public class PhongBanDAO {

        public List<PhongBan> getAllPhongBan() {
            List<PhongBan> list = new ArrayList<>();
            String sql = "SELECT * FROM PhongBan";
            try (Connection conn = DBConnection.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new PhongBan(
                            rs.getInt("MaPB"),
                            rs.getString("TenPB"),
                            rs.getString("MoTa"),
                            rs.getObject("TruongPhong") != null ? rs.getInt("TruongPhong") : null
                    ));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return list;
        }

        public PhongBan getByID(String maPB) {
            String sql = "SELECT * FROM PhongBan WHERE MaPB = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, maPB);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new PhongBan(
                            rs.getInt("MaPB"),
                            rs.getString("TenPB"),
                            rs.getString("MoTa"),
                            rs.getObject("TruongPhong") != null ? rs.getInt("TruongPhong") : null
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public int countAllPhongBan() {
            String sql = "SELECT COUNT(*) FROM PhongBan";
            try (Connection conn = DBConnection.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                if (rs.next()) return rs.getInt(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
         public boolean insert(PhongBan pb) {
            String sql = "INSERT INTO PhongBan(TenPB, MoTa, TruongPhong) VALUES (?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, pb.getTenPB());
                ps.setString(2, pb.getMoTa());
                if (pb.getTruongPhong() != null) {
                    ps.setInt(3, pb.getTruongPhong());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }

                int affectedRows = ps.executeUpdate();

                // Lấy ID vừa tạo
                if (affectedRows > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        pb.setMaPB(rs.getInt(1));
                    }
                    return true;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        // Cập nhật phòng ban
        public boolean update(PhongBan pb) {
            String sql = "UPDATE PhongBan SET TenPB = ?, MoTa = ?, TruongPhong = ? WHERE MaPB = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, pb.getTenPB());
                ps.setString(2, pb.getMoTa());
                if (pb.getTruongPhong() != null) {
                    ps.setInt(3, pb.getTruongPhong());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setInt(4, pb.getMaPB());

                return ps.executeUpdate() > 0;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        // Xóa phòng ban
        public boolean deletePhongBan(int maPB) throws SQLException {
            String sql = "DELETE FROM PhongBan WHERE MaPB = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, maPB);
                return ps.executeUpdate() > 0;

            } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) { // MySQL foreign key constraint
                throw new SQLException("Không thể xóa phòng ban vì đang có nhân viên!", ex);
            } else {
                throw ex;
            }
        }
        }
    }
