/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import org.apache.poi.hpsf.Decimal;

/**
 *
 * @author PC
 */
public class ChamCong {

    private int maCC;
    private String maNV;
    private Date ngayLam;
    private String trangThai;
    private Time gioVao;
    private BigDecimal sogioLam;
    private Time gioRa;

    public ChamCong(int maCC, String maNV, Date ngayLam, String trangThai, Time gioVao, Time gioRa) {
        this.maCC = maCC;
        this.maNV = maNV;
        this.ngayLam = ngayLam;
        this.trangThai = trangThai;
        this.gioVao = gioVao;
        this.gioRa = gioRa;
    }

    public ChamCong() {
    }

    public ChamCong(int maCC, String maNV, Date ngayLam, String trangThai) {
        this.maCC = maCC;
        this.maNV = maNV;
        this.ngayLam = ngayLam;
        this.trangThai = trangThai;
    }

    public int getMaCC() {
        return maCC;
    }

    public BigDecimal getSogioLam() {
        return sogioLam;
    }

    public void setSogioLam(BigDecimal sogioLam) {
        this.sogioLam = sogioLam;
    }

    public void setMaCC(int maCC) {
        this.maCC = maCC;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public Date getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(Date ngayLam) {
        this.ngayLam = ngayLam;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Time getGioVao() {
        return gioVao;
    }

    public void setGioVao(Time gioVao) {
        this.gioVao = gioVao;
    }

    public Time getGioRa() {
        return gioRa;
    }

    public void setGioRa(Time gioRa) {
        this.gioRa = gioRa;
    }

    @Override
    public String toString() {
        return "ChamCong{" + "maCC=" + maCC + ", maNV=" + maNV + ", ngayLam=" + ngayLam + ", trangThai=" + trangThai + '}';
    }
}
