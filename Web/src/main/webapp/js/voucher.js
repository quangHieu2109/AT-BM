var voucherType = 22;

function randomVoucherCode() {
    $.ajax({
        url: "/admin/voucherManager/createVoucherSevlet?type=randomVoucherCode",
        type: "GET",
        success: function (response) {
            console.log(response)
            $("#voucher-code").val(response.voucherCode)

        },error: function (response) {
            console.log(response)

        }
    })
}

$('#my_form').on('submit', submitForm)

function submitForm(event) {
    event.preventDefault()
    $('#voucher-imageName_error').text("")
    if ($('#voucher-imageName').val() == "") {
        $('#voucher-imageName_error').text("Vui lòng chọn ảnh voucher")

    }
    $('#voucher-startsAt_error').text("")
    if ($('#voucher-startsAt').val() == "") {
        $('#voucher-startsAt_error').text("Vui lòng chọn ngày bắt đầu")
    }
    $('#voucher-endAt_error').text("")
    if ($('#voucher-endAt').val() == "") {
        $('#voucher-endAt_error').text("Vui lòng chọn ngày kết thúc")
    }

    var categorys = [];
    $('input[name="categorys"]:checked').each(function () {
        categorys.push($(this).val());
    });

    var formData = new FormData();
    formData.append('image', $('#voucher-imageName')[0].files[0]);
    formData.append('voucherName', $('#voucher-name').val());
    formData.append('voucherCode', $('#voucher-code').val());
    formData.append('typeVoucher', $("input[name='typeVoucher']:checked").val());
    formData.append('description', $('#voucher-description').val());
    formData.append('percentDecrease', $('#percent-decrease').val());
    formData.append('maxDecrease', $('#max-decrease').val());
    formData.append('minPrice', $('#min-price').val());
    formData.append('voucherStartsAt', $('#voucher-startsAt').val());
    formData.append('voucherEndAt', $('#voucher-endAt').val());
    formData.append('quantity', $('#quantity').val());
    if (categorys.length > 0) {
        formData.append('categorys', categorys);
    }

    $.ajax({
        url: "/admin/voucherManager/createVoucherSevlet",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            alert(response.msg);
            window.location.reload();
        },
        error: function (response) {
            console.log(response)
            if (response.status == 400) {
                $('#voucher-code_error').text(response.responseJSON.msg)
            }
            alert(response.responseJSON.msg)
        }
    });

}

function getVoucher(type) {
   let voucher_body_product = $('#voucher_body_product')
   let voucher_body_ship = $('#voucher_body_ship')
    $('#voucher_container').css('display', 'block')
    let get = false;
    if(type == 0){
        $('#voucher_body_ship').css('display', 'block');
        $('#voucher_body_product').css('display', 'none');
       get = ($('#voucher_body_ship').children().length <=0)
    }else{
        $('#voucher_body_product').css('display', 'block');
        $('#voucher_body_ship').css('display', 'none');
        get = ($('#voucher_body_product').children().length <=0)
    }

    if(get){
        //Lấy danh sách các voucher từ server
        $.ajax({
            url: "/voucherServlet?type=getVouchers&voucherType=" + type,
            type: 'GET',
            success: function (repsonse) {
                let vouchers = repsonse.vouchers;
                if (type === 1) {
                    voucher_body_product.empty();
                    if (vouchers.length === 0) {
                        $('#voucher_body_product').append(`<div class="w-100 h-100 d-flex justify-content-center
                            align-content-center">Hiện không có voucher nào khả dụng</div>`)
                    } else {
                        for (let i = 0; i < vouchers.length; i++) {
                            $('#voucher_body_product').append(convertVoucherToHTML(vouchers[i], type))
                        }
                        applyVoucher(type)
                    }
                } else {
                    voucher_body_ship.empty();
                    if (vouchers.length === 0) {
                        $('#voucher_body_ship').append(`<div class="w-100 h-100 d-flex justify-content-center 
                            align-content-center">Hiện không có voucher nào khả dụng</div>`)
                    } else {
                        for (let i = 0; i < vouchers.length; i++) {
                            $('#voucher_body_ship').append(convertVoucherToHTML(vouchers[i], type))
                        }
                        applyVoucher(type)
                    }
                }
            },
            error: function (response) {
                console.log(response)

            }
        })
    }

}

function applyVoucher(type) {
    const vouchers = $(`input[name='${type}']`);
    vouchers.each(function () {
        $(this).on('change', function () {
            getDecrease(type)
            let selectedVoucher = $(`input[name='${type}']:checked`);
            let cartItemIds = []
            $("input[name='cartItem']:checked").each(function () {
                cartItemIds.push($(this).val())
            })
            if (cartItemIds.length == 0) {
                alert('Vui lòng chọn sản phẩm muốn áp dụng mã giảm giá')
                selectedVoucher.prop('checked', false)
            }
        });
    });
}

function updateAplyVoucher() {
    getDecrease(0);
    getDecrease(1);
    setTimeout(function() {
        updateToTalPrice()
    }, 500);

}

function getDecrease(type) {
    console.log("type", type)
    let selectedVoucher = $(`input[name='${type}']:checked`);
    let cartItemIds = []
    $("input[name='cartItem']:checked").each(function () {
        cartItemIds.push($(this).val())
    })
    if (cartItemIds.length == 0) {
        $('#voucher-ship').text(0)
        $('#voucher-product').text(0)

    } else if(selectedVoucher.val()>=0){
        if(type ==0 && typeof $("input[name='infoship']:checked").val() == 'undefined'){
            alert('Vui lòng chọn phương thức vận chuyển')
            selectedVoucher.prop('checked', false)
        }
        if ((type == 0 && $("input[name='infoship']:checked").val() > 0) || type ==1) {
            formData = new FormData();
            let ship = (type == 1) ? 0 : $("input[name='infoship']:checked").val()
            formData.append('voucherId', selectedVoucher.val());
            formData.append('cartItemIds', cartItemIds);
            formData.append('ship', ship);
            $.ajax({
                url: "/voucherServlet",
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    if(response.decrease >0){
                        if (type == 0) {
                            $('#voucher-ship').attr('data-value' ,(response.decrease))
                            $('#voucher-ship').text(_formatPrice(response.decrease))
                        } else {
                            $('#voucher-product').attr('data-value' ,(response.decrease))
                            $('#voucher-product').text(_formatPrice(response.decrease))
                        }
                        setTimeout(function() {
                            updateToTalPrice()
                        }, 500);
                    }else{
                        selectedVoucher.prop('checked', false)
                        alert("Chưa đủ điều kiện để sử dụng mã")
                    }
                },
                error: function (response) {
                    console.log(response)
                }
            })

        }
    }
}
//Hiển thị danh sách các voucher cho người dùng từ dữ liệu
// của server
function convertVoucherToHTML(voucher, type) {
    return `
        <input type="radio" value="${voucher.id}" name="${type}" id="voucher${voucher.id}" hidden>
    <div class="row voucher_item">
        <div class="col-lg-3 d-flex justify-content-center">
            <img src="${voucher.voucherImage}" alt="${voucher.voucherName}">
        </div>
        <div class="col-lg-7 row">
            <span class="title">${voucher.voucherName}</span>
            <span class="title">Giảm: ${voucher.percentDecrease}% Tối đa: ${_formatPrice(voucher.maxDecrease)}đ</span>
            <span class="title">Cho đơn hàng từ: ${_formatPrice(voucher.minPrice)}đ</span>
            <span class="title">Từ ngày ${voucher.startAt} đến hết ${voucher.endAt}</span>
            <span class="title">Áp dụng cho: ${voucher.categories}</span>
        </div>
        <div class="col-lg-2">
             <label for="voucher${voucher.id}" class="btn btn-primary 
             align-content-center w-100 h-100">Chọn</label>
        </div>
    </div>`;
}

function updateToTalPrice() {

    let totalPrice = parseFloat($('#temp-price').attr('data-value'));
    totalPrice -= parseFloat($('#voucher-product').attr('data-value'));
    totalPrice -= parseFloat($('#voucher-ship').attr('data-value'));
    totalPrice += parseFloat($('#delivery-price').attr('data-value'));
    $('#total-price').attr('data-value', totalPrice)
    $('#total-price').text(_formatPrice(totalPrice))
    console.log('totalPrice ',totalPrice)

}

function _formatPrice(value) {
    return parseFloat(value).toLocaleString('vi-VN')
}

function _reFormatPrice(value) {
    return parseFloat(value.replace(".", ""))
}

