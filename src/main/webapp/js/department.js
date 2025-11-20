$(document).ready(function () {

    /** ==================== THÊM PHÒNG BAN ==================== **/
    $(document).on('submit', '#addDepartmentModal form', function (e) {
        e.preventDefault();
        const form = $(this);

        $.ajax({
            url: 'department?action=add',
            type: 'POST',
            data: form.serialize(),
            dataType: 'json',
            success: function (res) {
                Swal.fire({
                    title: res.status === 'success' ? 'Thành công' : 'Lỗi',
                    text: res.message,
                    icon: res.status === 'success' ? 'success' : 'error',
                    confirmButtonText: 'OK'
                }).then(() => {
                    if (res.status === 'success') {
                        $('#addDepartmentModal').modal('hide');
                        form[0].reset();
                        loadPhongBan(); 
                    }
                });
            },
            error: function () {
                Swal.fire('Lỗi', 'Không thể kết nối tới server!', 'error');
            }
        });
    });

// Mở modal sửa phòng ban
    $(document).on('click', '.btn-edit-department', function () {
        const maPB = $(this).data('mapb');

        $.ajax({
            url: 'department?action=getByID',
            type: 'POST',
            data: {id: maPB},
            dataType: 'json',
            success: function (res) {
                if (res.status === 'success') {
                    const pb = res.data;
                    const dsNhanVien = res.dsNhanVien;

                    const $form = $('#formEditDepartment');
                    $form.find('input[name="mapb"]').val(pb.maPB);
                    $form.find('input[name="tenpb"]').val(pb.tenPB);
                    $form.find('textarea[name="mota"]').val(pb.moTa || '');

                    const $select = $form.find('select[name="truongphong"]');
                    $select.empty().append('<option value="">-- Chọn trưởng phòng --</option>');
                    dsNhanVien.forEach(nv => {
                        const selected = nv.maNV == pb.truongPhong ? 'selected' : '';
                        $select.append(`<option value="${nv.maNV}" ${selected}>${nv.hoTen}</option>`);
                    });



                    $('#editDepartmentModal').modal('show');
                }
            }
        });
    });
// Submit form sửa phòng ban
    $(document).on('submit', '#formEditDepartment', function (e) {
        e.preventDefault();
        const form = $(this);

        $.ajax({
            url: 'department?action=update',
            type: 'POST',
            data: form.serialize(),
            dataType: 'json',
            success: function (res) {
                Swal.fire({
                    title: res.status === 'success' ? 'Thành công' : 'Lỗi',
                    text: res.message,
                    icon: res.status === 'success' ? 'success' : 'error',
                    confirmButtonText: 'OK'
                }).then(() => {
                    if (res.status === 'success') {
                        $('#editDepartmentModal').modal('hide');
                        form[0].reset();
                        loadPhongBan(); // reload toàn bộ danh sách
                    }
                });
            },
            error: function () {
                Swal.fire('Lỗi', 'Không thể kết nối server!', 'error');
            }
        });
    });


    $(document).on('click', '.btn-delete-department', function (e) {
        e.preventDefault();
        const maPB = $(this).data('mapb');

        Swal.fire({
            title: 'Xác nhận xóa?',
            text: 'Bạn có chắc muốn xóa phòng ban này?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Có',
            cancelButtonText: 'Không',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: 'department?action=delete',
                    type: 'POST',
                    data: {id: maPB},
                    dataType: 'json',
                    success: function (res) {
                        if (res.status === 'success') {
                            Swal.fire('Đã xóa!', res.message, 'success');
                            loadPhongBan();
                        } else {
                            Swal.fire('Lỗi', res.message, 'error'); // hiện thông báo nếu có nhân viên
                        }
                    },
                    error: function () {
                        Swal.fire('Lỗi', 'Không thể kết nối đến server', 'error');
                    }
                });
            }
        });
    });
    $(document).on('click', '.btn-detail-department', function () {
        const maPB = $(this).data('mapb');

        $.ajax({
            url: 'department?action=detail',
            type: 'POST',
            data: {id: maPB},
            dataType: 'json',
            success: function (res) {
                if (res.status === 'success') {
                    const pb = res.data;
                    const dsNhanVien = res.dsNhanVien;
                    const $modal = $('#departmentDetailModal');

                    $modal.find('.modal-title').text('Chi Tiết Phòng Ban: ' + pb.tenPB);
                    $modal.find('.card:contains("Mã Phòng Ban") h4').text(pb.maPB);
                    $modal.find('.card:contains("Trưởng Phòng") h4').text(pb.truongPhongTen);
                    $modal.find('#detail-tongNV').text(dsNhanVien.length); // Tổng NV
                    $modal.find('#detail-dangLam').text(dsNhanVien.filter(nv => nv.trangThai === 'Đang làm').length); // Đang làm
                    const $tbody = $modal.find('table tbody');
                    $tbody.empty();
                    dsNhanVien.forEach((nv, index) => {
                        $tbody.append(`
                        <tr>
                            <td>${index + 1}</td>
                            <td><strong>${nv.maNV}</strong></td>
                            <td>${nv.hoTen}</td>
                            <td><span class="badge badge-${nv.chucVu === 'Trưởng Phòng' ? 'warning' : 'info'}">${nv.chucVu}</span></td>
                            <td>${nv.email}</td>
                            <td>${nv.SDT}</td>
                            <td><span class="badge badge-${nv.trangThai === 'Đang làm' ? 'success' : 'warning'}">${nv.trangThai}</span></td>
                        </tr>
                    `);
                    });

                    $modal.modal('show');
                } else {
                    Swal.fire('Lỗi', res.message, 'error');
                }
            },
            error: function () {
                Swal.fire('Lỗi', 'Không thể tải chi tiết phòng ban!', 'error');
            }
        });
    });


    function loadPhongBan() {
        $.ajax({
            url: 'department?action=list',
            type: 'GET',
            success: function (data) {
                // Lấy HTML của grid từ JSP
                const newGrid = $(data).find('#departmentsGrid').html();
                $('#departmentsGrid').html(newGrid);
            },
            error: function () {
                console.error('Không thể load phòng ban!');
            }
        });
    }
    // Load lần đầu khi mở trang
    loadPhongBan();
});
$(document).on('click', '#btnExportPhongBan', function (e) {
    e.preventDefault();

    fetch('department?action=exportExcel', { // servlet xử lý xuất Excel phòng ban
        method: 'POST'
    })
    .then(res => {
        if (!res.ok) throw new Error("Lỗi khi xuất Excel");
        return res.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'DanhSachPhongBan.xlsx';
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
    })
    .catch(err => {
        console.error(err);
        Swal.fire({
            icon: 'error',
            title: 'Xuất Excel thất bại!',
            text: 'Đã xảy ra lỗi khi tải file.'
        });
    });
});
$(document).on('click', '#btnExportPDF', function () {
    const maPB = $('#detail-maPB').text();
    window.open('department?action=exportPDF&id=' + maPB, '_blank');
});

