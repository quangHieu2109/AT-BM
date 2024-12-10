import createToast, { toastComponent } from "./toast.js";
import { setTotalCartItemsQuantity } from "./header.js";


// STATIC DATA
const contextPathMetaTag = document.querySelector("meta[name='contextPath']");
const currentUserIdMetaTag = document.querySelector("meta[name='currentUserId']");
const productIdMetaTag = document.querySelector("meta[name='productId']");

const quantityInput = document.querySelector("#cart-item-quantity");
const productTitleElement = document.querySelector(".title");

// MESSAGES
const REQUIRED_SIGNIN_MESSAGE = "Vui lòng đăng nhập để thực hiện thao tác!";
const SUCCESS_ADD_CART_ITEM_MESSAGE = (quantity, productTitle) =>
  `Đã thêm thành công ${quantity} sản phẩm ${productTitle} vào giỏ hàng!`;
const FAILED_ADD_CART_ITEM_MESSAGE = "Đã có lỗi truy vấn!";
const SUCCESS_ADD_WISHLIST_ITEM_MESSAGE = (productTitle) =>
  `Đã thêm thành công sản phẩm ${productTitle} vào danh sách yêu thích!`;
const FAILED_ADD_WISHLIST_ITEM_MESSAGE = "Đã có lỗi truy vấn!";

// UTILS



// EVENT HANDLERS
function noneSigninEvent() {
  createToast(toastComponent(REQUIRED_SIGNIN_MESSAGE));
}

async function addWishlistItemBtnEvent() {
  let formData = new FormData();
  formData.append('userId', currentUserIdMetaTag.content);
  formData.append('productId', productIdMetaTag.content);
  $.ajax({
    url:contextPathMetaTag.content + "/wishlist",
    type:"PUT",
    data: formData,
    processing: false,
    contentType: false,
    success: function (response) {
      createToast(toastComponent(
          SUCCESS_ADD_WISHLIST_ITEM_MESSAGE(productTitleElement.innerText), "success"));
      addWishlistItemBtn.disabled = true;
      updateTotalQuantity()
    },
    error: function (response) {
      console.log(response)
      createToast(toastComponent(FAILED_ADD_WISHLIST_ITEM_MESSAGE, "danger"));
    }

  })

}
 function _fetchPostAddCartItem() {
  $.ajax({
    url: contextPathMetaTag.content + "/cartItem",
    type: 'POST',
    data:{
      userId: currentUserIdMetaTag.content,
      productId: productIdMetaTag.content,
      quantity: quantityInput.value,
    },
    success: function (response) {
      updateTotalQuantity()
      return response.status
    },
    error: function (response) {
      console.log(response)

      return response.status
    }
  })

}
function buyNowBtnEvent() {
  $.ajax({
    url: contextPathMetaTag.content + "/cartItem",
    type: 'POST',
    data:{
      userId: currentUserIdMetaTag.content,
      productId: productIdMetaTag.content,
      quantity: quantityInput.value,
    },
    success: function (response) {
      updateTotalQuantity()
      window.location.href = contextPathMetaTag.content + "/cart";
    },
    error: function (response) {
      console.log(response)

      createToast(toastComponent(FAILED_ADD_CART_ITEM_MESSAGE, "danger"));
    }
  })

}

//Thêm sản phẩm vào giỏ hàng
function addCartItemBtnEvent() {
  $.ajax({
    url:"/cartItem",
    type: 'POST',
    data:{
      productId: productIdMetaTag.content,
      quantity: quantityInput.value,
    },
    success: function (response) {
      //Cập nhật số lượng trong giỏ hàng
      updateTotalQuantity()
      //Thông báo thành công
      createToast(toastComponent(
          SUCCESS_ADD_CART_ITEM_MESSAGE(quantityInput.value,
              productTitleElement.innerText), "success"));
    },
    error: function (response) {
      console.log(response)
      //Thông báo thất bại
      createToast(toastComponent(FAILED_ADD_CART_ITEM_MESSAGE, "danger"));
    }
  })
}

// MAIN
const addWishlistItemBtn = document.querySelector("#add-wishlist-item");
const buyNowBtn = document.querySelector("#buy-now");
const addCartItemBtn = document.querySelector("#add-cart-item");

if (currentUserIdMetaTag) {
  addWishlistItemBtn.addEventListener("click", addWishlistItemBtnEvent)
  buyNowBtn.addEventListener("click", buyNowBtnEvent);
  addCartItemBtn.addEventListener("click", addCartItemBtnEvent);
} else {
  addWishlistItemBtn.addEventListener("click", noneSigninEvent);
  buyNowBtn.addEventListener("click", noneSigninEvent);
  addCartItemBtn.addEventListener("click", noneSigninEvent);
}

function updateTotalQuantity() {
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