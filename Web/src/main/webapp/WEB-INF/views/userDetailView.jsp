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
  <title><fmt:message key="thong_tin_nguoi_dung"/> #${requestScope.user.id}</title>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content">
  <div class="container">
    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="thong_tin_nguoi_dung"/></h3>
    </header> <!-- section-heading.// -->

    <div class="card mb-5">
      <div class="card-body">
        <dl class="row">
          <dt class="col-md-3">ID</dt>
          <dd class="col-md-9">${requestScope.user.id}</dd>

          <dt class="col-md-3"><fmt:message key="ten_dang_nhap"/></dt>
          <dd class="col-md-9">${requestScope.user.username}</dd>

          <dt class="col-md-3"><fmt:message key="ho_va_ten"/></dt>
          <dd class="col-md-9">${requestScope.user.fullname}</dd>

          <dt class="col-md-3">Email</dt>
          <dd class="col-md-9">${requestScope.user.email}</dd>

          <dt class="col-md-3"><fmt:message key="so_dien_thoai"/></dt>
          <dd class="col-md-9">${requestScope.user.phoneNumber}</dd>

          <dt class="col-md-3"><fmt:message key="gioi_tinh"/></dt>
         <dd class="col-md-9"><fmt:message key="${requestScope.user.gender == 0 ? 'nam' : 'nu'}"/></dd>

<%--          <dt class="col-md-3"><fmt:message key="dia_chi"/></dt>--%>
<%--          <dd class="col-md-9">${requestScope.user.address}</dd>--%>
        </dl>
      </div>
    </div> <!-- card.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
