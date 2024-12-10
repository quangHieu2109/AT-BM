<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <jsp:include page="_meta.jsp"/>
  <title><fmt:message key="them_voucher"/></title>
  <style>
    .w-maxcontent{
      width: max-content;
    }
    .ml-0{
      width: 20%;
      margin-left: 0;
    }
  .ml-20px{
    margin-left: 20px;
  }
  </style>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content">
  <div class="container">
    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="them_voucher"/></h3>
    </header> <!-- section-heading.// -->

    <main class="row mb-5">
      <form class="col-lg-9 m-auto" method="POST" action="${pageContext.request.contextPath}/createVoucherSevlet"
            enctype="multipart/form-data" id="my_form">
        <c:if test="${not empty requestScope.successMessage}">
          <div class="alert alert-success mb-3" role="alert">
              ${requestScope.successMessage}
          </div>
        </c:if>
        <c:if test="${not empty requestScope.errorMessage}">
          <div class="alert alert-danger mb-3" role="alert">
              ${requestScope.errorMessage}
          </div>
        </c:if>
        <div class="mb-3">
          <label for="voucher-name" class="form-label"><fmt:message key="ten_voucher"/> <span class="text-danger">*</span></label>
            <label class="form-label" id="voucher-name_error"></label>
            <input type="text"
                 class="form-control "
                 id="voucher-name"
                 name="voucherName"
                 required>
        </div>
        <div class="mb-3">
          <label for="voucher-code" class="form-label"><fmt:message key="ma_voucher"/> <span class="text-danger">*</span></label>
          <label class="form-label text-danger" id="voucher-code_error"></label>
          <div class="input-group">
                <input type="text"
                   class="form-control "
                   id="voucher-code"
                   name="voucherCode"
                   required>
            <button type="button" class="btn btn-primary" onclick="randomVoucherCode()">Random</button>
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label"><fmt:message key="the_loai_hang_hoa_ap_dung"/></label>
          <div class="ml-20px">
            <div class="row ">
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="1" id="ctg1">
                <label for="ctg1" class=" w-maxcontent"><fmt:message key="sach_giao_khoa"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="2" id="ctg2">
                <label for="ctg2" class=" w-maxcontent"><fmt:message key="sach_khoa_hoc"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="3" id="ctg3">
                <label for="ctg3" class=" w-maxcontent"><fmt:message key="truyen_tranh"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="4" id="ctg4">
                <label for="ctg4" class=" w-maxcontent"><fmt:message key="tieu_thuyet"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="5" id="ctg5">
                <label for="ctg5" class=" w-maxcontent"><fmt:message key="truyen_ngan"/></label>
              </div>
            </div>
            <div class="row ">
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="6" id="ctg6">
                <label for="ctg6" class=" w-maxcontent"><fmt:message key="truyen_dai"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="7" id="ctg7">
                <label for="ctg7" class=" w-maxcontent"><fmt:message key="sach_giao_trinh"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="8" id="ctg8">
                <label for="ctg8" class=" w-maxcontent"><fmt:message key="bao_in"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="9" id="ctg9">
                <label for="ctg9" class=" w-maxcontent"><fmt:message key="tap_chi"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="10" id="ctg10">
                <label for="ctg10" class=" w-maxcontent"><fmt:message key="tap_san"/></label>
              </div>
            </div>
            <div class="row ">
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="11" id="ctg11">
                <label for="ctg11" class=" w-maxcontent"><fmt:message key="sach_nau_an"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="12" id="ctg12">
                <label for="ctg12" class=" w-maxcontent"><fmt:message key="sach_ky_thuat"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="13" id="ctg13">
                <label for="ctg13" class=" w-maxcontent"><fmt:message key="sach_nong_nghiep"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="14" id="ctg14">
                <label for="ctg14" class=" w-maxcontent"><fmt:message key="sach_thieu_nhi"/></label>
              </div>
              <div class="row w-maxcontent col-lg-2 ml-0">
                <input type="checkbox" name="categorys" class="form-check w-maxcontent" value="15" id="ctg15">
                <label for="ctg15" class=" w-maxcontent"><fmt:message key="sach_ky_nang_song"/></label>
              </div>
            </div>
          </div>
          </div>

        <div class="mb-3">
          <label class="form-label d-block"><fmt:message key="loai_voucher"/> <span class="text-danger">*</span></label>
          <div class="form-check d-inline-block me-4">
            <input class="form-check-input "
                   type="radio"
                   name="typeVoucher"
                   id="ship"
                   value="0" checked
                   required>
            <label class="form-check-label" for="ship">Voucher freeship</label>
          </div>
          <div class="form-check d-inline-block">
            <input class="form-check-input "
                   type="radio"
                   name="typeVoucher"
                   id="shop"
                   value="1"
                   required>
            <label class="form-check-label" for="shop">Voucher shop</label>
          </div>
        </div>
        <div class="mb-3">
          <label for="voucher-description" class="form-label"><fmt:message key="mo_ta_voucher"/></label>
            <label id="voucher-description_error" class="form-label text-danger"></label>
            <textarea class="form-control "
                    id="voucher-description"
                    rows="10"
                    name="description"></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label text-danger" id="quantity_erro"></label>
          <label for="quantity" class="form-label"><fmt:message key="so_luong"/><span class="text-danger">*</span></label>

            <input type="number"
                   class="form-control "
                   id="quantity"
                   name="quantity"
                   value=""
                   min="0"
                   step="1"
                   required>

        </div>
        <div class="mb-3">
          <label class="form-label text-danger" id="percent-decrease_erro"></label>
          <label for="percent-decrease" class="form-label"><fmt:message key="phan_tram_giam"/> <span class="text-danger">*</span></label>
          <div class="input-group ">
            <input type="number"
                   class="form-control "
                   id="percent-decrease"
                   name="percentDecrease"
                   value=""
                   min="0"
                   step="1"
                   required>
            <span class="input-group-text">%</span>
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label text-danger" id="max-decrease_erro"></label>
          <label for="max-decrease" class="form-label"><fmt:message key="giam_toi_da"/> <span class="text-danger">*</span></label>
          <div class="input-group ">
            <input type="number"
                   class="form-control "
                   id="max-decrease"
                   name="maxDecrease"
                   value=""
                   min="0"
                   step="1"
                   required>
            <span class="input-group-text">đ</span>
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label text-danger" id="min-price_erro"></label>
          <label for="min-price" class="form-label"><fmt:message key="don_gia_toi_thieu"/><span class="text-danger">*</span></label>
          <div class="input-group ">
            <input type="number"
                   class="form-control "
                   id="min-price"
                   name="minPrice"
                   value=""
                   min="0"
                   step="1"
                   required>
            <span class="input-group-text">đ</span>
          </div>
        </div>

        <div class="mb-3">
          <label for="voucher-imageName" class="form-label"><fmt:message key="anh_voucher"/></label>
            <label id="voucher-imageName_error" class="form-label text-danger"></label>
            <input type="file"
                 class="form-control"
                 id="voucher-imageName"
                 name="image"
                 accept="image/*">
        </div>


        <div class="mb-3">
          <label for="voucher-startsAt" class="form-label"><fmt:message key="ngay_bat_dau"/></label>
            <label id="voucher-startsAt_error" class="form-label text-danger"></label>
            <input type="datetime-local"
                 class="form-control "
                 id="voucher-startsAt"
                 name="voucherStartsAt"
                 value="">
        </div>
        <div class="mb-3">
          <label for="voucher-endAt" class="form-label"><fmt:message key="ngay_ket_thuc"/></label>
            <label id="voucher-endAt_error" class="form-label text-danger"></label>
            <input type="datetime-local"
                 class="form-control "
                 id="voucher-endAt"
                 name="voucherEndAt"
            value="">
        </div>
        <button type="submit" class="btn btn-primary me-2 mb-3" id="submit_form">
          <fmt:message key="them"/>
        </button>

      </form>
    </main>
  </div> <!-- container.// -->
</section> <!-- section-content.// -->
<script src="${pageContext.request.contextPath}/js/voucher.js" type="text/javascript"></script>
<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
