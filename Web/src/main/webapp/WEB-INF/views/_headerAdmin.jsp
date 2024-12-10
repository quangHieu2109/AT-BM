<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="servletPath" scope="page" value="${requestScope['javax.servlet.forward.servlet_path']}"/>
<header class="section-header">
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <section class="header-main border-bottom">
    <div class="container">
      <div class="w-75 m-auto d-flex justify-content-end">
        <span onclick="changeLanguage()" style="cursor: pointer"><i class="bi bi-translate"></i><fmt:message key="lang"/></span>
      </div>
      <div class="row align-items-center">
        <div class="col-lg-8">
          <a class="text-body" href="${pageContext.request.contextPath}/admin">
            <h3><span class="badge bg-primary">Admin</span> <fmt:message key="shop_ban_sach"/></h3>
          </a>
        </div> <!-- col.// -->
        <div class="col-lg-4 justify-content-end">
          <ul class="nav col-12 col-lg-auto my-2 my-lg-0 justify-content-center justify-content-lg-end text-small">
            <li>
              <a href="${pageContext.request.contextPath}/" class="nav-link text-body" target="_blank">
                <i class="bi bi-window d-block text-center fs-3"></i>
                Client
              </a>
            </li>
            <li class="d-flex align-items-center">
              <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                  <span>Xin chào <strong>${sessionScope.currentUser.fullname}</strong>!</span>
                  <a class="btn btn-light ms-2" href="${pageContext.request.contextPath}/admin/signout" role="button">
                    <fmt:message key="dang_xuat"/>
                  </a>
                </c:when>
                <c:otherwise>
                  <a class="btn btn-primary w-auto" href="${pageContext.request.contextPath}/admin/signin" role="button">
                    <fmt:message key="dang_nhap"/>
                  </a>
                </c:otherwise>
              </c:choose>
            </li>
          </ul>
        </div> <!-- col.// -->
      </div> <!-- row.// -->
    </div> <!-- container.// -->
  </section> <!-- header-main.// -->
</header> <!-- section-header.// -->

<nav class="navbar navbar-main navbar-expand-lg navbar-light border-bottom">
  <div class="container">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/admin/userManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/admin/userManager">
            <i class="bi bi-people"></i> <fmt:message key="quan_ly_nguoi_dung"/>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/logManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/logManagerServlet">
            <img src="${pageContext.request.contextPath}/img/log-icon.png" alt="" width="17px"> <fmt:message key="quan_ly_log"/>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/admin/categoryManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/admin/categoryManager">
            <i class="bi bi-tags"></i> <fmt:message key="quan_ly_the_loai"/>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/admin/productManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/admin/productManager">
            <i class="bi bi-book"></i> <fmt:message key="quan_li_san_pham"/>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/admin/reviewManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/admin/reviewManager">
            <i class="bi bi-star"></i> <fmt:message key="quan_li_danh_gia"/>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/admin/orderManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/admin/orderManager">
            <i class="bi bi-inboxes"></i> <fmt:message key="quan_ly_don_hang"/>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${fn:startsWith(servletPath, '/admin/orderManager') ? 'active' : ''}"
             href="${pageContext.request.contextPath}/admin/voucherManager/createVoucherSevlet">
            <img src="${pageContext.request.contextPath}/img/ticket-perforated.svg" alt=""> Voucher
          </a>
        </li>
        <a class="nav-link ${fn:startsWith(servletPath, '/admin/statiscalManager') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/statiscalManager/product">
          <i class="bi bi-graph-up"></i> <fmt:message key="quan_ly_thong_ke"/>
        </a>
        </li>
      </ul>

    </div>
  </div> <!-- container.// -->
</nav> <!-- navbar-main.// -->
<script>
  function changeLanguage(){
    $.ajax({
      url:"/changeLanguage",
      type:"GET",
      success:function(){
        window.location.reload()
      }
    })
  }
</script>