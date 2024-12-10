import createToast, {toastComponent} from "./toast.js";

var contextPath = $("meta[name='contextPath']").attr('content');

const FAILED_OPERATION_MESSAGE = "Đã có lỗi truy vấn!";
const SUCCESS_DELETE_CART_ITEM_MESSAGE = (productName) => `Đã xóa sản phẩm ${productName} khỏi giỏ hàng thành công!`;
const SUCCESS_UPDATE_CART_ITEM_MESSAGE = (productName) => `Đã cập nhật số lượng của sản phẩm ${productName} thành công!`;
const SUCCESS_ADD_ORDER_MESSAGE = "Đặt hàng thành công!";
const ERROR_ADD_ORDER_MESSAGE = "Đặt hàng thất bại, vui lòng thử lại sau!";
var cartItems;

loadCartItem()

function loadCartItem() {
    $.ajax({
        url: '/cartItem',
        type: 'GET',
        success: function (response) {
            cartItems = response.cartItems;
            if (cartItems.length == 0) {
                $('#cart_body').append('Giỏ hàng không có sản phẩm nào :(')
            } else {
                for (let i = 0; i < cartItems.length; i++) {
                    $('#cart_body').append(convertCartItemToHTML(cartItems[i]))
                    updateCartItem(cartItems[i].id, cartItems[i].productName)
                    deleteCartItem(cartItems[i].id, cartItems[i].productName)
                }
                updateTotalProductPrice()
            }
            autoCheckProduct($('#idCheck').text());
        },
        error: function (response) {
            console.log(response)

        }
    })

}

function autoCheckProduct(json) {
    try {
        json = JSON.parse(json)
        for (let i = 0; i < json.data.length; i++) {
            document.getElementById('isBuy' + json.data[i]).click()

        }
    } catch (e) {

    }


}

export function updateTotalQuantity() {
    $.ajax({
        url: '/cartItem?type=getTotalQuantity',
        type: 'GET',
        success: function (response) {
            $('#total-cart-items-quantity').text(response.totalQuantity)
        },
        error: function (response) {
            console.log(response)
        }
    })
}

function deleteCartItem(id, productName) {


    $('#delete-cart-item-' + id).on('click', function () {
        if (confirm("Bạn có muốn xóa?")) {
            let formData = new FormData()
            formData.append('cartItemId', id);

            $.ajax({
                url: "/cartItem",
                type: "DELETE",
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    createToast(toastComponent(SUCCESS_DELETE_CART_ITEM_MESSAGE(productName), "success"));
                    updateTotalQuantity()
                    getTotalProductPrice()
                    getDecrease(0)
                    getDecrease(1)
                    $('#tr' + id).remove()
                },
                error: function (response) {
                    console.log(response)
                    createToast(toastComponent(FAILED_OPERATION_MESSAGE, "danger"));
                }
            })
        }

    })
}

function updateCartItem(id, productName) {


    $('#update-cart-item-' + id).on('click', function () {

        let formData = new FormData()
        formData.append('cartItemId', id);
        formData.append('quantity', $('#quantity-cart-item-' + id).val());
        $.ajax({
            url: "/cartItem",
            type: "PUT",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                createToast(toastComponent(SUCCESS_UPDATE_CART_ITEM_MESSAGE(productName), "success"));
                if (response.productQuantity < $('#quantity-cart-item-' + id).val()) {
                    $('#isBuy' + id).prop('disabled', true)
                    $('#isBuy' + id).prop('checked', false)
                } else {
                    $('#isBuy' + id).prop('disabled', false)
                }
                updateTotalQuantity()
                getTotalProductPrice()
                getDecrease(0)
                getDecrease(1)
            },
            error: function (response) {
                console.log(response)
                createToast(toastComponent(FAILED_OPERATION_MESSAGE, "danger"));
            }
        })
    })
}

function getTotalProductPrice() {
    let cartItemIds = []
    $("input[name='cartItem']:checked").each(function () {
        cartItemIds.push($(this).val())
    })

    if (cartItemIds.length == 0) {
        $('#temp-price').text('0')
        $('#temp-price').attr('data-value', 0)
        $("#checkoutBtn").prop("disabled", true);
    } else {
        $("#checkoutBtn").prop("disabled", false);
        let formData = new FormData()
        formData.append('cartItemIds', cartItemIds)

        $.ajax({
            url: '/cartItemPriceServlet',
            type: 'Post',
            data: formData,
            processData: false,
            contentType: false,
            success: function (repsonse) {

                $('#temp-price').attr('data-value', repsonse.totalPrice)
                $('#temp-price').text(_formatPrice(repsonse.totalPrice))
            },
            error: function (response) {
                console.log(response)
            }
        })
    }
}

$('#checkoutBtn').on('click', function () {
    if (!$(this).prop('disabled')) {
        createOrder();
    }
})

function createOrder() {

    let ship = $('#delivery-price').attr('data-value');
    let addressId = $('input[name="address"]:checked').attr('id');
    if (ship > 0 && addressId > 0) {
        let unitshipVal = $("#unit-ship").val();

        if (unitshipVal != 'none') {
            if (unitshipVal == "ViettelPost") {
                unitshipVal = 1;
            } else if (unitshipVal == "GHN") {
                unitshipVal = 0;
            }
        }
        let cartItemIds = []
        $("input[name='cartItem']:checked").each(function () {
            cartItemIds.push($(this).val())
        })

        let formData = new FormData()
        formData.append('cartItemIds', cartItemIds)
        let shipVoucherId = $("input[name='0']").val()
        shipVoucherId = shipVoucherId > 0 ? shipVoucherId : 0
        let productVoucherId = $("input[name='1']").val()
        productVoucherId = productVoucherId > 0 ? productVoucherId : 0
        formData.append('shipVoucherId', shipVoucherId)
        formData.append('productVoucherId', productVoucherId)
        formData.append('ship', ship)
        formData.append('unitshipVal', unitshipVal)
        formData.append('addressId', addressId)
        $.ajax({
            url: '/order',
            type: 'Post',
            data: formData,
            processData: false,
            contentType: false,
            success: function (repsonse) {
                createToast(toastComponent(SUCCESS_ADD_ORDER_MESSAGE, "success"));
                updateTotalQuantity()
                cartItemIds.forEach(value => {
                    $('#tr' + value).remove()
                })
                getTotalProductPrice()
                getDecrease(0)
                getDecrease(1)
            },
            error: function (response) {
                console.log(response)
                createToast(toastComponent(ERROR_ADD_ORDER_MESSAGE, "danger"));
            }
        })
    }


}

function updateTotalProductPrice() {
    $("input[name='cartItem']").each(function () {
        $(this).on('change', function () {
            getTotalProductPrice()

        })
    })
}

function cartItemPriceComponent(productPrice, productDiscount) {
    if (productDiscount === 0) {
        return `<span class="price">${_formatPrice(productPrice)}₫</span>`;
    }

    return `
    <div>
      <span class="price">${_formatPrice(productPrice * (100 - productDiscount) / 100)}₫</span>
      <span class="ms-2 badge bg-info">-${productDiscount}%</span>
    </div>
    <small class="text-muted text-decoration-line-through">${_formatPrice(productPrice)}₫</small>
  `;
}

export function _formatPrice(value) {
    return parseFloat(value).toLocaleString('vi-VN')
}

function convertCartItemToHTML(cartItem) {
    let disable = "";
    // Kiểm tra xem số lượng sản phẩm trong giỏ hàng có lớn hơn
    // số lượng còn lại trong kho hay không
    if (cartItem.productQuantity < cartItem.quantity) {
        disable = 'disabled';
    }
    return `
    <tr id="tr${cartItem.id}">
    <td>
<!--    checkbox để lựa chọn đặt hàng-->
    <input id="isBuy${cartItem.id}" type="checkbox" value="${cartItem.id}" 
    name="cartItem" 
    class="form-check"
    style="width: 30px" 
    onchange="updateAplyVoucher()" ${disable} 
    title="Số lượng trong kho không đủ">
    </td>
      <td>
        <figure class="itemside">
          <div class="float-start me-3">
            <img
              src="${contextPath + (cartItem.productImageName ? ('/image/' + cartItem.productImageName) : '/img/80px.png')}"
              alt="${cartItem.productName}"
              width="80"
              height="80"
            >
          </div>
          <figcaption class="info row">
            <a href="${contextPath + '/product?id=' + cartItem.productId}" class="title">${cartItem.productName}</a>
            <span class="title"><strong>Thể loại:</strong> ${cartItem.category}</span>   
          </figcaption>
        </figure>
      </td>
      <td>
        <div class="price-wrap">
          ${cartItemPriceComponent(cartItem.productPrice, cartItem.productDiscount)}
        </div>
      </td>
      <td>
        <input type="number" value="${cartItem.quantity}" min="1" max="${cartItem.productQuantity}" class="form-control" id="quantity-cart-item-${cartItem.id}">
      </td>
      <td class="text-center text-nowrap">
        <button type="button" class="btn btn-success" id="update-cart-item-${cartItem.id}"  onclick="updateAplyVoucher()">Cập nhật</button>
        <button type="button" class="btn btn-danger ms-1" id="delete-cart-item-${cartItem.id}" onclick="updateAplyVoucher()">Xóa</button>
      </td>
    </tr>
  `;
}

$('#add_address-btn').on('click', function () {
    $('#add_address_content').css('display', 'block');
    $('#main_content').css('display', 'none');

})
$('#back').on('click', function () {
    $('#add_address_content').css('display', "none");
    $('#main_content').css('display', 'block');

})
$(document).ready(function () {

    $('#voucher_container').on('click', function (event) {
        // Kiểm tra xem phần tử được click có nằm trong thẻ div B hay không
        if (event.target.closest('#voucher_content')) {

        } else {
            $('#voucher_container').css('display', 'none')
        }
    });

    $('#close_voucher_container').on('click', function () {

        $('#voucher_container').css('display', 'none')

    });

})

function updateToTalPrice() {
    let totalPrice = parseFloat($('#temp-price').attr('data-value'));
    totalPrice -= parseFloat($('#voucher-product').attr('data-value'));
    totalPrice -= parseFloat($('#voucher-ship').attr('data-value'));
    totalPrice += parseFloat($('#delivery-price').attr('data-value'));
    $('#total-price').attr('data-value', totalPrice)
    $('#total-price').text(_formatPrice(totalPrice))

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
        $('#voucher-product').attr('data-value', 0)
        $('#voucher-ship').attr('data-value', 0)

    } else if (selectedVoucher.val() >= 0) {
        if (type == 0 && typeof $("input[name='infoship']:checked").val() == 'undefined') {
            alert('Vui lòng chọn phương thức vận chuyển')
            selectedVoucher.prop('checked', false)
        }
        if ((type == 0 && $("input[name='infoship']:checked").val() > 0) || type == 1) {
            let formData = new FormData();
            let ship = (type == 1) ? 0 : $("input[name='infoship']:checked").val()
            formData.append('voucherId', selectedVoucher.val());
            formData.append('cartItemIds', cartItemIds);
            formData.append('ship', ship);
            console.log('voucherId', selectedVoucher.val())
            $.ajax({
                url: "/voucherServlet",
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    console.log(response)
                    if (response.decrease > 0) {
                        if (type == 0) {
                            $('#voucher-ship').attr('data-value', (response.decrease))
                            $('#voucher-ship').text(_formatPrice(response.decrease))
                        } else {
                            $('#voucher-product').attr('data-value', (response.decrease))
                            $('#voucher-product').text(_formatPrice(response.decrease))
                        }
                        setTimeout(function () {
                            updateToTalPrice()
                        }, 500);
                    } else {
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

