package Model;

public enum TrangThaiLuong {
    Paid,      
    Unpaid;    

    public static TrangThaiLuong fromString(String value) {
        if (value == null) return Unpaid;
        switch (value.trim().toLowerCase()) {
            case "paid":
                return Paid;
            default:
                return Unpaid;
        }
    }
}
