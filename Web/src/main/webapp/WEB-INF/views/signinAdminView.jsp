<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <jsp:include page="_meta.jsp"/>
  <title><fmt:message key="dang_nhap_admin"/></title>
</head>

<body class="bg-light">

<section class="section-content mx-auto" style="margin: 100px 0; max-width: 380px;">
  <h3 class="mb-3 p-3 text-center bg-primary text-white rounded"><fmt:message key="shop_ban_sach"/></h3>
  <div class="card">
    <div class="card-body">
      <c:if test="${not empty requestScope.errorMessage}">
        <div class="alert alert-danger mb-3 small" role="alert">
            ${requestScope.errorMessage}
        </div>
      </c:if>
      <h4 class="card-title mb-3">Đăng nhập Admin</h4>
      <p class="small"><fmt:message key="chi_danh_cho_quan_tri_vien_va_nhan_vien"/></p>
      <form action="${pageContext.request.contextPath}/admin/signin" method="post">
        <div class="mb-3">
          <input name="username"
                 class="form-control ${not empty requestScope.violations.usernameViolations
                   ? 'is-invalid' : (not empty requestScope.values.username ? 'is-valid' : '')}"
                 placeholder="<fmt:message key="ten_dang_nhap"/>"
                 type="text"
                 autocomplete="off"
                 value="${requestScope.values.username}">
          <c:if test="${not empty requestScope.violations.usernameViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.usernameViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>
        <div class="mb-3">
          <input name="password"
                 class="form-control ${not empty requestScope.violations.passwordViolations
                   ? 'is-invalid' : (not empty requestScope.values.password ? 'is-valid' : '')}"
                 placeholder="<fmt:message key="mat_khau"/>"
                 type="password"
                 autocomplete="off"
                 value="${requestScope.values.password}">
          <c:if test="${not empty requestScope.violations.passwordViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.passwordViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>
        <button type="submit" class="btn btn-primary w-100"><fmt:message key="dang_nhap"/></button>
      </form>
    </div> <!-- card-body.// -->
  </div> <!-- card .// -->
</section> <!-- section-content.// -->
</body>

</html>
