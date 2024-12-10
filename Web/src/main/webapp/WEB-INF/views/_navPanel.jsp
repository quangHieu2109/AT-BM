<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<aside class="col-md-3 mb-md-0 mb-3">
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <nav class="list-group">
    <a class="list-group-item ${param.active == 'USER' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/user" role="button">
      <fmt:message key="tai_khoan"/>

    </a>
    <a class="list-group-item ${param.active == 'ORDER' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/order" role="button">
      <fmt:message key="don_hang_cua_toi"/>
    </a>
    <a class="list-group-item ${param.active == 'WISHLIST' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/wishlist">
      <fmt:message key="san_pham_yeu_thich"/>
    </a>
    <a class="list-group-item ${param.active == 'CHANGE_PASSWORD' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/changePassword" role="button">
      <fmt:message key="doi_mat_khau"/>
    </a>
    <a class="list-group-item ${param.active == 'SETTING' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/setting" role="button">
      <fmt:message key="thiet_dat"/>
    </a>
    <a class="list-group-item" href="${pageContext.request.contextPath}/signout" role="button"><fmt:message key="dang_xuat"/></a>
  </nav>
</aside> <!-- col.// -->
