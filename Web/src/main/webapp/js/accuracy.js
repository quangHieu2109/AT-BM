const contextPathMetaTag = document.querySelector("meta[name='contextPath']");
document.addEventListener('DOMContentLoaded', async function () {
    var resendBtn = document.getElementById("resend");
    var comfirmBtn = document.getElementById("confirm");
    var verificationCode = document.getElementById("verificationCode").value;

    // Tạo yêu cầu POST
    async function _confirm() {
        fetch(contextPathMetaTag.content + "/accurancy", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({ verifyCode: verificationCode })
        })
            .then(function (response) {
                // Xử lý response từ servlet
            });
    }

    comfirmBtn.addEventListener('click', _confirm);

    async function _resend() {
        fetch(contextPathMetaTag.content + "/accurancy", {
            method: "GET"
        })
            .then(function (response) {
                // Xử lý response từ servlet
            });
        console.log(contextPathMetaTag.content + "/accurancy");
    }

    resendBtn.addEventListener('click', _resend);
});