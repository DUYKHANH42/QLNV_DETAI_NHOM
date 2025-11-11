function parsePage(page) {
    page = parseInt(page, 10);
    if (isNaN(page) || page < 1) page = 1;
    return page;
}

function loadChamCongTable(page) {
    page = parsePage(page);

    $.ajax({
        url: 'checkin?action=list&page=' + page,
        method: 'GET',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
        success: function (data) {
            const dom = $('<div>').html(data);
            $('#tableChamCong tbody').html(dom.find('#tableChamCong tbody').html());
            $('.pagination-cc').html(dom.find('.pagination-cc').html());

            let newPage = parsePage(dom.find('#currentPageChamCong').val());
            $('#currentPageChamCong').val(newPage);
        }
    });
}

function loadTableBySearch(page) {
    page = parsePage(page);

    const dateValue = $('input[name="date"]').val();
    const phongban = $('select[name="phongban"]').val();
    const tenNV = $('input[name="tenNV"]').val();

    $.ajax({
        url: 'checkin',
        method: 'GET',
        data: {action: 'search', page: page, date: dateValue, phongban, tenNV},
        headers: {'X-Requested-With': 'XMLHttpRequest'},
        success: function (data) {
            const dom = $('<div>').html(data);
            $('#tableChamCong tbody').html(dom.find('#tableChamCong tbody').html());
            $('.pagination-cc').html(dom.find('.pagination-cc').html());
            let newPage = parsePage(dom.find('#currentPageChamCong').val());
            $('#currentPageChamCong').val(newPage);
        }
    });
}


// Delegate event cho pagination
$('.pagination-cc').on('click', 'a.page-link', function (e) {
    let href = $(this).attr('href');
    let pageParam = new URLSearchParams(href.split('?')[1]).get('page');

    // Xác định action dựa vào URL
    if (href.includes('action=search')) {
        loadTableBySearch(pageParam);
    } else {
        loadChamCongTable(pageParam);
    }
});
const $table = $('#tableChamCong');
const $toolbar = $('#chamCongToolbar');
const $selectedCount = $('#selectedCount');
const $btnChamCong = $('#btnChamCong');
let toolbarManuallyClosed = false;
$('#btnCloseToolbar').on('click', function () {
    $toolbar.addClass('d-none');
    $('#select-all').prop('checked', false);
    $table.find('.employee-checkbox').prop('checked', false);
    window.selectedEmployees = [];
    toolbarManuallyClosed = true; // đánh dấu đã tắt
});
// Hàm lấy tất cả checkbox được check
function getSelectedEmployees() {
    const selected = [];
    $table.find('.employee-checkbox:checked').each(function () {
        selected.push($(this).val());
    });
    return selected;
}

/// Cập nhật toolbar
function updateToolbar() {
    // Nếu đang check select-all thì lấy số lượng toàn bộ nhân viên đã lưu
    const totalSelected = $('#select-all').is(':checked') ? window.selectedEmployees.length : getSelectedEmployees().length;

    if (totalSelected > 0) {
        $toolbar.removeClass('d-none');
        $selectedCount.text(totalSelected);
        $btnChamCong.html(totalSelected > 1
                ? '<i class="fas fa-calendar-check"></i> Chấm Công Hàng Loạt'
                : '<i class="fas fa-calendar-check"></i> Chấm Công');
    } else {
        $toolbar.addClass('d-none');
    }
}
window.selectedEmployees = [];


// Click select-all
$('#select-all').on('change', function () {
    const checked = $(this).is(':checked');

    if (checked) {
        const date = $('input[name="date"]').val();
        const phongban = $('select[name="phongban"]').val();
        const tenNV = $('input[name="tenNV"]').val();
        $.ajax({
            url: 'checkin?action=searchAll',
            method: 'GET',
            data: {date, phongban, tenNV},
            success: function (data) {
                window.selectedEmployees = data.map(nv => nv.maNV);

                $table.find('.employee-checkbox').prop('checked', true);
                updateToolbar();
            }
        });
    } else {
        window.selectedEmployees = [];
        $table.find('.employee-checkbox').prop('checked', false);
        updateToolbar();
    }
});

// Xử lý click từng checkbox
$table.on('change', '.employee-checkbox', function () {
    const allChecked = $table.find('.employee-checkbox').length === $table.find('.employee-checkbox:checked').length;
    $('#select-all').prop('checked', allChecked);
    updateToolbar();
});
$btnChamCong.on('click', function () {
    let selected = [];

    if ($('#select-all').is(':checked')) {
        selected = window.selectedEmployees.slice();
    } else {
        selected = getSelectedEmployees();
    }

    if (selected.length === 0)
        return;

    const isBatch = selected.length > 1;

    const dateValue = $('input[name="date"]').val() || new Date().toISOString().slice(0, 10);

    $.ajax({
        url: 'checkin?action=chamCong',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            maNVList: selected,
            batch: isBatch,
            date: dateValue
        }),
        success: function (res) {
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Chấm công ' + (isBatch ? 'hàng loạt' : 'cá nhân') + ' thành công!',
                timer: 2000,
                showConfirmButton: false
            });

            $('#select-all').prop('checked', false);
            $table.find('.employee-checkbox').prop('checked', false);
            window.selectedEmployees = [];
            updateToolbar();

            const currentPage = $('#currentPageChamCong').val();
            console.log(currentPage)
            if (dateValue || $('select[name="phongban"]').val() || $('input[name="tenNV"]').val()) {
                loadTableBySearch(currentPage);
            } else {
                loadChamCongTable(currentPage);
            }
        },
        error: function (err) {
            console.error(err);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi chấm công!'
            });
        }
    });
});
// Bắt sự kiện click nút sửa
$(document).ready(function () {
    $table.on('click', '.btn-editChamCong', function () {
        const $tr = $(this).closest('tr');
        const maNV = $tr.find('td:eq(1)').text().trim();
        const hoTen = $tr.find('td:eq(2)').text().trim();
        const phongBan = $tr.find('td:eq(3) .badge').text().trim();
        let gioVao = $tr.find('td:eq(4) .text-success').text().trim();
        let gioRa = $tr.find('td:eq(5)').text().trim();
        const textTrangThai = $tr.find('td:eq(6) .badge').text().trim();

        let trangThaiValue;
        switch (textTrangThai) {
            case 'Đi làm':
                trangThaiValue = 'present';
               
                break;
            case 'Đi muộn':
                trangThaiValue = 'late';
                break;
            case 'Vắng':
                trangThaiValue = '';
                break;
            case 'Nghỉ phép':
                trangThaiValue = 'leave';
                break;
            default:
                trangThaiValue = '';
        }
        console.log(trangThaiValue);
        function to24Hour(timeStr) {
            if (!timeStr || timeStr === '--:--')
                return '';
            if (timeStr.toUpperCase().includes('AM') || timeStr.toUpperCase().includes('PM')) {
                let [time, modifier] = timeStr.split(' ');
                let [hours, minutes] = time.split(':');
                hours = parseInt(hours, 10);
                if (modifier.toUpperCase() === 'PM' && hours < 12)
                    hours += 12;
                if (modifier.toUpperCase() === 'AM' && hours === 12)
                    hours = 0;
                return ('0' + hours).slice(-2) + ':' + minutes;
            }
            return timeStr.slice(0, 5);
        }

        gioVao = to24Hour(gioVao);
        gioRa = to24Hour(gioRa);

        const ngay = $('#date').val();

        $('#edit_id').val(maNV);
        $('#edit_nhanvien').val(maNV + ' - ' + hoTen + ' (' + phongBan + ')');
        $('#edit_ngay').val(ngay);
        $('#edit_gio_vao').val(gioVao);
        $('#edit_gio_ra').val(gioRa);
        $('#edit_trangthai').val(trangThaiValue);

        $('#editAttendanceModal').modal('show');
    });
});


$(document).ready(function () {
    $('#editAttendanceForm').on('submit', function (e) {

        const formData = {
            id: $('#edit_id').val(),
            ngay: $('#edit_ngay').val(),
            gio_vao: $('#edit_gio_vao').val(),
            gio_ra: $('#edit_gio_ra').val(),
            trangthai: $('#edit_trangthai').val()
        };

        $.ajax({
            url: 'checkin?action=update',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (res) {
                Swal.fire({
                    icon: 'success',
                    title: 'Cập nhật thành công!',
                    timer: 1500,
                    showConfirmButton: false
                });

                $('#editAttendanceModal').modal('hide');
                const currentPage = $('#currentPageChamCong').val() || 1;
                const dateValue = $('#date').val();
                const phongban = $('select[name="phongban"]').val();
                const tenNV = $('input[name="tenNV"]').val();

                if (dateValue || phongban || tenNV) {
                    loadTableBySearch(currentPage);
                } else {
                    loadChamCongTable(currentPage);
                }
            },
            error: function (err) {
                console.error(err);
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Cập nhật chấm công thất bại!'
                });
            }
        });
    });
});
// Mở modal chi tiết
$(document).on("click", ".btn-view-detail", function () {
    const maNV = $(this).data("id");
    const dateValue = $("#date").val(); // lấy giá trị từ bộ lọc
    let thang, nam;

    if (dateValue) {
        const dt = new Date(dateValue);
        thang = dt.getMonth() + 1; // JS tháng bắt đầu từ 0
        nam = dt.getFullYear();
    } else {
        const now = new Date();
        thang = now.getMonth() + 1;
        nam = now.getFullYear();
    }
    $.ajax({
        url: "checkin",
        type: "GET",
        data: {action: "detail",
            maNV: maNV,
            thang: thang,
            nam: nam},
        dataType: "json",
        success: function (data) {
            if (!data)
                return;

            // --- Gán thông tin nhân viên ---
            $("#detailAttendanceModal .modal-title").html(
                    `<i class="fas fa-calendar-alt"></i> Chi Tiết Chấm Công Tháng ${data.thangNam}`
                    );
            $("#detailAttendanceModal .nv-ma").text(data.maNV);
            $("#detailAttendanceModal .nv-ten").text(data.hoTen);
            $("#detailAttendanceModal .nv-pb").text(data.tenPB);
            $("#detailAttendanceModal .nv-ngayCong").text(`${data.soNgayCong}/26`);

            // --- Cập nhật 4 ô thống kê ---
            $("#detailAttendanceModal .ngay-co-mat").text(data.coMat);
            $("#detailAttendanceModal .ngay-di-muon").text(data.diMuon);
            $("#detailAttendanceModal .ngay-vang").text(data.vang);
            $("#detailAttendanceModal .ngay-nghi-phep").text(data.nghiPhep);
            $("#btnExportExcelChiTietCC").data("id", data.maNV);
            $("#btnExportPDFChiTietCC").data("id", data.maNV);
            // --- Gán maNV cho nút xuất Excel ---
            $("#btnExportExcelChiTietCC").data("id", data.maNV);

            // --- Bảng chi tiết ---
            let html = "";
            data.dsChiTiet.forEach(item => {
                let rowClass = "";
                let gioVao = item.gioVao || "--:--";
                let gioRa = item.gioRa || "--:--";
                let tongGio = item.tongGio || "0h 0p";
                let badge = "";

                if (item.isSunday && item.trangThai === "absent") {
                    rowClass = "bg-warning ";
                    gioVao = gioRa = tongGio = "-";
                    badge = `<span class="badge badge-secondary">Ngày nghỉ</span>`;
                } else {
                    badge =
                            item.trangThai === "present" ? `<span class="badge badge-success"><i class="fas fa-check"></i> Có mặt</span>` :
                            item.trangThai === "late" ? `<span class="badge badge-warning"><i class="fas fa-clock"></i> Đi muộn</span>` :
                            item.trangThai === "leave" ? `<span class="badge badge-info"><i class="fas fa-plane"></i> Nghỉ phép</span>` :
                            `<span class="badge badge-danger"><i class="fas fa-times"></i> Vắng</span>`;
                }

                html += `
                    <tr class="${rowClass}">
                        <td><strong>${item.ngay}</strong></td>
                        <td>${item.thu}</td>
                        <td class="text-center">${gioVao}</td>
                        <td class="text-center">${gioRa}</td>
                        <td class="text-center">${tongGio}</td>
                        <td class="text-center">${badge}</td>
                    </tr>
                `;
            });

            $("#tableChiTietCC tbody").html(html);
            $("#detailAttendanceModal").modal("show");
        },
        error: function (xhr, status, err) {
//            console.error("Lỗi khi tải chi tiết:", err);
        }
    });
});

// Xuất Excel chi tiết
$(document).on('click', '#btnExportExcelChiTietCC', function (e) {
  
    const maNV = $(this).data('id');
    if (!maNV) {
        Swal.fire({
            icon: 'error',
            title: 'Thiếu nhân viên!',
            text: 'Vui lòng mở chi tiết nhân viên trước khi xuất Excel.'
        });
        return;
    }

    fetch(`checkin?action=exportExcelChiTietCC&maNV=${maNV}`, {method: 'GET'})
            .then(res => {
                if (!res.ok)
                    throw new Error("Lỗi khi xuất Excel chi tiết");
                return res.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = `ChiTietChamCong_${maNV}.xlsx`;
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
$(document).on('click', '#btnExportPDFChiTietCC', function () {
    const maNV = $(this).data('id');
    if (!maNV) {
        Swal.fire({
            icon: 'error',
            title: 'Thiếu nhân viên!',
            text: 'Vui lòng mở chi tiết nhân viên trước khi xuất PDF.'
        });
        return;
    }

    const link = document.createElement('a');
    link.href = 'checkin?action=exportPDFChiTietCC&maNV=' + maNV;
    link.download = `ChiTietChamCong_${maNV}.pdf`;
    document.body.appendChild(link);
    link.click();
    link.remove();
});

$(document).on('click', '#btnXuatExcelCC', function (e) {
    

    // Lấy bộ lọc hiện tại
    const dateValue = $('#date').val();
    const phongBan = $('select[name="phongban"]').val();
    const tenNV = $('input[name="tenNV"]').val();

    // Nếu muốn xuất tất cả table thì ko cần maNV, chỉ dùng filter
    const query = new URLSearchParams({
        action: 'exportExcelCC',
        date: dateValue,
        phongban: phongBan || '',
        tenNV: tenNV || ''
    }).toString();

    fetch(`checkin?${query}`, { method: 'GET' })
        .then(res => {
            if (!res.ok) throw new Error("Lỗi khi xuất Excel");
            return res.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `BangChamCong_${dateValue}.xlsx`;
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







