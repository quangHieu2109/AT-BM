<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<header class="section-header">
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <c:set var="accuracy" value="0"></c:set>

  <c:if test="${sessionScope.currentUser.accuracy}">
    <c:set var="accuracy" value="1"></c:set>
  </c:if>
  <input type="hidden" value="${accuracy}" id="accuracy">
  <section class="header-main border-bottom">
    <div class="container">
      <div class="w-75 m-auto d-flex justify-content-end">
        <span onclick="changeLanguage()" style="cursor: pointer"><i class="bi bi-translate"></i><fmt:message key="lang"/></span>
      </div>
      <div class="row align-items-center">
        <div class="col-lg-3 py-3">
          <a class="text-body" href="${pageContext.request.contextPath}/">
            <h3><fmt:message key="shop_ban_sach"/></h3>
          </a>
        </div> <!-- col.// -->
        <div class="col-lg-4 col-xl-5 ${empty sessionScope.currentUser ? 'mb-3 mb-lg-0' : ''}">
          <form action="${pageContext.request.contextPath}/search" method="post" class="search">
            <div class="input-group w-100">
              <input type="text"
                     class="form-control"
                     placeholder="<fmt:message key="nhap_tu_khoa_can_tim"/> ..."
                     name="q"
                     value="${requestScope.query}">
              <button class="btn btn-primary" type="submit">
                <i class="bi bi-search"></i>
              </button>
            </div>
          </form>
        </div> <!-- col.// -->
        <div class="col-lg-5 col-xl-4">
          <c:if test="${not empty sessionScope.currentUser}">
            <ul class="nav col-12 col-lg-auto my-2 my-lg-0 justify-content-center justify-content-lg-end text-small">
              <li>
                <a href="${pageContext.request.contextPath}/user" class="nav-link text-body">
                  <i class="bi bi-person d-block text-center fs-3"></i>
                  <fmt:message key="tai_khoan"/>
                </a>
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/order" class="nav-link text-body">
                  <i class="bi bi-list-check d-block text-center fs-3"></i>
                  <fmt:message key="don_hang"/>
                </a>
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/cart" class="nav-link text-body position-relative">
                  <span id="total-cart-items-quantity" class="position-absolute top-0 end-0 mt-2 badge rounded-pill bg-primary">
                    ...



                  </span>
                  <i class="bi bi-cart d-block text-center fs-3 position-relative"></i>
                  <fmt:message key="gio_hang"/>
                </a>
              </li>
		<c:if test="${!sessionScope.currentUser.isCustomer()}">

              <li class=" m-auto"><a href="${pageContext.request.contextPath}/admin" class="btn btn-secondary">Admin</a></li>
              </c:if>
            </ul>
          </c:if>
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
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            <strong><i class="bi bi-list"></i> <fmt:message key="danh_muc_san_pham"/></strong>
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
            <li><a class="dropdown-item" href="#"><fmt:message key="sach_giao_khoa"/></a></li>
            <li><a class="dropdown-item" href="#"><fmt:message key="sach_khoa_hoc"/></a></li>
            <li><a class="dropdown-item" href="#"><fmt:message key="truyen_tranh"/></a></li>
            <li><a class="dropdown-item" href="#"><fmt:message key="tieu_thuyet"/></a></li>
            <li>
              <hr class="dropdown-divider">
            </li>
            <li><a class="dropdown-item" href="#"><fmt:message key="tat_ca_danh_muc"/></a></li>
          </ul>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#"><fmt:message key="san_pham_moi"/></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#"><fmt:message key="san_pham_ban_chay"/></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#"><fmt:message key="khuyen_mai"/></a>
        </li>
      </ul>
      <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
          <span>Xin chào <strong>${sessionScope.currentUser.fullname}</strong>!</span>
          <a class="btn btn-light ms-2" href="${pageContext.request.contextPath}/signout" role="button">
            <fmt:message key="dang_xuat"/>
          </a>
        </c:when>
        <c:otherwise>
          <a class="btn btn-light me-2" href="${pageContext.request.contextPath}/signup" role="button">
            <fmt:message key="dang_ki"/>
          </a>
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/signin" role="button">
            <fmt:message key="dang_nhap"/>
          </a>
        </c:otherwise>
      </c:choose>
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