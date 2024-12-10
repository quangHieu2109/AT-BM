<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <title><fmt:message key="doi_mat_khau"/></title>
</head>

<body>
<jsp:include page="_header.jsp"/>

<c:if test="${sessionScope.currentUser.googleUser}">

    <c:set var="disable" value="disabled"/> </c:if>

<section class="section-pagetop bg-light">
    <div class="container">
        <h2 class="title-page"><fmt:message key="doi_mat_khau"/></h2>
    </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->

<section class="section-content padding-y">
    <div class="container">
        <div class="row">
            <c:choose>
                <c:when test="${empty sessionScope.currentUser}">
                    <p>
                        <fmt:message key="vui_long"/> <a href="${pageContext.request.contextPath}/signin"><fmt:message
                            key="dang_nhap"/></a> <fmt:message key="de_doi_mat_khau"/>.
                    </p>
                </c:when>
                <c:otherwise>
                    <jsp:include page="_navPanel.jsp">
                        <jsp:param name="active" value="CHANGE_PASSWORD"/>
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
                                    <form action="${pageContext.request.contextPath}/changePassword" method="post">
                                        <div class="mb-3">
                                            <label for="inputCurrentPassword" class="form-label">
                                                <fmt:message key="nhap_mat_khau_hien_tai"/>
                                            </label>
                                            <input type="password"
                                                   class="form-control"
                                                   id="inputCurrentPassword"
                                                   name="currentPassword"
                                                ${disable}>
                                        </div>
                                        <div class="mb-3">
                                            <label for="inputNewPassword" class="form-label">
                                                <fmt:message key="nhap_mat_khau_moi"/>
                                            </label>
                                            <input type="password"
                                                   class="form-control"
                                                   id="inputNewPassword"
                                                   name="newPassword"
                                                ${disable}>
                                        </div>
                                        <div class="mb-3">
                                            <label for="inputNewPasswordAgain" class="form-label">
                                                <fmt:message key="nhap_mat_khau_moi_mot_lan_nua"/>
                                            </label>
                                            <input type="password"
                                                   class="form-control"
                                                   id="inputNewPasswordAgain"
                                                   name="newPasswordAgain"
                                                ${disable}>
                                        </div>
                                        <button type="submit" class="btn btn-primary w-100"><fmt:message
                                                key="doi_mat_khau"/></button>
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
</body>

</html>
