/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;

/**
 *
 * @author PC
 */
public class Luong {

    private String maNV;
    private int thang;
    private int nam;
    private int soNgayCong;
    private BigDecimal luongCoBan;   
    private BigDecimal thuong;
    private BigDecimal thucLanh;
    private BigDecimal tongLuong;

    public Luong() {
    }

    public Luong(int thang, int nam, int soNgayCong, BigDecimal luongCoBan, BigDecimal thucLanh) {
        this.thang = thang;
        this.nam = nam;
        this.soNgayCong = soNgayCong;
        this.luongCoBan = luongCoBan;
        this.thucLanh = thucLanh;

    }

    public Luong(String maNV, int thang, int nam, int soNgayCong, BigDecimal luongCoBan, BigDecimal thuong, BigDecimal thucLanh, BigDecimal tongLuong) {
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.soNgayCong = soNgayCong;
        this.luongCoBan = luongCoBan;
        this.thuong = thuong;
        this.thucLanh = thucLanh;
        this.tongLuong = tongLuong;
    }

    public Luong(String maNV, int thang, int nam, int soNgayCong, BigDecimal luongCoBan, BigDecimal thucLanh) {
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.soNgayCong = soNgayCong;
        this.luongCoBan = luongCoBan;
        this.thucLanh = thucLanh;

    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
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

    public int getSoNgayCong() {
        return soNgayCong;
    }

    public void setSoNgayCong(int soNgayCong) {
        this.soNgayCong = soNgayCong;
    }

    public BigDecimal getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(BigDecimal luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public BigDecimal getThuong() {
        return thuong;
    }

    public void setThuong(BigDecimal thuong) {
        this.thuong = thuong;
    }
    

    public BigDecimal getThucLanh() {
        return thucLanh;
    }

    public void setThucLanh(BigDecimal thucLanh) {
        this.thucLanh = thucLanh;
    }

    public BigDecimal getTongLuong() {
        return tongLuong;
    }

    public void setTongLuong(BigDecimal tongLuong) {
        this.tongLuong = tongLuong;
    }


}
