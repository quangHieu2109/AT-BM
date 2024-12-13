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

    <title>Authenticator</title>

</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-pagetop bg-light">
    <div class="container">
        <h2 class="title-page">Authenticator</h2>
    </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->
<%--  <c:set var="disable" value=""/>--%>
<%--<c:if test="${sessionScope.currentUser.googleUser}">--%>
<%--    <c:set var="disable" value="disabled"/> </c:if>--%>
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
                        <jsp:param name="active" value="AUTHENTICATOR"/>
                    </jsp:include>

                    <main class="col-md-9 d-flex justify-content-center align-items-center">
                        <article class="card w-100">
                            <div class="card-body">
                                <c:if test="${not empty requestScope.successMessage}">
                                    <div class="alert alert-success" role="alert">${requestScope.successMessage}</div>
                                </c:if>
                                <c:if test="${not empty requestScope.errorMessage}">
                                    <div class="alert alert-danger" role="alert">${requestScope.errorMessage}</div>
                                </c:if>
                                <i class="bi bi-arrow-left-square w-auto">
                                    <a href="${pageContext.request.contextPath}/authenticator" style="color: inherit; text-decoration: none;">Quay lại</a>
                                </i>

                                <div class="col-lg-6 mx-auto">

                                    <form action="${pageContext.request.contextPath}/createAuthenticator" method="post">
                                        <div class="mb-3">
                                            <label for="inputOTP" class="form-label">OTP</label>
                                            <input type="text"
                                                   class="form-control"
                                                   id="inputOTP"
                                                   name="OTP"
                                                   value="">
                                        </div>
                                        <div class="mb-3">
                                            <label for="showPublickey" class="form-label">Public key</label>
                                            <div class="input-group">
                                                <input type="password"
                                                       class="form-control"
                                                       id="showPublickey"
                                                       name="publicKey"
                                                       value="day la public key"
                                                       disabled>
                                                <span class="input-group-text" id="togglePublicKey" style="cursor: pointer;">
                                <i class="bi bi-eye"></i>
                            </span>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="showPrivatekey" class="form-label">Private key</label>
                                            <div class="input-group">
                                                <input type="password"
                                                       class="form-control"
                                                       id="showPrivatekey"
                                                       name="privateKey"
                                                       value="day la private key"
                                                       disabled>
                                                <span class="input-group-text" id="togglePrivateKey" style="cursor: pointer;">
                                <i class="bi bi-eye"></i>
                            </span>
                                            </div>
                                        </div>
                                        <div class="button-container d-flex justify-content-between">
                                            <button type="button" class="btn btn-warning w-30">
                                                <fmt:message key="gui_lai_OTP"/>
                                            </button>
                                            <button type="button" class="btn btn-warning w-30">
                                                <fmt:message key="export_file_privateKey"/>
                                            </button>

                                            <button type="submit" class="btn btn-primary w-30">
                                                <fmt:message key="xac_nhan_OTP"/>
                                            </button>
                                        </div>
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
<script src="${pageContext.request.contextPath}/js/authenticator.js"></script>
</body>

</html>
