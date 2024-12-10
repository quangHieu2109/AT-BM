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
  <title><fmt:message key="thong_tin_danh_gia"/> #${requestScope.productReview.id}</title>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content">
  <div class="container">
    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="thong_tin_danh_gia"/></h3>
    </header> <!-- section-heading.// -->

    <div class="card mb-5">
      <div class="card-body">
        <dl class="row">
          <dt class="col-md-3">ID</dt>
          <dd class="col-md-9">${requestScope.productReview.id}</dd>

          <dt class="col-md-3"><fmt:message key="nguoi_dung"/></dt>
          <dd class="col-md-9">
            <a href="${pageContext.request.contextPath}/admin/userManager/detail?id=${requestScope.productReview.user.id}">
              ${requestScope.productReview.user.username}
            </a>
            (${requestScope.productReview.user.fullname})
          </dd>

          <dt class="col-md-3"><fmt:message key="ngay_tao"/></dt>
          <dd class="col-md-9">
            ${requestScope.productReview.createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))}
          </dd>

          <dt class="col-md-3"><fmt:message key="ngay_cap_nhat"/></dt>
          <dd class="col-md-9">
            ${requestScope.productReview.updatedAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))}
          </dd>

          <dt class="col-md-3"><fmt:message key="so_sao"/></dt>
          <dd class="col-md-9">
            <div class="ratting-star">
              <span class="rating-stars">
                <c:forEach begin="1" end="5" step="1" var="i">
                  <i class="bi bi-star-fill ${i <= requestScope.productReview.ratingScore ? 'active' : ''}"></i>
                </c:forEach>
              </span>
              <span>(${requestScope.productReview.ratingScore})</span>
            </div>
          </dd>

          <dt class="col-md-3"><fmt:message key="noi_dung_danh_gia"/></dt>
          <dd class="col-md-9">${requestScope.productReview.content}</dd>

          <dt class="col-md-3"><fmt:message key="san_pham"/></dt>
          <dd class="col-md-9">
            <a href="${pageContext.request.contextPath}/product?id=${requestScope.productReview.product.id}#review"
               target="_blank">
              ${requestScope.productReview.product.name}
            </a>
          </dd>
        </dl>
      </div>
    </div> <!-- card.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
