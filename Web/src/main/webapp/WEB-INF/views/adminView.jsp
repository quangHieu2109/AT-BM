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
  <title><fmt:message key="trang_chu_admin"/></title>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content padding-y">
  <div class="container">
    <div class="card bg-light">
      <div class="card-body p-5">
        <h1 class="display-5 mb-5"><fmt:message key="quan_li_shop_ban_sach"/></h1>
        <div class="row">
          <div class="col-6 col-lg-3">
            <figure class="card">
              <div class="p-3">
                <h4 class="title">${requestScope.totalUsers}</h4>
                <span><fmt:message key="nguoi_dung"/></span>
              </div>
            </figure>
          </div>
          <div class="col-6 col-lg-3">
            <figure class="card">
              <div class="p-3">
                <h4 class="title">${requestScope.totalCategories}</h4>
                <span><fmt:message key="the_loai_sach"/></span>
              </div>
            </figure>
          </div>
          <div class="col-6 col-lg-3">
            <figure class="card">
              <div class="p-3">
                <h4 class="title">${requestScope.totalProducts}</h4>
                <span><fmt:message key="sach"/></span>
              </div>
            </figure>
          </div>
          <div class="col-6 col-lg-3">
            <figure class="card">
              <div class="p-3">
                <h4 class="title">${requestScope.totalOrders}</h4>
                <span><fmt:message key="don_hang"/></span>
              </div>
            </figure>
          </div>
        </div>
      </div>
    </div> <!-- card.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
