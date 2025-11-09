// Hàm xem chi tiết lương
function viewSalaryDetail(employeeId) {
    $('#salaryDetailModal').modal('show');
}

// Hàm sửa lương
function addSalary() {
    $('#addSalaryModal').modal('show');
    // Code load dữ liệu vào form để edit
}
// Click nút edit trong table
$(document).on('click', '.btn-edit-salary', function () {
    let $tr = $(this).closest('tr'); // 
    let employeeId = $tr.find('td:eq(0)').text().trim(); // giả sử cột 2 là mã NV

    // AJAX lấy dữ liệu của nhân viên
    $.ajax({
        url: 'salary',
        method: 'GET',
        data: {action: 'getByID', maNV: employeeId},
        success: function (data) {
            // Load dữ liệu vào form edit modal
            $('#editSalaryId').val(data.id);
            $('#editMaNV').val(data.maNV);
            $('#editHoTen').val(data.hoTen);
            $('#editPhongBan').val(data.phongBan);
            $('#editChucVu').val(data.chucVu);
            $('#editThang').val(data.thang);
            $('#editLuongCoBan').val(data.luongCoBan);
            $('#editPhuCap').val(data.phuCap);
            $('#editThuong').val(data.thuong);
            $('#editKhauTru').val(data.khauTru);
            $('#editTrangThai').val(data.trangThai.toLowerCase());
            $('#editGhiChu').val(data.ghiChu);

            $('#editSalaryModal').modal('show');
        },
        error: function () {
            Swal.fire('Lỗi!', 'Không lấy được dữ liệu lương.', 'error');
        }
    });
});


function loadSalaryTable(page = 1) {
    page = parseInt(page);
    if (!page || isNaN(page) || page < 1) {
        page = parseInt($('#currentPageLuong').val()) || 1;
    }

    const search = $('#searchInput').val();
    const department = $('select[name="filterDepartment"]').val();
    const month = $('select[name="filterMonth"]').val();

    $.ajax({
        url: 'salary',
        method: 'GET',
        data: {
            action: 'list',
            page: page,
            search: search,
            filterDepartment: department,
            filterMonth: month
        },
        headers: {'X-Requested-With': 'XMLHttpRequest'},
        success: function (data) {
            const dom = $('<div>').html(data);
            $('#salaryTableBody').html(dom.find('#salaryTableBody').html());
            $('.pagination-luong').html(dom.find('.pagination-luong').html());
            $('#currentPageLuong').val(dom.find('#currentPageLuong').val());
            $('#employeeSelect').html(dom.find('#employeeSelectAjax').html());
            // Update thống kê
            $('#tongLuongThang').text(dom.find('#tongLuongThang').text());
            $('#luongTrungBinh').text(dom.find('#luongTrungBinh').text());
            $('#daThanhToan').text(dom.find('#daThanhToan').text());
            $('#choThanhToan').text(dom.find('#choThanhToan').text());
        },
        error: function () {
            Swal.fire('Lỗi!', 'Có lỗi xảy ra khi tải dữ liệu lương!', 'error');
        }
    });
}

// Trigger lọc khi nhấn nút
$('#btnLocLuong').on('click', function () {
    loadSalaryTable(1); // luôn bắt đầu từ trang 1
});

// Trigger tìm kiếm realtime
$('#searchInput').on('input', function () {
    loadSalaryTable(1);
});

// Click pagination salary
$(document).on('click', '.pagination-luong a.page-link', function (e) {
    e.preventDefault(); // ngăn reload
    let page = parseInt(new URLSearchParams($(this).attr('href').split('?')[1]).get('page'));
    console.log(page)
    loadSalaryTable(page);
});
$(document).ready(function () {
    $('#employeeSelect').select2({
        placeholder: "Chọn nhân viên",
        allowClear: true,
        width: '100%'
    });
});

$('#employeeSelect').on('change', function () {
    const maNV = $(this).val();
    if (!maNV) {
        // Nếu chọn rỗng thì reset
        $('#chucVu').val('');
        $('#phongBan').html('<option value="">-- Chọn phòng ban --</option>');
        $('#luongCoBan').val('');
        return;
    }

    // Gọi AJAX để lấy thông tin nhân viên + lương
    $.ajax({
        url: 'salary',
        method: 'GET',
        data: {
            action: 'getEmployeeData',
            maNV: maNV
        },
        success: function (data) {
            // data trả về JSON {chucVu, maPB, tenPB, luongCoBan}
            $('#chucVu').val(data.chucVu);

            // Set phòng ban
            $('#phongBan').html('<option value="' + data.maPB + '">' + data.tenPB + '</option>');

            // Set lương cơ bản
            $('#luongCoBan').val(data.luongCoBan);
        },
        error: function () {
            Swal.fire('Lỗi!', 'Không lấy được thông tin nhân viên', 'error');
        }
    });
});

$('#addSalaryModal .btn-primary').on('click', function () {
    $('#salaryForm').submit();
    const maNV = $('#employeeSelect').val();
    const thangNam = $('input[type="month"]').val();
    const luongCoBan = $('#luongCoBan').val();
    const phuCap = $('input[placeholder="Nhập phụ cấp"]').val() || 0;
    const thuong = $('input[placeholder="Nhập tiền thưởng"]').val() || 0;
    const khauTru = $('input[placeholder="Nhập khấu trừ"]').val() || 0;
    const trangThai = $('select[required]').val();
    const ghiChu = $('textarea').val();

    if (!maNV || !thangNam) {
        Swal.fire('Lỗi!', 'Vui lòng chọn nhân viên và tháng', 'warning');
        return;
    }

    $.ajax({
        url: 'salary',
        method: 'POST',
        data: {
            action: 'add',
            maNV: maNV,
            thangNam: thangNam,
            luongCoBan: luongCoBan,
            phuCap: phuCap,
            thuong: thuong,
            khauTru: khauTru,
            trangThai: trangThai,
            ghiChu: ghiChu
        },
        success: function (res) {
            if (res.success) {
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Đã thêm bảng lương mới',
                    icon: 'success',
                    showConfirmButton: true,
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    confirmButtonText: 'OK'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Khi modal ẩn xong mới reload table
                        $('#addSalaryModal').one('hidden.bs.modal', function () {
                            loadSalaryTable();
                        });
                        $('#addSalaryModal').modal('hide'); // ẩn modal
                    }
                });
            } else {
                Swal.fire('Lỗi!', res.message, 'error');
            }
        },
        error: function () {
            Swal.fire('Lỗi!', 'Có lỗi khi thêm bảng lương', 'error');
        }


    });
});
// Click nút edit trong table

$(document).on('click', '#btn-edit-salary', function () {
//    console.log("Ngu");
    let $tr = $(this).closest('tr');
    let employeeId = $(this).data('id');

    // AJAX lấy dữ liệu nhân viên
    $.ajax({
        url: 'salary',
        method: 'GET',
        data: {action: 'getByID', maNV: employeeId},
        success: function (data) {
            // Load dữ liệu vào modal edit
            $('#editSalaryId').val(data.id);
            $('#editMaNV').val(data.maNV);
            $('#editHoTen').val(data.hoTen);
            $('#editPhongBan').val(data.phongBan);
            $('#editChucVu').val(data.chucVu);
            $('#editThang').val(data.thang);
            $('#editLuongCoBan').val(data.luongCoBan);
            $('#editPhuCap').val(data.phuCap);
            $('#editThuong').val(data.thuong);
            $('#editKhauTru').val(data.khauTru);
            $('#editTrangThai').val(data.trangThai);
            $('#editGhiChu').val(data.ghiChu);
            $('#editPhongBanId').val(data.maPB);
            $('#editSalaryModal').modal('show');
        },
        error: function () {
            Swal.fire('Lỗi!', 'Không lấy được dữ liệu lương.', 'error');
        }
    });
});
$('#btnSaveEditSalary').on('click', function () {
    const id = $('#editSalaryId').val();
    const phongBan = $('#editPhongBan').val();
    const chucVu = $('#editChucVu').val();
    const thang = $('#editThang').val();
    const luongCoBan = $('#editLuongCoBan').val();
    const phuCap = $('#editPhuCap').val() || 0;
    const thuong = $('#editThuong').val() || 0;
    const khauTru = $('#editKhauTru').val() || 0;
    const trangThai = $('#editTrangThai').val();
    const ghiChu = $('#editGhiChu').val();
    const maPB = $('#editPhongBanId').val();
    if (!phongBan || !chucVu || !thang || !luongCoBan) {
        Swal.fire('Lỗi!', 'Vui lòng nhập đầy đủ thông tin bắt buộc', 'warning');
        return;
    }

    $.ajax({
        url: 'salary',
        method: 'POST',
        data: $('#editSalaryForm').serialize() + '&action=update',
        dataType: 'json',
        success: function (res) {
            // In toàn bộ phản hồi server ra console để debug
            console.log('Server response:', res);


            if (res.success) {
                Swal.fire({
                    title: 'Thành công!',
                    text: res.message || 'Đã cập nhật bảng lương',
                    icon: 'success',
                    confirmButtonText: 'OK'
                }).then(() => {
                    $('#editSalaryModal').modal('hide');
                    loadSalaryTable(); // reload bảng lương
                });
            } else {
                // Hiển thị chi tiết lỗi từ server
                let message = res.message || 'Không xác định';
                Swal.fire('Lỗi!', message, 'error');
                console.error('Server error detail:', res);
            }
        },
        error: function (xhr, status, error) {
            // Hiển thị lỗi AJAX mạng/HTTP
            let errorText = xhr.responseText || error || 'Không xác định';
            Swal.fire(
                    'Lỗi AJAX!',
                    `Status: ${xhr.status} - ${xhr.statusText}\nChi tiết: ${errorText}`,
                    'error'
                    );
            console.error('AJAX network error:', status, error);
            console.error('Response text:', xhr.responseText);
        }
    });
});
function showSalaryDetail(maNV) {
    $('#salaryDetailBody').html('<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</div>');
    $('#salaryDetailModal').modal('show');

    $.ajax({
        url: 'salary',
        type: 'GET',
        data: {action: 'detail', maNV: maNV},
        dataType: 'json',
        success: function (res) {
            if (res && res.id) {
                $('#salaryDetailModal').data('maLuong', res.id);
                $('#salaryDetailModal').data('chiTietLuong', res.chiTietLuong || []);
                $('#salaryDetailModal').data('thucLinh', res.thucLinh);
                $('#salaryDetailModal').data('maNV', res.maNV);
                let html = `
    <div class="row mb-3">
        <div class="col-md-6">
            <h6 class="text-muted">THÔNG TIN NHÂN VIÊN</h6>
            <table class="table table-borderless table-sm">
                <tr><td width="40%"><strong>Mã NV:</strong></td><td>${res.maNV}</td></tr>
                <tr><td><strong>Họ Tên:</strong></td><td>${res.hoTen}</td></tr>
                <tr><td><strong>Phòng Ban:</strong></td><td>${res.phongBan}</td></tr>
                <tr><td><strong>Chức Vụ:</strong></td><td>${res.chucVu}</td></tr>
            </table>
        </div>
        <div class="col-md-6">
            <h6 class="text-muted">CHI TIẾT LƯƠNG</h6>
            <table class="table table-borderless table-sm">
                <tr><td width="50%"><strong>Tháng:</strong></td><td>${res.thang}</td></tr>
                <tr><td><strong>Trạng Thái:</strong></td>
                    <td>${res.trangThai && res.trangThai.toLowerCase() === 'paid' ? "Đã thanh toán" : "Chờ thanh toán"}</td>
                </tr>
            </table>
        </div>
    </div>
    <hr>
    <h6 class="text-muted mb-3">BẢNG TÍNH LƯƠNG</h6>
    <table class="table table-bordered">
        <thead style="background-color: #f8f9fa;">
            <tr><th>Khoản Mục</th><th class="text-right">Số Tiền (VNĐ)</th></tr>
        </thead>
        <tbody>
            <tr><td>Lương cơ bản</td><td class="text-right">${parseFloat(res.luongCoBan).toLocaleString()}</td></tr>
            <tr><td>Tổng phụ cấp</td><td class="text-right">${parseFloat(res.phuCap).toLocaleString()}</td></tr>
            <tr><td>Thưởng</td><td class="text-right">${parseFloat(res.thuong).toLocaleString()}</td></tr>
            <tr><td>Phạt</td><td class="text-right">${parseFloat(res.phat || 0).toLocaleString()}</td></tr>
            <tr><td>Tổng tăng ca</td><td class="text-right">${parseFloat(res.tongTangCa || 0).toLocaleString()}</td></tr>
`;
                html += `
            <tr class="table-light"><td><strong>Tổng khấu trừ</strong></td><td class="text-right text-danger">${parseFloat(res.khauTru || 0).toLocaleString()}</td></tr>
            <tr class="table-info"><td><strong>Lương Thực Nhận</strong></td><td class="text-right"><strong style="color:#0c62a8;">${parseFloat(res.thucLinh).toLocaleString()}</strong></td></tr>
        </tbody>
    </table>
`;

                $('#salaryDetailBody').html(html);
            } else {
                $('#salaryDetailBody').html('<div class="text-danger text-center">Không tìm thấy dữ liệu.</div>');
            }
        },
        error: function () {
            $('#salaryDetailBody').html('<div class="text-danger text-center">Lỗi khi tải dữ liệu.</div>');
        }
    });
}
$('#salaryDetailModal #exportExcelCTL').on('click', function () {
    const maNV = $('#salaryDetailModal').data('maNV'); // Lấy mã nhân viên từ modal

    if (!maNV) {
        Swal.fire('Lỗi', 'Không có dữ liệu để xuất', 'error');
        return;
    }

    // Gửi yêu cầu GET hoặc POST để server xuất file
    window.location.href = `salary?action=exportExcel&maNV=${maNV}`;
});


$('#salaryDetailModal #exportPDFCTL').on('click', function () {
    const maNV = $('#salaryDetailModal').data('maNV'); // Lấy mã nhân viên từ modal

    if (!maNV) {
        Swal.fire('Lỗi', 'Không có dữ liệu để xuất', 'error');
        return;
    }

    // Gọi server xuất PDF (trả về file)
    window.location.href = `salary?action=exportPDF&maNV=${maNV}`;
});




