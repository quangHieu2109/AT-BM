<%--
  Created by IntelliJ IDEA.
  User: dmx
  Date: 4/29/2024
  Time: 1:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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
    <title><fmt:message key="quan_ly_don_hang"/></title>

    <link href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="shortcut icon" type="image/x-icon">

    <!-- Bootstrap v5.0.1 -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.js" type="text/javascript"></script>

    <!-- Bootstrap Icons v1.5.0 -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" type="text/css" rel="stylesheet">

    <!-- Custom Styles -->
    <link href="${pageContext.request.contextPath}/css/styles.css" type="text/css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://cdn.datatables.net/2.0.5/js/dataTables.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.0.5/css/dataTables.dataTables.css">
    <style>
        input[type="radio"]:checked + label {
            background-color: #52e74c; /* Màu sắc bạn muốn áp dụng khi label được chọn */
        }
        #detail_table__content{
            justify-content: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<jsp:include page="_headerAdmin.jsp"/>
<div class="row justify-content-around">
    <div class="w-auto">
        <input type="radio" name="category" value="0" id="0" checked hidden>
        <label for="0" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadTable(0)"><fmt:message key="dat_hang_thanh_cong"/></label>
    </div>
    <div class="w-auto">
        <input type="radio" name="category" value="1" id="1" hidden>
        <label for="1" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadTable(1)"><fmt:message key="dang_giao_hang"/></label>
    </div>
    <div class="w-auto">
        <input type="radio" name="category" value="2" id="2" hidden>
        <label for="2" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadTable(2)"><fmt:message key="giao_hang_thanh_cong"/></label>
    </div>
    <div class="w-auto">
        <input type="radio" name="category" value="3" id="3" hidden>
        <label for="3" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadTable(3)"><fmt:message key="da_huy"/></label>
    </div>
    <div class="w-auto">
        <input type="radio" name="category" value="4" id="4" hidden>
        <label for="4" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadTable(4)"><fmt:message key="tra_hang"/></label>
    </div>
    <div class="w-auto">
        <input type="radio" name="category" id="detail" hidden>
        <label class="btn p-3 pt-1 pb-1 mt-2"><fmt:message key="chi_tiet_don_hang"/></label>
    </div>

</div>
<div class="container">
    <div id="my_table__content">
        <table id="my_table" class="table-bordered table-hover">
            <thead>
            <tr>
                <th>ID Order</th>
                <th>ID Customer</th>
                <th>Delivery method</th>
                <th>Delivery price</th>
                <th>Products price</th>
                <th>Total price</th>
                <th>Create at</th>
                <th>Update at</th>
                <th>Update status</th>
                <th>Operation</th>
            </tr>

            </thead>
            <tbody>

            </tbody>

        </table>
    </div>
    <div id="detail_table__content">

    </div>
</div>
<script src="${pageContext.request.contextPath}/js/orderManager.js" type="text/javascript" async></script>
</body>
</html>
