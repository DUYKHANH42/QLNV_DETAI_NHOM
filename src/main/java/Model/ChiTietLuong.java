package Model;

import java.math.BigDecimal;

public class ChiTietLuong {
    private int maChiTietLuong;
    private int maLuong;
    private String loaiChiTiet;
    private String moTa;
    private BigDecimal soTien;

    public ChiTietLuong() {}

    public int getMaChiTietLuong() {
        return maChiTietLuong;
    }

    public void setMaChiTietLuong(int maChiTietLuong) {
        this.maChiTietLuong = maChiTietLuong;
    }

    public int getMaLuong() {
        return maLuong;
    }

    public void setMaLuong(int maLuong) {
        this.maLuong = maLuong;
    }

    public String getLoaiChiTiet() {
        return loaiChiTiet;
    }

    public void setLoaiChiTiet(String loaiChiTiet) {
        this.loaiChiTiet = loaiChiTiet;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }
}
