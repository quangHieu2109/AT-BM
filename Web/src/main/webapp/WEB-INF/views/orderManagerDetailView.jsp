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
  <title><fmt:message key="thong_tin_don_hang"/> #${requestScope.order.id}</title>
</head>

<body>

<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content">
  <div class="container">
    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="thong_tin_don_hang"/></h3>
    </header> <!-- section-heading.// -->

    <div class="card mb-5">
      <div class="card-body">
        <dl class="row">
          <dt class="col-md-3"><fmt:message key="ma_don_hang"/></dt>
          <dd class="col-md-9">${requestScope.order.id}</dd>

          <dt class="col-md-3"><fmt:message key="ngay_tao"/></dt>
          <dd class="col-md-9">
            ${requestScope.order.createdAt}
          </dd>

          <dt class="col-md-3"><fmt:message key="ngay_cap_nhat"/></dt>
          <dd class="col-md-9">
            ${requestScope.order.updatedAt}
          </dd>

          <dt class="col-md-3"><fmt:message key="nguoi_dat_nhan"/></dt>
          <dd class="col-md-9">
            <a href="${pageContext.request.contextPath}/admin/userManager/detail?id=${requestScope.order.user.id}">
              ${requestScope.order.user.username}
            </a>
            (${requestScope.order.user.fullname})
          </dd>

          <dt class="col-md-3"><fmt:message key="hinh_thuc_giao_hang"/></dt>
          <dd class="col-md-9">
           <fmt:message key="${requestScope.order.deliveryMethod == 1 ? 'giao_tieu_chuan' : 'giao_nhanh'}"/>
          </dd>

          <dt class="col-md-3"><fmt:message key="tam_tinh"/></dt>
          <dd class="col-md-9">
            <fmt:formatNumber pattern="#,##0"
                              value="${requestScope.order.totalPrice - requestScope.order.deliveryPrice}"/>₫
          </dd>

          <dt class="col-md-3"><fmt:message key="phi_van_chuyen"/></dt>
          <dd class="col-md-9">
            <fmt:formatNumber pattern="#,##0" value="${requestScope.order.deliveryPrice}"/>₫
          </dd>

          <dt class="col-md-3"><fmt:message key="tong_tien"/></dt>
          <dd class="col-md-9">
            <fmt:formatNumber pattern="#,##0" value="${requestScope.order.totalPrice}"/>₫
          </dd>

          <dt class="col-md-3"><fmt:message key="trang_thai"/></dt>
          <dd class="col-md-9">
            <c:choose>
              <c:when test="${requestScope.order.status == 1}">
                <span class="badge bg-warning text-dark"><fmt:message key="dang_giao_hang"/></span>
              </c:when>
              <c:when test="${requestScope.order.status == 2}">
                <span class="badge bg-success"><fmt:message key="giao_hang_thanh_cong"/></span>
              </c:when>
              <c:when test="${requestScope.order.status == 3}">
                <span class="badge bg-danger"><fmt:message key="huy_don_hang"/></span>
              </c:when>
            </c:choose>
          </dd>

          <dt class="col-md-3"><fmt:message key="san_pham"/></dt>
          <dd class="col-md-9">
            <div class="table-responsive border rounded">
              <table class="table table-borderless">
                <thead class="text-muted">
                <tr class="small text-uppercase">
                  <th scope="col" style="min-width: 280px;"><fmt:message key="san_pham"/></th>
                  <th scope="col" style="min-width: 150px;"><fmt:message key="gia"/></th>
                  <th scope="col" style="min-width: 150px;"><fmt:message key="so_luong"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="orderItem" items="${requestScope.order.orderItems}">
                  <tr>
                    <td>
                      <figure class="itemside">
                        <div class="float-start me-3">
                          <c:choose>
                            <c:when test="${empty orderItem.product.imageName}">
                              <img width="80"
                                   height="80"
                                   src="${pageContext.request.contextPath}/img/280px.png"
                                   alt="280px.png">
                            </c:when>
                            <c:otherwise>
                              <img width="80"
                                   height="80"
                                   src="${pageContext.request.contextPath}/image/${orderItem.product.imageName}"
                                   alt="${orderItem.product.imageName}">
                            </c:otherwise>
                          </c:choose>
                        </div>
                        <figcaption class="info">
                          <a href="${pageContext.request.contextPath}/product?id=${orderItem.product.id}"
                             target="_blank">
                              ${orderItem.product.name}
                          </a>
                        </figcaption>
                      </figure>
                    </td>
                    <td>
                      <div class="price-wrap">
                        <c:choose>
                          <c:when test="${orderItem.discount == 0}">
                            <span class="price">
                              <fmt:formatNumber pattern="#,##0" value="${orderItem.price}"/>₫
                            </span>
                          </c:when>
                          <c:otherwise>
                            <div>
                              <span class="price">
                                <fmt:formatNumber
                                        pattern="#,##0"
                                        value="${orderItem.price * (100 - orderItem.discount) / 100}"/>₫
                              </span>
                              <span class="ms-2 badge bg-info">
                                -<fmt:formatNumber pattern="#,##0" value="${orderItem.discount}"/>%
                              </span>
                            </div>
                            <small class="text-muted text-decoration-line-through">
                              <fmt:formatNumber pattern="#,##0" value="${orderItem.price}"/>₫
                            </small>
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </td>
                    <td>${orderItem.quantity}</td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div> <!-- table.responsive-md.// -->
          </dd>
        </dl>
      </div>
    </div> <!-- card.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
