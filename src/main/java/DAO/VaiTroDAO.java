/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.VaiTro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

/**
 *
 * @author PC
 */
public class VaiTroDAO {
    
    public List<VaiTro> getAll() {
        List<VaiTro> dsVaiTro = new ArrayList<>();
        String sql = "SELECT MaVaiTro, TenVaiTro, MoTa FROM VAITRO";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int maVaiTro = rs.getInt("MaVaiTro");
                String tenVaiTro = rs.getString("TenVaiTro");
                String moTa = rs.getString("MoTa");

                VaiTro vt = new VaiTro(maVaiTro, tenVaiTro, moTa);
                dsVaiTro.add(vt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dsVaiTro;
    }
}
