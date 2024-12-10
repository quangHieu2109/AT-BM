<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
    <fmt:setLocale value="vi_VN"/>
    <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
        <fmt:setLocale value="en_US"/>
    </c:if>
    <fmt:setBundle basename="lang"/>
    <jsp:include page="_meta.jsp"/>

    <title><fmt:message key="thiet_dat"/></title>

</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-pagetop bg-light">
    <div class="container">
        <h2 class="title-page"><fmt:message key="thiet_dat"/></h2>
    </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->
<%--  <c:set var="disable" value=""/>--%>
<c:if test="${sessionScope.currentUser.googleUser}">
    <c:set var="disable" value="disabled"/> </c:if>
<section class="section-content padding-y">
    <div class="container">
        <div class="row">
            <c:choose>
                <c:when test="${empty sessionScope.currentUser}">
                    <p>
                        <fmt:message key="vui_long"/> <a href="${pageContext.request.contextPath}/signin"><fmt:message
                            key="dang_nhap"/></a> <fmt:message key="de_su_dung_thiet_dat"/>.
                    </p>
                </c:when>
                <c:otherwise>
                    <jsp:include page="_navPanel.jsp">
                        <jsp:param name="active" value="SETTING"/>
                    </jsp:include>

                    <main class="col-md-9">
                        <article class="card">
                            <div class="card-body">
                                <c:if test="${not empty requestScope.successMessage}">
                                    <div class="alert alert-success" role="alert">${requestScope.successMessage}</div>
                                </c:if>
                                <c:if test="${not empty requestScope.errorMessage}">
                                    <div class="alert alert-danger" role="alert">${requestScope.errorMessage}</div>
                                </c:if>
                                <div class="col-lg-6">
                                    <form action="${pageContext.request.contextPath}/setting" method="post">
                                        <div class="mb-3">
                                            <label for="inputUsername" class="form-label"><fmt:message
                                                    key="ten_dang_nhap"/></label>
                                            <input type="text"
                                                   class="form-control"
                                                   id="inputUsername"
                                                   name="username"
                                                   value="${requestScope.user.username}"
                                                   disabled>
                                        </div>
                                        <div class="mb-3">
                                            <label for="inputFullname" class="form-label"><fmt:message
                                                    key="ho_va_ten"/></label>
                                            <input type="text"
                                                   class="form-control"
                                                   id="inputFullname"
                                                   name="fullname"
                                                   value="${requestScope.user.fullname}">
                                        </div>
                                        <div class="mb-3">
                                            <label for="inputEmail" class="form-label">Email</label>
                                            <input type="email"
                                                   class="form-control"
                                                   id="inputEmail"
                                                   name="email"
                                                   value="${requestScope.user.email}"
                                                ${disable}>
                                        </div>
                                        <div class="mb-3">
                                            <label for="inputPhoneNumber" class="form-label"><fmt:message
                                                    key="so_dien_thoai"/></label>
                                            <input type="text"
                                                   class="form-control"
                                                   id="inputPhoneNumber"
                                                   name="phoneNumber"
                                                   value="${requestScope.user.phoneNumber}">
                                        </div>
                                        <div class="mb-3">
                                            <div class="form-check d-inline-block me-4">
                                                <input class="form-check-input"
                                                       type="radio"
                                                       name="gender"
                                                       id="radioGender1"
                                                       value="0"
                                                    ${requestScope.user.gender == 0 ? 'checked' : ''}>
                                                <label class="form-check-label" for="radioGender1"><fmt:message
                                                        key="nam"/></label>
                                            </div>
                                            <div class="form-check d-inline-block">
                                                <input class="form-check-input"
                                                       type="radio"
                                                       name="gender"
                                                       id="radioGender2"
                                                       value="1"
                                                    ${requestScope.user.gender == 1 ? 'checked' : ''}>
                                                <label class="form-check-label" for="radioGender2"><fmt:message
                                                        key="nu"/></label>
                                            </div>
                                        </div>
<%--                                        <div class="card mb-3">--%>
<%--                                            <div class="card-header">--%>
<%--                                                <a href="#collapsePassword" data-bs-toggle="collapse" class="card-link">--%>
<%--                                                    <fmt:message key="dia_chi"/>--%>
<%--                                                </a>--%>
<%--                                            </div>--%>
<%--                                            <div id="collapsePassword" class="collapse">--%>
<%--                                                <div class="card-body">--%>
<%--                                                    <div class="mb-3">--%>
<%--                                                        <label for="houseNumber" class="form-label">Số nhà</label>--%>
<%--                                                        <input type="text"--%>
<%--                                                               class="form-control"--%>
<%--                                                               id="houseNumber"--%>
<%--                                                               name="houseNumber"--%>
<%--                                                               value="${requestScope.user.address.houseNumber}">--%>
<%--                                                    </div>--%>
<%--                                                    <div class="mb-3">--%>
<%--                                                        <label for="selectProvince" class="form-label">Tiình--%>
<%--                                                            thành</label>--%>
<%--                                                        <select id="selectProvince" name="province">--%>
<%--                                                            <option selected>${requestScope.user.address.province}</option>--%>

<%--                                                        </select>--%>
<%--                                                    </div>--%>

<%--                                                    <div class="mb-3">--%>
<%--                                                        <label for="selectDistrict" class="form-label">Quan</label>--%>
<%--                                                        <select id="selectDistrict" name="district">--%>
<%--                                                            <option selected>${requestScope.user.address.district}</option>--%>

<%--                                                        </select>--%>
<%--                                                    </div>--%>
<%--                                                    <div class="mb-3">--%>
<%--                                                        <label for="selectWard" class="form-label">Phuường/Xã</label>--%>
<%--                                                        <select id="selectWard" name="ward">--%>
<%--                                                            <option selected>${requestScope.user.address.ward}</option>--%>
<%--                                                        </select>--%>
<%--                                                    </div>--%>
<%--                                                </div>--%>
<%--                                            </div>--%>
<%--                                        </div>--%>

                                        <button type="submit" class="btn btn-primary w-100"><fmt:message
                                                key="cap_nhat_thong_tin_moi"/></button>
                                    </form>
                                </div>
                            </div> <!-- card-body.// -->
                        </article>
                    </main>
                    <!-- col.// -->
                </c:otherwise>
            </c:choose>
        </div> <!-- row.// -->
    </div> <!-- container.// -->
</section> <!-- section-content.// -->


<jsp:include page="_footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/address.js"></script>
</body>

</html>
