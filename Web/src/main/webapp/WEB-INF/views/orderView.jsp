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
  <title><fmt:message key="don_hang"/></title>
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-pagetop bg-light">
  <div class="container">
    <h2 class="title-page"><fmt:message key="don_hang"/></h2>
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

            <div class="table-responsive-xxl">
              <table class="table table-bordered table-striped table-hover align-middle">
                <thead>
                <tr>
                  <th scope="col" style="min-width: 125px;"><fmt:message key="ma_don_hang"/></th>
                  <th scope="col" style="min-width: 100px;"><fmt:message key="ngay_mua"/></th>
                  <th scope="col" style="min-width: 300px;"><fmt:message key="san_pham"/></th>
                  <th scope="col" style="min-width: 100px;"><fmt:message key="tong_tien"/></th>
                  <th scope="col" style="min-width: 175px;"><fmt:message key="trang_thai_don_hang"/></th>
                  <th scope="col"><fmt:message key="thao_tac"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${requestScope.orders}">
                  <tr>
                    <th scope="row">${order.id}</th>
                    <td>${order.createdAt}</td>
                    <td>${order.name}</td>
                    <td><fmt:formatNumber pattern="#,##0" value="${order.total}"/>₫</td>
                    <td>
                      <c:choose>
                        <c:when test="${order.status == 0}">
                          <span class="badge bg-info text-dark"><fmt:message key="dat_hang_thanh_cong"/></span>
                        </c:when>
                        <c:when test="${order.status == 1}">
                          <span class="badge bg-warning text-dark"><fmt:message key="dang_giao_hang"/></span>
                        </c:when>
                        <c:when test="${order.status == 2}">
                          <span class="badge bg-success"><fmt:message key="giao_hang_thanh_cong"/></span>
                        </c:when>
                        <c:when test="${order.status == 3}">
                          <span class="badge bg-danger"><fmt:message key="huy_don_hang"/></span>
                        </c:when>
                        <c:when test="${order.status == 4}">
                          <span class="badge bg-danger"><fmt:message key="tra_hang"/></span>
                        </c:when>
                      </c:choose>
                    </td>
                    <td class="text-center text-nowrap">
                      <a class="btn btn-primary me-2"
                         href="${pageContext.request.contextPath}/orderDetail?id=${order.id}"
                         role="button">
                        <fmt:message key="xem_don_hang"/>
                      </a>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>

            <c:if test="${requestScope.totalPages != 0}">
              <nav class="mt-4">
                <ul class="pagination">
                  <li class="page-item ${requestScope.page == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/order?page=${requestScope.page - 1}">
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
                             href="${pageContext.request.contextPath}/order?page=${i}">
                              ${i}
                          </a>
                        </li>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>

                  <li class="page-item ${requestScope.page == requestScope.totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/order?page=${requestScope.page + 1}">
                      <fmt:message key="trang_sau"/>
                    </a>
                  </li>
                </ul>
              </nav>
            </c:if>

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
