$('#resendOTP').on("click", function () {
    console.log(1231231231);

    let formData = new FormData();
    formData.append('type', 'resendOTP');

    $('#notifi_sendOTP').text("Hệ thống đang gửi OTP đến email của bạn ...");
    $('#resendOTP').prop('disabled', true); // Vô hiệu hóa nút

    $.ajax({
        url: '/createAuthenticator',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            $('#notifi_sendOTP').text("OTP đã được gửi đến email của bạn!");
            // alert("OTP mới đã được gửi đến email của bạn")
        },
        error: function (response) {
            $('#notifi_sendOTP').text("Gửi OTP thất bại, vui lòng thử lại sau!");
            console.log(response);
        },
        complete: function () {
            // Mở khóa nút sau khi nhận phản hồi từ máy chủ
            $('#resendOTP').prop('disabled', false);
        }
    });
});
$('#inputOTP').on('input', function () {
    let otp = $('#inputOTP').val(); // Sử dụng .val() để lấy giá trị từ input
    if (otp.length > 0) {
        $('#verifyOTP').prop('disabled', false); // Kích hoạt nút nếu có giá trị
    } else {
        $('#verifyOTP').prop('disabled', true); // Vô hiệu hóa nút nếu không có giá trị
    }
});
$('#showPublickey').on('change', function () {
    let publicKey = $('#showPublickey').val();
    if(publicKey.length >0){
        $('#exportKey').prop('disabled', false)
    }else{
        $('#exportKey').prop('disabled', true)
    }
})