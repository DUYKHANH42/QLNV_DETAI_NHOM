package Model;

public class PhongBan {
    private int maPB;
    private String tenPB;
    private String moTa;
    private Integer truongPhong;

    public PhongBan(int maPB, String tenPB, String moTa, Integer truongPhong) {
        this.maPB = maPB;
        this.tenPB = tenPB;
        this.moTa = moTa;
        this.truongPhong = truongPhong;
    }

    public PhongBan() {
    }

    public PhongBan(int maPB, String tenPB, String moTa) {
        this(maPB, tenPB, moTa, null);
    }

    public int getMaPB() {
        return maPB;
    }

    public void setMaPB(int maPB) {
        this.maPB = maPB;
    }

    public String getTenPB() {
        return tenPB;
    }

    public void setTenPB(String tenPB) {
        this.tenPB = tenPB;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getTruongPhong() {
        return truongPhong;
    }

    public void setTruongPhong(Integer truongPhong) {
        this.truongPhong = truongPhong;
    }

    @Override
    public String toString() {
        return tenPB;
    }
}
