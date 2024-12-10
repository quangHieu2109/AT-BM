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
  <title><fmt:message key="thong_tin_san_pham"/> #${requestScope.product.id}</title>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content">
  <div class="container">
    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="thong_tin_san_pham"/></h3>
    </header> <!-- section-heading.// -->

    <div class="card mb-5">
      <div class="card-body">
        <dl class="row">
          <dt class="col-md-3">ID</dt>
          <dd class="col-md-9">${requestScope.product.id}</dd>

          <dt class="col-md-3"><fmt:message key="ten_san_pham"/></dt>
          <dd class="col-md-9">
            <a href="${pageContext.request.contextPath}/product?id=${requestScope.product.id}" target="_blank">
              ${requestScope.product.name}
            </a>
          </dd>

          <dt class="col-md-3"><fmt:message key="the_loai"/></dt>
          <dd class="col-md-9">
            <a href="${pageContext.request.contextPath}/category?id=${requestScope.category.id}" target="_blank">
              ${requestScope.category.name}
            </a>
          </dd>

          <dt class="col-md-3"><fmt:message key="gia_goc"/></dt>
          <dd class="col-md-9"><fmt:formatNumber pattern="#,##0" value="${requestScope.product.price}"/>₫</dd>

          <dt class="col-md-3"><fmt:message key="khuyen_mai"/></dt>
          <dd class="col-md-9"><fmt:formatNumber pattern="#,##0" value="${requestScope.product.discount}"/>%</dd>

          <dt class="col-md-3"><fmt:message key="gia_ban"/></dt>
          <dd class="col-md-9">
            <c:choose>
              <c:when test="${requestScope.product.discount == 0}">
                <fmt:formatNumber pattern="#,##0" value="${requestScope.product.price}"/>₫
              </c:when>
              <c:otherwise>
                <fmt:formatNumber
                        pattern="#,##0"
                        value="${requestScope.product.price * (100 - requestScope.product.discount) / 100}"/>₫
              </c:otherwise>
            </c:choose>
          </dd>

          <dt class="col-md-3"><fmt:message key="ton_kho"/></dt>
          <dd class="col-md-9">${requestScope.product.quantity}</dd>

          <dt class="col-md-3"><fmt:message key="luot_mua"/></dt>
          <dd class="col-md-9">${requestScope.product.totalBuy}</dd>

          <dt class="col-md-3"><fmt:message key="tac_gia"/></dt>
          <dd class="col-md-9">${requestScope.product.author}</dd>

          <dt class="col-md-3"><fmt:message key="so_trang"/></dt>
          <dd class="col-md-9">${requestScope.product.pages}</dd>

          <dt class="col-md-3"><fmt:message key="nha_xuat_ban"/></dt>
          <dd class="col-md-9">${requestScope.product.publisher}</dd>

          <dt class="col-md-3"><fmt:message key="nam_xuat_ban"/></dt>
          <dd class="col-md-9">${requestScope.product.yearPublishing}</dd>

          <dt class="col-md-3"><fmt:message key="mo_ta_san_pham"/></dt>
          <dd class="col-md-9">${requestScope.product.description}</dd>

          <dt class="col-md-3"><fmt:message key="hinh_san_pham"/></dt>
          <dd class="col-md-9">
            <c:choose>
              <c:when test="${empty requestScope.product.imageName}">
                <img width="280"
                     class="img-thumbnail"
                     src="${pageContext.request.contextPath}/img/280px.png"
                     alt="280px.png">
              </c:when>
              <c:otherwise>
                <img width="280"
                     class="img-thumbnail"
                     src="${pageContext.request.contextPath}/image/${requestScope.product.imageName}"
                     alt="${requestScope.product.imageName}">
              </c:otherwise>
            </c:choose>
          </dd>

          <dt class="col-md-3"><fmt:message key="duoc_giao_dich"/>?</dt>
          <dd class="col-md-9">${requestScope.product.shop == 0 ? '<fmt:message key="khong"/>' : '<fmt:message key="co"/>'}</dd>

          <dt class="col-md-3"><fmt:message key="ngay_tao"/></dt>
          <dd class="col-md-9">${requestScope.product.createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")) }</dd>

          <dt class="col-md-3"><fmt:message key="ngay_cap_nhat"/></dt>
          <dd class="col-md-9">${requestScope.product.updatedAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")) }</dd>
          <dt class="col-md-3"><fmt:message key="ngay_bat_dau_khuyen_mai"/></dt>
          <dd class="col-md-9">${requestScope.product.startsAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")) }</dd>
          <dt class="col-md-3"><fmt:message key="ngay_ket_thuc_khuyen_mai"/></dt>
          <dd class="col-md-9">${requestScope.product.endsAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")) }</dd>
          Trong đoạn mã trên:
        </dl>
      </div>
    </div> <!-- card.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
