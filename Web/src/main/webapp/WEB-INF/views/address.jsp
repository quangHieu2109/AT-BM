<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head>
    <title>Address</title>
    <jsp:include page="_meta.jsp"/>
    <script src="${pageContext.request.contextPath}/js/address.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/addAddress.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="_header.jsp"/>
<div class="container flex-column">
    <div class="card-body">
        <b class="card-title">Địa chỉ nhận hàng</b>
        <div>
<label for="houseNumber">Số nhà</label>
            <label></label><input type="text" id="houseNumber" name="houseNumber">

        </div>

        <select class="form-select " onscroll="4" id="selectProvince">
            <option selected value="${requestScope.province}">${requestScope.province}</option>

        </select>
        <select class="form-select mt-2 mb-2" onscroll="4" id="selectDistrict">
            <option selected value="${requestScope.district}">${requestScope.district}</option>

        </select>
        <select class="form-select " onscroll="4" id="selectWard">
            <option selected value="${requestScope.ward}">${requestScope.ward}</option>
        </select>
    </div>
    <button class="btn btn-primary" onclick="addAddress()">Thêm</button>
</div>


<jsp:include page="_footer.jsp"/>
</body>
</html>
