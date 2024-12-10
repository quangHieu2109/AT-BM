// Khởi tạo DataTable và lưu trữ vào biến "table"
// var table = $('#my_table').DataTable();

$(document).ready(function () {
    renderTable(); // Gọi hàm renderTable để tạo DataTable ban đầu

    // Xử lý sự kiện khi nút "addUser" được nhấn
    // var add_user__btn = $('#addUser');
    // var cancel = $('#cancel')
    // var add_user__content = $('#add-user__content');

    $('#addUser').on('click', function () {
        $('#add-user__content').css('display', 'block'); // Hiển thị nội dung thêm người dùng
        $('#table-contaier').css('display', 'none'); // Ẩn bảng người dùng hiện tại
    });

    // Xử lý sự kiện khi nút "cancel" được nhấn
    $('#cancel').on('click', function () {
        $('#add-user__content').css('display', 'none'); // Ẩn nội dung thêm người dùng
        $('#table-contaier').css('display', 'block'); // Hiển thị bảng người dùng hiện tại
    });
});

// Hàm tạo DataTable ban đầu
function renderTable() {
    $('#my_table').DataTable({
        processing: true, // Xóa comment này nếu không muốn hiển thị biểu thị tiến trình
        serverSide: true, // Kích hoạt chế độ xử lý phía máy chủ
        ajax: {
            url: "/userManagerServlet2", // URL của Servlet để lấy dữ liệu từ phía máy chủ
            dataSrc: "data", // Thuộc tính chứa mảng dữ liệu trong đáp ứng từ phía máy chủ
            type: "GET"
        },
        columns: [
            {"data": "id"}, // Cột id
            {"data": "username"}, // Cột username
            {"data": "fullname"}, // Cột fullname
            {"data": "email"}, // Cột email
            {"data": "phoneNumber"}, // Cột phoneNumber
            {"data": "gender"}, // Cột gender
            {"data": "role"}, // Cột role
            {"data": "deleteBtn"} // Cột deleteBtn
        ]
    });
}

// Xử lý sự kiện khi nút "add" được nhấn
// var add = $('#add');
$('#add').on('click', addUserAction);

// Hàm thực hiện thêm người dùng
function addUserAction() {
    // Lấy giá trị từ các trường input
    let username = $('#username').val();
    let password = $('#password').val();
    let fullname = $('#fullname').val();
    let email = $('#email').val();
    let role = $('#role').val();
    let emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/;
    let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/;

    // Kiểm tra và hiển thị thông báo lỗi nếu có
    if (username.length == 0) {
        $('#username-error').text('Vui lòng nhập tên đăng nhập');
    } else {
        $('#username-error').text('');
    }

    if (password.length == 0) {
        $('#password-error').text('Vui lòng nhập mật khẩu');
    } else {
        if (password.length < 8 || !passwordRegex.test(password)) {
            $('#password-error').text('Mật khẩu không đủ mạnh, vui lòng nhập 8 kí tự trở lên bao gồm cả số và chữ viết hoa');
        } else {
            $('#password-error').text('');
        }
    }

    if (fullname.length == 0) {
        $('#fullname-error').text('Vui lòng nhập họ tên');
    } else {
        $('#fullname-error').text('');
    }

    if (email.length == 0) {
        $('#email-error').text('Vui lòng nhập email');
    } else if (!emailRegex.test(email)) {
        $('#email-error').text('Email không hợp lệ');
    } else {
        $('#email-error').text('');
    }

    // Kiểm tra// Kiểm tra xem có lỗi không và thực hiện thêm người dùng nếu không có lỗi
    if ($('#username-error').text().length > 0 || $('#password-error').text().length > 0 ||
        $('#fullname-error').text().length > 0 || $('#email-error').text().length > 0) {
        // Nếu có lỗi, không thực hiện thêm người dùng
    } else {
        addUser(username, password, fullname, email, role);
    }
}

// Hàm gửi yêu cầu thêm người dùng đến phía máy chủ
function addUser(username, password, fullname, email, role) {
    $.ajax({
        url: "/userManagerServlet2", // URL của Servlet để thêm người dùng
        type: 'post',
        data: {
            username: username,
            password: password,
            fullname: fullname,
            email: email,
            role: role
        },
        success: function (response) {
            console.log(response);
            $('#my_table').DataTable().ajax.reload();
            $('#username').val("");
            $('#password').val("");
            $('#fullname').val("");
            $('#emaill').val("");
            $('#add-user__content').css('display', 'none'); // Ẩn nội dung thêm người dùng
            $('#table-contaier').css('display', 'block'); // Hiển thị bảng người dùng hiện tại
            // console.log(response);
            alert('Thêm thành công');
        },
        error: function (response) {
            $('#username-error').text(response.responseJSON.username_error);
            $('#password-error').text(response.responseJSON.password_error);
            $('#email-error').text(response.responseJSON.email_error);
            // console.log(response);
            alert('Thêm thất bại');
        }
    });
}

// Hàm xóa người dùng
function deleteUser(id) {
    $.ajax({
        url: "/userManagerServlet2?id=" + id, // URL của Servlet để xóa người dùng dựa trên id
        type: "DELETE",
        success: function (response) {
            alert("Xóa thành công");

            $('#my_table').DataTable().ajax.reload()
        },
        error: function (response) {
            alert("Xóa thất bại");
        }
    });
}