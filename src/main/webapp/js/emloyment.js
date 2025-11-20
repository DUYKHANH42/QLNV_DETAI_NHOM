$(document).on('click', '#btnSaveEmployee', function () {
    console.log("chạy đc");
    const form = $('#formAddEmployee');

    $.ajax({
        url: 'emloyment?action=add',
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
                    $('#modalAddEmployee').modal('hide');
                    form[0].reset();
                    loadTable();
                }
            });
        },
        error: function () {
            Swal.fire({
                title: 'Lỗi',
                text: 'Không thể kết nối tới server!',
                icon: 'error'
            });
        }
    });
});

function deleteNhanVien(id) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: 'Bạn có chắc muốn xóa nhân viên này không?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Có',
        cancelButtonText: 'Không',
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: 'emloyment?action=delete',
                type: 'POST',
                data: {id: id},
                dataType: 'json',
                success: function (response) {
                    if (response.status === 'success') {
                        loadTable();
                        Swal.fire({
                            icon: 'success',
                            title: 'Đã xóa!',
                            text: response.message,
                            showConfirmButton: "OK",
                        });
                    } else {
                        Swal.fire('Lỗi', response.message, 'error');
                    }
                },
                error: function (xhr, status, error) {
                    Swal.fire('Lỗi', 'Không thể kết nối đến máy chủ', 'error');
                    console.error(error);
                }
            });
        }
    });
}


$(document).on('click', '.btn-edit', function () {
    const maNV = $(this).data('id');

    $.ajax({
        url: 'emloyment', // Servlet xử lý
        type: 'POST',
        data: {action: 'getById', id: maNV},
        dataType: 'json',
        success: function (response) {
            if (response.status === 'success') {
                const nv = response.data;

                // Đưa dữ liệu vào modal
                $('#edit-manv').val(nv.maNV);
                $('#edit-hoten').val(nv.hoTen);
                $('#edit-phongban').val(nv.maPB);
                $('#edit-chucvu').val(nv.chucVu);
                $('#edit-email').val(nv.email);
                $('#edit-sdt').val(nv.sdt);
                $('#edit-trangthai').val(nv.trangThai);
                $('#edit-luongcb').val(nv.luongCoBan);
                $('#edit-hsluong').val(nv.heSoLuong);
                // Hiển thị modal
                $('#editEmployeeModal').modal('show');
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: response.message,
                    confirmButtonColor: '#e74c3c'
                });
            }
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Không thể tải thông tin nhân viên!',
                confirmButtonColor: '#e74c3c'
            });
        }
    });
});
$(document).on('submit', '#FormEditEmployment', function (e) {
    e.preventDefault();

    const form = $(this);

    $.ajax({
        url: 'emloyment?action=update',
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
                    // Ẩn modal
                    $('#editEmployeeModal').modal('hide');
                    form[0].reset();
                    loadTable();
                }
            });
        },
        error: function (err) {
            console.error(err);
            Swal.fire('Lỗi', 'Không thể kết nối server!', 'error');
        }
    });
});

function loadTable(page) {
    if (!page || page === 'undefined' || isNaN(page) || page < 1) {
        page = parseInt($('#currentPage').val()) || 1;
    } else {
        page = parseInt(page);
    }
    $.ajax({
        url: 'emloyment?action=list&page=' + page,
        method: 'GET',
        success: function (data) {
            let newTable = $(data).find('#tableNhanVien tbody').html();
            $('#tableNhanVien tbody').html(newTable);
            let newPagination = $(data).find('.pagination').html();
            $('.pagination').html(newPagination);
            let newPage = $(data).find('#currentPage').val();
            $('#currentPage').val(newPage);
        },
        error: function (err) {
            console.error(err);
        }
    });
}
$(document).on('click', '#btnExportExcel', function (e) {
    e.preventDefault();
    const hoten = $('#hoten').val() || '';
    const phongban = $('#filter-phongban').val() || '';

    fetch('emloyment?action=export', {
        method: 'POST',
        body: new URLSearchParams({hoten, phongban})
    })
            .then(res => res.blob())
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                let fileName = 'DanhSachNhanVien';
                if (phongban)
                    fileName += `_${phongban}`;
                a.download = `${fileName}.xlsx`;
                document.body.appendChild(a);
                a.click();
                a.remove();
            })
            .catch(() => {
                Swal.fire('❌ Xuất Excel thất bại!');
            });
});







