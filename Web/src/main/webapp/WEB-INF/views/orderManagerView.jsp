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
  <title><fmt:message key="quan_ly_don_hang"/></title>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content">
  <div class="container">
    <c:if test="${not empty sessionScope.successMessage}">
      <div class="alert alert-success mb-0 mt-4" role="alert">
          ${sessionScope.successMessage}
      </div>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
      <div class="alert alert-danger mb-0 mt-4" role="alert">
          ${sessionScope.errorMessage}
      </div>
    </c:if>
    <c:remove var="successMessage" scope="session"/>
    <c:remove var="errorMessage" scope="session"/>

    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="quan_ly_don_hang"/></h3>
    </header> <!-- section-heading.// -->

    <main class="table-responsive-xl mb-5">
      <table class="table table-bordered table-striped table-hover align-middle">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col"><fmt:message key="ma"/></th>
          <th scope="col"><fmt:message key="nguoi_dung"/></th>
          <th scope="col"><fmt:message key="ngay_tao"/></th>
          <th scope="col"><fmt:message key="ngay_cap_nhat"/></th>
          <th scope="col"><fmt:message key="so_san_pham"/></th>
          <th scope="col"><fmt:message key="tong_tien"/></th>
          <th scope="col"><fmt:message key="trang_thai"/></th>
          <th scope="col" style="width: 250px;"><fmt:message key="thao_tac"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" varStatus="loop" items="${requestScope.orders}">
          <tr>
            <th scope="row">${loop.index + 1}</th>
            <td>${order.id}</td>
            <td>
              <a href="${pageContext.request.contextPath}/admin/userManager/detail?id=${order.user.id}">
                  ${order.user.username}
              </a>
              (${order.user.fullname})
            </td>
            <td>${order.createdAt}</td>
            <td>${order.updatedAt}</td>
            <td>${order.orderItems.size()}</td>
            <td class="text-end">
              <c:choose>
                <c:when test="${order.deliveryMethod == 2}">
                  <i class="bi bi-truck me-1" title="<fmt:message key="giao_nhanh"/>"></i>
                </c:when>
              </c:choose>
              <fmt:formatNumber pattern="#,##0" value="${order.totalPrice}"/>₫
            </td>
            <td>
              <c:choose>
                <c:when test="${order.status == 1}">
                  <span class="badge bg-warning text-dark"><fmt:message key="dang_giao_hang"/></span>
                </c:when>
                <c:when test="${order.status == 2}">
                  <span class="badge bg-success"><fmt:message key="giao_hang_thanh_cong"/></span>
                </c:when>
                <c:when test="${order.status == 3}">
                  <span class="badge bg-danger"><fmt:message key="huy_don_hang"/></span>
                </c:when>
              </c:choose>
            </td>
            <td class="text-center text-nowrap">
              <a class="btn btn-primary me-2"
                 href="${pageContext.request.contextPath}/admin/orderManager/detail?id=${order.id}"
                 role="button">
                <fmt:message key="xem"/>
              </a>
              <button type="submit"
                      class="btn ${order.status == 2 || order.status == 3 ? 'btn-secondary' : 'btn-success'} me-2"
                      form="update-confirm-${order.id}" ${order.status == 2 || order.status == 3 ? 'disabled' : ''}
                      onclick="return confirm('Bạn có muốn xác nhận đã giao đơn hàng này?')"
                      title="<fmt:message key="xac_nhan_da_giao"/>">
                <i class="bi bi-check-circle"></i>
              </button>
              <button type="submit"
                      class="btn  ${order.status == 2 || order.status == 3 ? 'btn-secondary' : 'btn-danger'} me-2"
                      form="update-cancel-${order.id}" ${order.status == 2 || order.status == 3 ? 'disabled' : ''}
                      onclick="return confirm('Bạn có muốn hủy đơn hàng này?')"
                      title="<fmt:message key="huy_don_hang"/>">
                <i class="bi bi-x-circle"></i>
              </button>
              <button type="submit"
                      class="btn  ${order.status == 1 ? 'btn-secondary' : 'btn-warning'}"
                      form="update-reset-${order.id}" ${order.status == 1 ? 'disabled' : ''}
                      onclick="return confirm('Bạn có muốn đặt lại trạng thái là đang giao hàng cho đơn hàng này?')"
                      title="<fmt:message key="dat_lai_trang_thai_la_dang_giao_hang"/>">
                <i class="bi bi-arrow-clockwise"></i>
              </button>
              <form action="${pageContext.request.contextPath}/admin/orderManager/update" method="post"
                    id="update-confirm-${order.id}">
                <input type="hidden" name="id" value="${order.id}">
                <input type="hidden" name="action" value="CONFIRM">
              </form>
              <form action="${pageContext.request.contextPath}/admin/orderManager/update" method="post"
                    id="update-cancel-${order.id}">
                <input type="hidden" name="id" value="${order.id}">
                <input type="hidden" name="action" value="CANCEL">
              </form>
              <form action="${pageContext.request.contextPath}/admin/orderManager/update" method="post"
                    id="update-reset-${order.id}">
                <input type="hidden" name="id" value="${order.id}">
                <input type="hidden" name="action" value="RESET">
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </main>

    <c:if test="${requestScope.totalPages != 0}">
      <nav class="mt-3 mb-5">
        <ul class="pagination justify-content-center">
          <li class="page-item ${requestScope.page == 1 ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.request.contextPath}/admin/orderManager?page=${requestScope.page - 1}">
              <fmt:message key="trang_truoc"/>
            </a>
          </li>

          <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
            <c:choose>
              <c:when test="${requestScope.page == i}">
                <li class="page-item active">
                  <a class="page-link">${i}</a>
                </li>
              </c:when>
              <c:otherwise>
                <li class="page-item">
                  <a class="page-link"
                     href="${pageContext.request.contextPath}/admin/orderManager?page=${i}">
                      ${i}
                  </a>
                </li>
              </c:otherwise>
            </c:choose>
          </c:forEach>

          <li class="page-item ${requestScope.page == requestScope.totalPages ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.request.contextPath}/admin/orderManager?page=${requestScope.page + 1}">
              <fmt:message key="trang_sau"/>
            </a>
          </li>
        </ul>
      </nav>
    </c:if>
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footerAdmin.jsp"/>
</body>

</html>
