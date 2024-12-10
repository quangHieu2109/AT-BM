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

  <title><fmt:message key="quan_ly_nguoi_dung"/></title>
  <jsp:include page="_meta.jsp"/>
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

    <header class="section-heading py-4 d-flex justify-content-between">
      <h3 class="section-title"><fmt:message key="quan_ly_nguoi_dung"/></h3>
      <a class="btn btn-primary"
         href="${pageContext.request.contextPath}/admin/userManager/create"
         role="button"
         style="height: fit-content;">
        <fmt:message key="them_nguoi_dung"/>
      </a>
    </header> <!-- section-heading.// -->

    <main class="table-responsive-xl mb-5">
      <table class="table table-bordered table-striped table-hover align-middle">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">ID</th>
          <th scope="col"><fmt:message key="ten_dang_nhap"/></th>
          <th scope="col"><fmt:message key="ho_va_ten"/></th>
          <th scope="col">Email</th>
          <th scope="col"><fmt:message key="so_dien_thoai"/></th>
          <th scope="col"><fmt:message key="gioi_tinh"/></th>
          <th scope="col"><fmt:message key="quyen"/></th>
          <th scope="col" style="width: 225px;"><fmt:message key="thao_tac"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" varStatus="loop" items="${requestScope.users}">
          <tr>
            <th scope="row">${loop.index + 1}</th>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.fullname}</td>
            <td>${user.email}</td>
            <td>${user.phoneNumber}</td>
           <td><fmt:message key="${user.gender == 0 ? 'nam' : 'nu'}"/></td>
            <td>
              <c:choose>
                <c:when test="${user.role == 'ADMIN'}"><fmt:message key="quan_tri_vien"/></c:when>
                <c:when test="${user.role == 'EMPLOYEE'}"><fmt:message key="nhan_vien"/></c:when>
                <c:when test="${user.role == 'CUSTOMER'}"><fmt:message key="khach_hang"/></c:when>
              </c:choose>
            </td>
            <td class="text-center text-nowrap">
              <a class="btn btn-primary me-2"
                 href="${pageContext.request.contextPath}/admin/userManager/detail?id=${user.id}"
                 role="button">
                <fmt:message key="xem"/>
              </a>
              <a class="btn btn-success me-2"
                 href="${pageContext.request.contextPath}/admin/userManager/update?id=${user.id}"
                 role="button">
                <fmt:message key="sua"/>
              </a>
              <a class="btn btn-danger"
                 href="${pageContext.request.contextPath}/admin/userManager/delete?id=${user.id}"
                 role="button"
                 onclick="return confirm('<fmt:message key="ban_co_muon_xoa"/>?')">
                <fmt:message key="xoa"/>
              </a>
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
               href="${pageContext.request.contextPath}/admin/userManager?page=${requestScope.page - 1}">
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
                     href="${pageContext.request.contextPath}/admin/userManager?page=${i}">
                      ${i}
                  </a>
                </li>
              </c:otherwise>
            </c:choose>
          </c:forEach>

          <li class="page-item ${requestScope.page == requestScope.totalPages ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.request.contextPath}/admin/userManager?page=${requestScope.page + 1}">
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
