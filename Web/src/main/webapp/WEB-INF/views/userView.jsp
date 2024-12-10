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
  <title><fmt:message key="tai_khoan"/></title>
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-pagetop bg-light">
  <div class="container">
    <h2 class="title-page"><fmt:message key="tai_khoan"/></h2>
  </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->

<section class="section-content padding-y">
  <div class="container">
    <div class="row">
      <c:choose>
        <c:when test="${empty sessionScope.currentUser}">
          <p>
            <fmt:message key="vui_long"/> <a href="${pageContext.request.contextPath}/signin"><fmt:message key="dang_nhap"/></a><fmt:message key="de_xem_thong_tin_tai_khoan"/>.
          </p>
        </c:when>
        <c:otherwise>
          <jsp:include page="_navPanel.jsp">
            <jsp:param name="active" value="USER"/>
          </jsp:include>

          <main class="col-md-9">
            <article class="card">
              <div class="card-body">
                <div>
                  <strong>${sessionScope.currentUser.fullname}</strong>
                  <p>${sessionScope.currentUser.email}</p>
                </div>
                <hr>
<%--                <div>--%>
<%--                  <p class="bi bi-map d-block lh-lg">--%>
<%--                    <fmt:message key="dia_chi"/>:--%>
<%--                    <br>--%>
<%--&lt;%&ndash;                      ${sessionScope.currentUser.address}&ndash;%&gt;--%>
<%--                  </p>--%>
<%--                </div>--%>
                <article class="card-group">
                  <figure class="card bg-light">
                    <div class="p-3">
                      <h4 class="title">${requestScope.countCartItemQuantity}</h4>
                      <span><fmt:message key="san_pham_trong_gio"/></span>
                    </div>
                  </figure>
                  <figure class="card bg-light">
                    <div class="p-3">
                      <h4 class="title">${requestScope.countOrder}</h4>
                      <span><fmt:message key="don_hang"/></span>
                    </div>
                  </figure>
                  <figure class="card bg-light">
                    <div class="p-3">
                      <h4 class="title">${requestScope.countOrderDeliver}</h4>
                      <span><fmt:message key="don_hang_dang_giao"/></span>
                    </div>
                  </figure>
                  <figure class="card bg-light">
                    <div class="p-3">
                      <h4 class="title">${requestScope.countOrderReceived}</h4>
                      <span><fmt:message key="don_hang_da_nhan"/></span>
                    </div>
                  </figure>
                </article> <!-- row .//  -->
              </div> <!-- card-body.// -->
            </article>
          </main> <!-- col.// -->
        </c:otherwise>
      </c:choose>
    </div> <!-- row.// -->
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footer.jsp"/>
</body>

</html>
