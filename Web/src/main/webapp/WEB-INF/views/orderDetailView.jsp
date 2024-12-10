<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
  <jsp:include page="_meta.jsp"/>
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <title><fmt:message key="thong_tin_don_hang"/> #${requestScope.order.id}</title>
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-pagetop bg-light">
  <div class="container">
    <h2 class="title-page"><fmt:message key="thong_tin_don_hang"/> #${requestScope.order.id}</h2>
  </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->

<section class="section-content padding-y">
  <div class="container">
    <div class="row">
      <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
          <jsp:include page="_navPanel.jsp">
            <jsp:param name="active" value="ORDER"/>
          </jsp:include>

          <main class="col-md-9">
            <article class="card mb-4">

              <header class="card-header">
                <strong class="d-inline-block me-4"><fmt:message key="ma_don_hang"/>: ${requestScope.order.id}</strong>
                <span><fmt:message key="ngay_mua"/>: ${requestScope.createdAt}</span>
                <c:choose>
                  <c:when test="${requestScope.order.status == 0}">
                    <span class="badge bg-warning text-dark float-end"><fmt:message key="dat_hang_thanh_cong"/></span>
                  </c:when>
                  <c:when test="${requestScope.order.status == 1}">
                    <span class="badge bg-warning text-dark float-end"><fmt:message key="dang_giao_hang"/></span>
                  </c:when>
                  <c:when test="${requestScope.order.status == 2}">
                    <span class="badge bg-success float-end"><fmt:message key="giao_hang_thanh_cong"/></span>
                  </c:when>
                  <c:when test="${requestScope.order.status == 3}">
                    <span class="badge bg-danger float-end"><fmt:message key="huy_don_hang"/></span>
                  </c:when>
                  <c:when test="${requestScope.order.status == 4}">
                    <span class="badge bg-danger float-end"><fmt:message key="tra_hang"/></span>
                  </c:when>
                </c:choose>
              </header> <!-- card-header.// -->

              <div class="card-body pb-0">
                <div class="row">
                  <div class="col-lg-8">
                    <h6 class="text-muted"><fmt:message key="dia_chi_nguoi_nhan"/></h6>
                    <p class="lh-lg">
                        ${sessionScope.currentUser.fullname} <br>
                          <fmt:message key="so_dien_thoai"/>: ${sessionScope.currentUser.phoneNumber} <br>
<%--                          <fmt:message key="dia_chi"/>: ${sessionScope.currentUser.address}--%>
                    </p>
                  </div>
                  <div class="col-lg-4">
                    <h6 class="text-muted"><fmt:message key="hinh_thuc_thanh_toan"/></h6>
                    <span class="text-success">
                    <i class="fab fa-lg fa-cc-visa"></i>
                    <fmt:message key="${requestScope.order.deliveryMethod == 1 ? 'giao_tieu_chuan' : 'giao_nhanh'}"/>

                  </span>
                    <p class="lh-lg">
                      <fmt:message key="tam_tinh"/>: <fmt:formatNumber pattern="#,##0" value="${requestScope.tempPrice}"/>₫ <br>
                      <fmt:message key="phi_van_chuyen"/>: <fmt:formatNumber pattern="#,##0" value="${requestScope.order.deliveryPrice}"/>₫
                      <br>
                      <strong>
                        <fmt:message key="tong_cong"/>: <fmt:formatNumber
                              pattern="#,##0"
                              value="${requestScope.tempPrice + requestScope.order.deliveryPrice}"/>₫
                      </strong>
                    </p>
                  </div>
                </div> <!-- row.// -->
              </div> <!-- card-body.// -->

              <hr class="m-0">

              <div class="table-responsive">
                <table class="cart-table table table-borderless">
                  <thead class="text-muted">
                  <tr class="small text-uppercase">
                    <th scope="col" style="min-width: 280px;"><fmt:message key="san_pham"/></th>
                    <th scope="col" style="min-width: 150px;"><fmt:message key="gia"/></th>
                    <th scope="col" style="min-width: 150px;"><fmt:message key="so_luong"/></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="orderItem" items="${requestScope.orderItems}">
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

              <form action="${pageContext.request.contextPath}/orderDetail" method="post">
                <div class="card-footer py-3">
                  <a href="#" class="btn btn-primary me-2"><fmt:message key="theo_doi_don_hang"/></a>
                  <input type="hidden" name="id" value="${requestScope.order.id}">
                  <button type="submit"
                          class="btn btn-danger ${requestScope.order.status !=0 ? "disabled" : ""}"
                          onclick="return confirm('<fmt:message key="ban_co_muon_huy_don_hang"/>?')">
                    <fmt:message key="huy_don_hang"/>
                  </button>

                </div> <!-- card-footer.// -->
              </form>
              <form action="${pageContext.request.contextPath}/orderDetail" method="post">
                <div class="card-footer py-3">
                  <input type="hidden" name="type" value="buyagain">
                  <input type="hidden" name="id" value="${requestScope.order.id}">
                  <button class="btn btn-warning ${requestScope.order.status != 2 ? "disabled" : ""}">
                    <fmt:message key="mua_lai"/>
                  </button>
                </div> <!-- card-footer.// -->
              </form>


            </article>
          </main> <!-- col.// -->
        </c:when>
        <c:otherwise>
          <p>
            <fmt:message key="vui_long"/> <a href="${pageContext.request.contextPath}/signin"><fmt:message key="dang_nhap"/></a> <fmt:message key="de_su_dung_trang_nay"/>.
          </p>
        </c:otherwise>
      </c:choose>
    </div> <!-- row.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footer.jsp"/>
</body>

</html>
