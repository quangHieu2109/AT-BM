<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
    <fmt:setLocale value="vi_VN"/>
    <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
        <fmt:setLocale value="en_US"/>
    </c:if>
    <fmt:setBundle basename="lang"/>
    <title><fmt:message key="gio_hang"/></title>
    <jsp:include page="_meta.jsp"/>


    <!-- Custom Scripts -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>


    <script src="${pageContext.request.contextPath}/js/toast.js" type="module"></script>
    <script src="${pageContext.request.contextPath}/js/cart2.js" type="module"></script>
    <script src="${pageContext.request.contextPath}/js/address.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/voucher.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/feeship.js" type="module"></script>
    <style>
        /* Modal Container */
        .modal-container {
            display: none; /* Ẩn modal mặc định */
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5); /* Màu nền mờ */
            align-items: center;
            justify-content: center;
            z-index: 9999;
        }

        /* Nội dung Modal */
        .modal-content {
            background-color: #fff;
            border-radius: 5px;
            width: 400px;
            padding: 20px;
        }

        /* Tiêu đề Modal */
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .modal-title {
            font-size: 18px;
            margin: 0;
        }

        .close-btn {
            background-color: transparent;
            border: none;
            font-size: 18px;
            cursor: pointer;
        }

        /* Thân Modal */
        .modal-body {
            margin-top: 10px;
        }

        /* Chân Modal */
        .modal-footer {
            margin-top: 20px;
            text-align: right;
        }

        .btn {
            padding: 10px 20px;
            cursor: pointer;
        }


        #add_address_content {
            display: none;
        }

        .row_address {
            margin: 5px 0px;
            border-radius: 10px;
            align-items: center;
            border: 1px solid #ddd;
            display: flex;
        }

        .row_address label {
            margin-top: 0.5rem;
            margin-left: 0.5rem;
        }

        #address_content {
            height: 300px;
        }

        #infoShip {
            max-height: 300px;
        }

        #add_voucher_ship, #add_voucher_product {
            width: 90%;
            padding: 5px;
            border: 1px solid #eae9e9;
            border-radius: 5px;
            margin: auto;
            cursor: pointer;
            margin-bottom: 5px;
        }

        #voucher_container {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(208, 208, 208, 0.94);
            display: none;
            z-index: 3;
        }

        #voucher_content {
            width: 50%;
            margin: auto;
            height: 650px;
            overflow: auto;
            border-radius: 10px;
            padding: 10px;
            margin-top: 50px;
            background-color: #fff;

        }

        #voucher_body {


        }

        .voucher_item {
            margin: 10px;
            border: 1px solid #ecebeb;
            border-radius: 5px;
            padding: 10px 5px;

        }

        input[name='0']:checked + div {
            background-color: rgb(199 200 202 / 47%);
        }

        input[name='1']:checked + div {
            background-color: rgb(199 200 202 / 47%);
        }

        .voucher_content-title {
            display: block;
            width: max-content;
            font-size: 30px;
            font-weight: 600;
            margin: auto;

        }

        input[disabled][name='cartItem']:hover:after {
            content: 'Số lượng trong kho không đủ';
            position: absolute;
            background-color: #333;
            color: #fff;
            padding: 5px;
            border-radius: 5px;
            font-size: 12px;
            z-index: 1;
        }
    </style>
</head>

<body>
<jsp:include page="_header.jsp"/>
<fmt:message key="gio_hang"/>
<section class="section-pagetop bg-light">
    <div class="container">
        <h2 class="title-page"></h2>
    </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->
<p id="idCheck" hidden="hidden">${requestScope.idCheck}</p>
<section class="section-content padding-y position-relative">
    <div class="container">
        <div id="main_content">
            <div class="row">

                <c:choose>
                    <c:when test="${empty sessionScope.currentUser}">
                        <p>
                            <fmt:message key="vui_long"/> <a
                                href="${pageContext.request.contextPath}/signin"><fmt:message key="dang_nhap"/></a>
                            <fmt:message key="de_su_dung_chuc_nang_gio_hang"/>.
                        </p>
                    </c:when>
                    <c:otherwise>
                        <main class="col-lg-9 mb-lg-0 mb-3">
                            <div class="card" style="height: 600px; overflow: auto">
                                <div class="table-responsive-xl">
                                    <table class="cart-table table table-borderless">
                                        <thead class="text-muted">
                                        <tr class="small text-uppercase">
                                            <th scope="col" style="min-width: 100px;">Mua</th>
                                            <th scope="col" style="min-width: 250px;">Sản phẩm</th>
                                            <th scope="col" style="min-width: 160px;">Giá</th>
                                            <th scope="col" style="min-width: 150px;">Số lượng</th>
                                            <th scope="col" style="min-width: 100px;"></th>
                                        </tr>
                                        </thead>
                                        <tbody id="cart_body"></tbody>
                                    </table>
                                </div> <!-- table.responsive-md.// -->
                                <div id="cart-table"></div>

                                <div class="card-body border-top">
                                    <button type="button" class="btn btn-primary float-end" id="checkoutBtn" disabled>
                                        <fmt:message key="dat_hang"/></button>
                                    <a href="${pageContext.request.contextPath}/" class="btn btn-light"><fmt:message
                                            key="tiep_tuc_mua_sam"/></a>
                                </div> <!-- card-body.// -->

                            </div> <!-- card.// -->
                        </main>
                        <!-- col.// -->

                        <aside class="col-lg-3">
                            <div class="card mb-3">
                                <div class="card-body">
                                    <b class="card-title">Địa chỉ nhận hàng</b>
                                    <div id="address_content" class="row overflow-auto">

                                    </div>
                                        <%--                  <select class="form-select " onscroll="4" id="selectProvince">--%>
                                        <%--                    <option selected value="${requestScope.province}">${requestScope.province}</option>--%>

                                        <%--                  </select>--%>
                                        <%--                  <select class="form-select mt-2 mb-2" onscroll="4" id="selectDistrict">--%>
                                        <%--                    <option selected value="${requestScope.district}">${requestScope.district}</option>--%>

                                        <%--                  </select>--%>
                                        <%--                  <select class="form-select " onscroll="4" id="selectWard">--%>
                                        <%--                    <option selected value="${requestScope.ward}">${requestScope.ward}</option>--%>

                                        <%--                  </select>--%>
                                    <div class="row w-auto justify-content-center border-info m-auto p-3"
                                         id="add_address-btn">
                                        <i class="bi bi-plus-square w-auto"></i>
                                        Thêm địa chỉ
                                    </div>
                                </div>

                            </div>
                            <div class="card mb-3">
                                <div class="card-body">
                                    <b class="card-title"><fmt:message key="don_vi_van_chuyen"/></b>
                                    <select class="form-select" id="unit-ship" onchange="changeUnitShip()">
                                        <option value="none" selected>---</option>
                                        <option value="ViettelPost">ViettelPost</option>
                                        <option value="GHN">GHN</option>
                                    </select>
                                    <form>
                                        <div class="form-check mb-2 disabled p-0 mt-3 overflow-auto" id="infoShip">
                                        </div>
                                    </form>

                                        <%--                <form>--%>
                                        <%--                  <div class="form-check mb-2">--%>
                                        <%--                    <input class="form-check-input" type="radio" name="delivery-method"--%>
                                        <%--                           id="delivery-method-1" value="1" disabled>--%>
                                        <%--                    <label class="form-check-label" for="delivery-method-1"><fmt:message key="giao_tieu_chuan"/></label>--%>
                                        <%--                  </div>--%>
                                        <%--                  <div class="form-check mb-2">--%>
                                        <%--                    <input class="form-check-input" type="radio" name="delivery-method"--%>
                                        <%--                           id="delivery-method-2" value="2" disabled>--%>
                                        <%--                    <label class="form-check-label" for="delivery-method-2"><fmt:message key="giao_nhanh"/></label>--%>
                                        <%--                  </div>--%>
                                        <%--                </form>--%>
                                </div> <!-- card-body.// -->
                            </div> <!-- card.// -->

                            <div class="card">
                                <div class="card-body">
                                    <dl class="row mb-0">
                                        <dt class="col-xxl-6 col-lg-12 col-6"><fmt:message key="tam_tinh"/>:</dt>
                                        <dd class="col-xxl-6 col-lg-12 col-6 text-end mb-3"><span id="temp-price"
                                                                                                  data-value="0">0</span>₫
                                        </dd>
                                        <dt class="col-xxl-6 col-lg-12 col-6">Voucher:</dt>
                                        <dd class="col-xxl-6 col-lg-12 col-6 text-end mb-3">-<span id="voucher-product"
                                                                                                   data-value="0">0</span>₫
                                        </dd>
                                        <div id="add_voucher_product" onclick="getVoucher(1)">
                                            <i class="bi bi-plus-square m-2"></i> Thêm voucher giảm giá
                                        </div>
                                        <dt class="col-xxl-6 col-lg-12 col-6"><fmt:message key="phi_van_chuyen"/>:</dt>
                                        <dd class="col-xxl-6 col-lg-12 col-6 text-end mb-3"><span id="delivery-price"
                                                                                                  data-value="0">---</span>₫
                                        </dd>
                                        <dt class="col-xxl-6 col-lg-12 col-6">Voucher:</dt>
                                        <dd class="col-xxl-6 col-lg-12 col-6 text-end mb-3">-<span id="voucher-ship"
                                                                                                   data-value="0">0</span>₫
                                        </dd>
                                        <div id="add_voucher_ship" onclick="getVoucher(0)">
                                            <i class="bi bi-plus-square m-2"></i> Thêm voucher freeship
                                        </div>
                                        <dt class="col-xxl-6 col-lg-12 col-6"><fmt:message key="tong_cong"/>:</dt>
                                        <dd class="col-xxl-6 col-lg-12 col-6 text-end mb-3"><strong><span
                                                id="total-price" data-value="0">0</span>₫</strong></dd>
                                    </dl>
                                </div> <!-- card-body.// -->
                            </div> <!-- card.// -->

                        </aside>
                        <!-- col.// -->
                    </c:otherwise>
                </c:choose>

            </div> <!-- row.// -->
        </div>
        <div id="add_address_content" class="w-50 m-auto">
            <div class="row w-auto" id="back">
                <i class="bi bi-arrow-left-square w-auto"></i>
                Quay lại
            </div>
            <div class="card-body">
                <b class="card-title">Địa chỉ nhận hàng</b>
                <label class="text-danger h-25" id="province-error"></label>
                <select class="form-select " onscroll="4" id="selectProvince">
                    <option selected value="${requestScope.province}">${requestScope.province}</option>

                </select>
                <label class="text-danger h-25" id="district-error"></label>

                <select class="form-select mt-2 mb-2" onscroll="4" id="selectDistrict">
                    <option selected value="${requestScope.district}">${requestScope.district}</option>

                </select>
                <label class="text-danger h-25" id="ward-error"></label>
                <select class="form-select " onscroll="4" id="selectWard">
                    <option selected value="${requestScope.ward}">${requestScope.ward}</option>

                </select>
                <label class="text-danger h-25" id="house_number-error"></label>
                <div class="d-flex ">
                    <label class="form-label">Số nhà</label>
                    <input type="text" class="form-control" id="house_number">
                </div>
                <div class="btn btn-primary w-auto m-auto d-block mt-3" onclick="submitAddAddress()"
                     id="submit-add_address">Thêm
                </div>
            </div>
        </div>
    </div> <!-- container -->
    <div id="voucher_container">
        <div class="container" id="voucher_content">
            <div class="row">

                <span class="title voucher_content-title col-lg-11">Danh sách voucher</span>
                <button type="button" class="btn col-lg-1" style="color: red; font-size: 30px; font-weight: 600"
                        id="close_voucher_container">X
                </button>
            </div>
            <div class="container" id="voucher_body_ship"></div>
            <div class="container" id="voucher_body_product">

            </div>
        </div>

    </div>
    <!-- Modal Xác Thực Key -->

    <!-- Modal -->
    <div id="authKeyModal" class="modal-container">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xác thực đơn hàng</h5>
                <button type="button" id="closeModal" class="close-btn">X</button>
            </div>
            <div class="modal-body">
                <c:choose>
                    <c:when test="${0 > 0}">
                        <!-- Hiển thị form nếu điều kiện đúng -->
                        <div id="authKeyForm">
                            <input type="text" id="authKeyInput" class="form-control"
                                   placeholder="Nhập private key của bạn để xác thực đơn hàng...">
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Hiển thị thông báo nếu điều kiện sai -->
                        <div id="authKeyMessage" class="text-center">
                            <p>Bạn chưa có key. Vui lòng tạo key mới để sử dụng chức năng này.</p>
                            <a href=${pageContext.request.contextPath}/createAuthenticator
                               class="btn btn-primary justify-content-center">Tạo key mới</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="modal-footer">
                <c:if test="${0 > 0}">
                    <div class="modal-footer justify-content-around">
                        <button type="button" id="confirmAuthKeyBtn" class="btn btn-primary">Xác nhận</button>
                        <button type="button" id="loadFromFile" class="btn btn-primary">Nhập Key từ file</button>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</section> <!-- section-content.// -->

<jsp:include page="_footer.jsp"/>

<div class="toast-container position-fixed bottom-0 start-0 p-3"></div> <!-- toast-container.// -->

</body>

</html>
