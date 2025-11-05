package Model;

public class PhuCap {
    private String maPC;
    private String maNV;
    private int thang;
    private int nam;
    private String loaiPhuCap;
    private double soTien;

    public PhuCap() {
    }

    public PhuCap(String maPC, String maNV, int thang, int nam, String loaiPhuCap, double soTien) {
        this.maPC = maPC;
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.loaiPhuCap = loaiPhuCap;
        this.soTien = soTien;
    }

    public String getMaPC() {
        return maPC;
    }

    public void setMaPC(String maPC) {
        this.maPC = maPC;
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

    public String getLoaiPhuCap() {
        return loaiPhuCap;
    }

    public void setLoaiPhuCap(String loaiPhuCap) {
        this.loaiPhuCap = loaiPhuCap;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    @Override
    public String toString() {
        return "PhuCap{" +
                "maPC='" + maPC + '\'' +
                ", maNV='" + maNV + '\'' +
                ", thang=" + thang +
                ", nam=" + nam +
                ", loaiPhuCap='" + loaiPhuCap + '\'' +
                ", soTien=" + soTien +
                '}';
    }
}
