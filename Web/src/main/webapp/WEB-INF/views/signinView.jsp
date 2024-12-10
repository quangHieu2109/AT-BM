<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <fmt:setLocale value="vi_VN"/>
    <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
        <fmt:setLocale value="en_US"/>
    </c:if>
    <fmt:setBundle basename="lang"/>
    <jsp:include page="_meta.jsp"/>
    <title><fmt:message key="dang_nhap"/></title>
    <script src="${pageContext.request.contextPath}/js/captcha.js"></script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <%--  <script src="https://www.google.com/recaptcha/api.js?render=6Ld7OPEpAAAAAH48Bhi161msl-sDAEk3UbiW_Jfw"></script>--%>
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-content" style="margin: 100px 0;">
    <div class="card mx-auto" style="max-width: 380px">
        <div class="card-body">
            <h4 class="card-title mb-4"><fmt:message key="dang_nhap"/></h4>
            <form action="${pageContext.request.contextPath}/signin" method="post">
                <div class="mb-3">
                    <input name="username"
                           class="form-control ${not empty requestScope.violations.usernameViolations
                   ? 'is-invalid' : (not empty requestScope.values.username ? 'is-valid' : '')}"
                           placeholder="<fmt:message key="ten_dang_nhap"/>"
                           type="text"
                           autocomplete="off"
                           value="${requestScope.values.username}">
                    <c:if test="${not empty requestScope.violations.usernameViolations}">
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.usernameViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <input name="password"
                           class="form-control ${not empty requestScope.violations.passwordViolations
                   ? 'is-invalid' : (not empty requestScope.values.password ? 'is-valid' : '')}"
                           placeholder="<fmt:message key="mat_khau"/>"
                           type="password"
                           autocomplete="off"
                           value="${requestScope.values.password}">
                    <c:if test="${not empty requestScope.violations.passwordViolations}">
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.passwordViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>
                <c:if test="${sessionScope.countWrongPass>=5}">
                    <div class="mb-3">
                        <div class="g-recaptcha" data-sitekey="6LcFrfEpAAAAAGVcBYPy1DAipxuTiCCu0JcZG0Qu"></div>

                    </div>
                    <div class="d-block invalid-feedback">
                        <ul class="list-unstyled">
                            <c:forEach var="violation" items="${requestScope.violations.captchaViolations}">
                                <li>${violation}</li>
                            </c:forEach></ul>
                    </div>
                </c:if>

                <button type="submit" class="btn btn-primary w-100"><fmt:message key="dang_nhap"/></button>
            </form>
        </div> <!-- card-body.// -->
    </div> <!-- card .// -->

    <p class="text-center mt-4"><fmt:message key="khong_co_tai_khoan"/>? <a
            href="${pageContext.request.contextPath}/signup"><fmt:message key="dang_ki"/></a></p>
    <div class="card mx-auto" style="max-width: 380px">

        <a id="siginInWithGoogle" href="https://accounts.google.com/o/oauth2/v2/auth?
			client_id=847546457050-7ccq01atin7m0ke5v2nva6u7i3k7bn6r.apps.googleusercontent.com
&response_type=code&scope=profile+email+https://www.googleapis.com/auth/user.gender.read+https://www.googleapis.com/auth/user.phonenumbers.read&redirect_uri=http://localhost:8080/googlehandle"
           class="btn"><img style="max-width: 30px;max-height: 30px;" src="./img/iconGoogle.png"><fmt:message
                key="dang_nhap_bang_google"/></a>
    </div>
</section> <!-- section-content.// -->

<jsp:include page="_footer.jsp"/>
</body>
<style>
    #siginInWithGoogle {
        background-color: #FEFEFF;
        color: black;
        border: none;
        text-align: center;
        text-decoration: none;
        display: inline-block;
        font-size: 16px;

    }

</style>
</html>
