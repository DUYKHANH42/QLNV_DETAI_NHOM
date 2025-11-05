package Model;

import java.sql.Date;

public class NhanVien {

    private String maNV;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
//    private String CCCD;
    private String SDT;
    private String diaChi;
    private String chucVu;
    private Date ngayVaoLam;
    private String maPB;
    private String tenPB;
    private String email;
//    private String maTK;
    private String trangThai;
    private byte[] hinhAnh;

    public NhanVien() {}

    public NhanVien(String maNV, String hoTen, Date ngaySinh, String gioiTinh, String SDT,
                    String diaChi, String chucVu, Date ngayVaoLam, String maPB, String tenPB, String email,
                     String trangThai, byte[] hinhAnh) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
//        this.CCCD = CCCD;
        this.SDT = SDT;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.maPB = maPB;
        this.tenPB = tenPB;
        this.email = email;
//        this.maTK = maTK;
        this.trangThai = trangThai;
        this.hinhAnh = hinhAnh;
    }

    public NhanVien(String maNV, String hoTen, Date ngaySinh, String gioiTinh,  String SDT, String diaChi, String chucVu, Date ngayVaoLam, String maPB, String tenPB, String email, String trangThai) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
//        this.CCCD = CCCD;
        this.SDT = SDT;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.maPB = maPB;
        this.tenPB = tenPB;
        this.email = email;
//        this.maTK = maTK;
        this.trangThai = trangThai;
    }
    

    public NhanVien(String maNV, String hoTen, String maPB, String tenPB, String email) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.maPB = maPB;
        this.tenPB = tenPB;
        this.email = email;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

//    public String getCCCD() { return CCCD; }
//    public void setCCCD(String CCCD) { this.CCCD = CCCD; }

    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }

    public String getTenPB() { return tenPB; }
    public void setTenPB(String tenPB) { this.tenPB = tenPB; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

//    public String getMaTK() { return maTK; }
//    public void setMaTK(String maTK) { this.maTK = maTK; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public byte[] getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(byte[] hinhAnh) { this.hinhAnh = hinhAnh; }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh='" + gioiTinh + '\'' +
//                ", CCCD='" + CCCD + '\'' +
                ", SDT='" + SDT + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", ngayVaoLam=" + ngayVaoLam +
                ", maPB='" + maPB + '\'' +
                ", tenPB='" + tenPB + '\'' +
                ", email='" + email + '\'' +
//                ", maTK='" + maTK + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", hinhAnh=" + (hinhAnh != null ? "[data]" : "null") +
                '}';
    }
}
