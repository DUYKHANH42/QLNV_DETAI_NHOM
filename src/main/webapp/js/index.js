$(document).ready(function() {
    $.ajax({
        url: '/home',       // Servlet hoặc Controller xử lý
        type: 'GET',
        data: { action: 'chart' },
        dataType: 'json',
        success: function(data) {
            const nvTheoPhongBan = data.nvTheoPhongBan;
            const tyLeGioiTinh = data.tyLeGioiTinh;

            // --- Biểu đồ Nhân viên theo Phòng ban ---
            const deptLabels = Object.keys(nvTheoPhongBan);
            const deptData = Object.values(nvTheoPhongBan);

            new Chart($('#departmentChart'), {
                type: 'bar',
                data: {
                    labels: deptLabels,
                    datasets: [{
                        label: 'Số lượng nhân viên',
                        data: deptData,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: { responsive: true }
            });

            // --- Biểu đồ Tỷ lệ giới tính ---
            const genderLabels = Object.keys(tyLeGioiTinh);
            const genderData = Object.values(tyLeGioiTinh);

            new Chart($('#genderChart'), {
                type: 'pie',
                data: {
                    labels: genderLabels,
                    datasets: [{
                        data: genderData,
                        backgroundColor: ['#36A2EB', '#FF6384', '#FFCE56'],
                        borderWidth: 1
                    }]
                },
                options: { responsive: true }
            });
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi tải dữ liệu biểu đồ:", error);
        }
    });
});