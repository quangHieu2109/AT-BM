<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">

<head>
  <jsp:include page="_meta.jsp"/>
  <title>Xác thực tài khoản</title>
  <!-- Custom Scripts -->
<%--  <script src="${pageContext.request.contextPath}/js/address.js" type="text/javascript"></script>--%>

</head>

<body>
<jsp:include page="_header.jsp"/>
<div class="container w-25 mx-auto">
  <h1>Xác thực tài khoản</h1>
  <form id="verificationForm" method="post" action="/accuracy">
    <div class="form-group ">
      <label>Mã xác nhận đã được gửi đến email ${sessionScope.currentUser.email}. Vui lòng kiểm tra email để lấy mã xác nhận!</label>
      <label for="verificationCode">Nhập mã xác nhận:</label>
      <label class="text-danger">
        <c:if test="${requestScope.error != null}">
          ${requestScope.error}
        </c:if>

      </label>
      <input type="text" class="form-control mt-2 mb-2" id="verificationCode" name="verificationCode" >
    </div>
    <div class="row justify-content-between">
        <button type="button" class="btn btn-primary w-auto" id="resend"><a href="${pageContext.request.contextPath}/accuracy" class="text-decoration-none text-white">Gửi lại mã</a></button>

      <button type="submit" class="btn btn-primary w-auto" id="confirm">Xác nhận</button>
    </div>
  </form>
</div>
<script src="${pageContext.request.contextPath}/js/address.js" type="text/javascript"></script>
<jsp:include page="_footer.jsp"/>
</body>

</html>
