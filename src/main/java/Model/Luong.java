/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author PC
 */
public class Luong {

    private int maLuong;
    private int maNV;
    private int thang;
    private int nam;
    private double luongCoBan;
    private int soNgayCong;
    private double tongPhuCap;
    private double thuong;
    private double phat;
    private double tongTangCa;
    private double tongKhauTru;
    private double tongThuNhap;
    private double thucLinh;
    private TrangThaiLuong trangThai;
    private String ghiChu;

    public Luong(int maLuong, int maNV, int thang, int nam, double luongCoBan, int soNgayCong, double tongPhuCap, double thuong, double phat, double tongTangCa, double tongKhauTru, double tongThuNhap, double thucLinh, TrangThaiLuong trangThai) {
        this.maLuong = maLuong;
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.soNgayCong = soNgayCong;
        this.tongPhuCap = tongPhuCap;
        this.thuong = thuong;
        this.phat = phat;
        this.tongTangCa = tongTangCa;
        this.tongKhauTru = tongKhauTru;
        this.tongThuNhap = tongThuNhap;
        this.thucLinh = thucLinh;
        this.trangThai = trangThai;
    }

    public Luong(int maNV, int thang, int nam, double luongCoBan, int soNgayCong, double tongPhuCap, double thuong, double phat, double tongTangCa, double tongKhauTru, double tongThuNhap, double thucLinh, TrangThaiLuong trangThai, String ghiChu) {
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.soNgayCong = soNgayCong;
        this.tongPhuCap = tongPhuCap;
        this.thuong = thuong;
        this.phat = phat;
        this.tongTangCa = tongTangCa;
        this.tongKhauTru = tongKhauTru;
        this.tongThuNhap = tongThuNhap;
        this.thucLinh = thucLinh;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public Luong() {
    }

    public int getMaLuong() {
        return maLuong;
    }

    public void setMaLuong(int maLuong) {
        this.maLuong = maLuong;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public int getSoNgayCong() {
        return soNgayCong;
    }

    public void setSoNgayCong(int soNgayCong) {
        this.soNgayCong = soNgayCong;
    }

    public double getTongPhuCap() {
        return tongPhuCap;
    }

    public void setTongPhuCap(double tongPhuCap) {
        this.tongPhuCap = tongPhuCap;
    }

    public double getThuong() {
        return thuong;
    }

    public void setThuong(double thuong) {
        this.thuong = thuong;
    }

    public double getPhat() {
        return phat;
    }

    public void setPhat(double phat) {
        this.phat = phat;
    }

    public double getTongTangCa() {
        return tongTangCa;
    }

    public void setTongTangCa(double tongTangCa) {
        this.tongTangCa = tongTangCa;
    }

    public double getTongKhauTru() {
        return tongKhauTru;
    }

    public void setTongKhauTru(double tongKhauTru) {
        this.tongKhauTru = tongKhauTru;
    }

    public double getTongThuNhap() {
        return tongThuNhap;
    }

    public void setTongThuNhap(double tongThuNhap) {
        this.tongThuNhap = tongThuNhap;
    }

    public double getThucLinh() {
        return thucLinh;
    }

    public void setThucLinh(double thucLinh) {
        this.thucLinh = thucLinh;
    }

    public TrangThaiLuong getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiLuong trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}
