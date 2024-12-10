<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <jsp:include page="_header.jsp"/>
    <fmt:setLocale value="vi_VN"/>
    <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
        <fmt:setLocale value="en_US"/>
    </c:if>
    <fmt:setBundle basename="lang"/>
    <jsp:include page="_meta.jsp"/>
    <title><fmt:message key="dang_ki"/></title>
</head>

<body>

<section class="section-content" style="margin: 100px 0;">
    <div class="card mx-auto" style="max-width: 380px">
        <div class="card-body">
            <c:if test="${not empty requestScope.successMessage}">
                <div class="alert alert-success" role="alert">${requestScope.successMessage}</div>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="alert alert-danger" role="alert">${requestScope.errorMessage}</div>
            </c:if>
            <h4 class="card-title mb-4"><fmt:message key="dang_ki"/></h4>
            <form action="${pageContext.request.contextPath}/signup" method="post">
                <div class="mb-3">
                    <label for="inputUsername" class="form-label"><fmt:message key="ten_dang_nhap"/></label>
                    <input type="text"
                           class="form-control ${not empty requestScope.violations.usernameViolations
                   ? 'is-invalid' : (not empty requestScope.values.username ? 'is-valid' : '')}"
                           id="inputUsername"
                           name="username"
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
                    <label for="inputPassword" class="form-label"><fmt:message key="mat_khau"/></label>
                    <input type="password"
                           class="form-control ${not empty requestScope.violations.passwordViolations
                   ? 'is-invalid' : (not empty requestScope.values.password ? 'is-valid' : '')}"
                           id="inputPassword"
                           name="password"
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
                <div class="mb-3">
                    <label for="inputFullname" class="form-label"><fmt:message key="ho_va_ten"/></label>
                    <input type="text"
                           class="form-control ${not empty requestScope.violations.fullnameViolations
                   ? 'is-invalid' : (not empty requestScope.values.fullname ? 'is-valid' : '')}"
                           id="inputFullname"
                           name="fullname"
                           value="${requestScope.values.fullname}">
                    <c:if test="${not empty requestScope.violations.fullnameViolations}">
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.fullnameViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="inputEmail" class="form-label">Email</label>
                    <input type="email"
                           class="form-control ${not empty requestScope.violations.emailViolations
                   ? 'is-invalid' : (not empty requestScope.values.email ? 'is-valid' : '')}"
                           id="inputEmail"
                           name="email"
                           value="${requestScope.values.email}">
                    <c:if test="${not empty requestScope.violations.emailViolations}">
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.emailViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="inputPhoneNumber" class="form-label"><fmt:message key="so_dien_thoai"/></label>
                    <input type="text"
                           class="form-control ${not empty requestScope.violations.phoneNumberViolations
                   ? 'is-invalid' : (not empty requestScope.values.phoneNumber ? 'is-valid' : '')}"
                           id="inputPhoneNumber"
                           name="phoneNumber"
                           value="${requestScope.values.phoneNumber}">
                    <c:if test="${not empty requestScope.violations.phoneNumberViolations}">
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.phoneNumberViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <div class="form-check d-inline-block me-4">
                        <input class="form-check-input ${not empty requestScope.violations.genderViolations
                     ? 'is-invalid' : (not empty requestScope.values.gender ? 'is-valid' : '')}"
                               type="radio"
                               name="gender"
                               id="radioGender1"
                               value="0"
                        ${requestScope.values.gender.equals("0") ? 'checked' : ''}>
                        <label class="form-check-label" for="radioGender1"><fmt:message key="nam"/></label>
                    </div>
                    <div class="form-check d-inline-block">
                        <input class="form-check-input ${not empty requestScope.violations.genderViolations
                     ? 'is-invalid' : (not empty requestScope.values.gender ? 'is-valid' : '')}"
                               type="radio"
                               name="gender"
                               id="radioGender2"
                               value="1"
                        ${requestScope.values.gender.equals("1") ? 'checked' : ''}>
                        <label class="form-check-label" for="radioGender2"><fmt:message key="nu"/></label>
                    </div>
                    <c:if test="${not empty requestScope.violations.genderViolations}">
                        <div class="is-invalid"></div>
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.genderViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>

<%--                <div class="mb-3">--%>

<%--                    <label class="form-label"><fmt:message key="dia_chi"/></label><br>--%>
<%--                    <input type="text"  name="numberhouse" class="mb-3 ${not empty requestScope.violations.numberhouseViolations--%>
<%--                   ? 'is-invalid' : (not empty requestScope.values.numberhouse ? 'is-valid' : '')}" value="${empty requestScope.violations.numberhouseViolations?requestScope.values.numberhouse:''}" placeholder="So nha">--%>
<%--                    <select class="form-select mb-3 ${not empty requestScope.violations.provinceViolations--%>
<%--                   ? 'is-invalid' : (not empty requestScope.values.province ? 'is-valid' : '')}"--%>
<%--                            id="selectProvince"--%>
<%--                            name="province">--%>
<%--                        <c:if test="${empty requestScope.violations.provinceViolations}">--%>
<%--                            <option value="${requestScope.values.province}" selected>${requestScope.values.province}</option>--%>
<%--                        </c:if>--%>


<%--                    </select>--%>
<%--                    <c:if test="${not empty requestScope.violations.provinceViolations}">--%>
<%--                        <div class="invalid-feedback">--%>
<%--                            <ul class="list-unstyled">--%>
<%--                                <c:forEach var="violation" items="${requestScope.violations.provinceViolations}">--%>
<%--                                    <li>${violation}</li>--%>
<%--                                </c:forEach>--%>
<%--                            </ul>--%>
<%--                        </div>--%>
<%--                    </c:if>--%>
<%--                    <select class="form-select mb-3 ${not empty requestScope.violations.districtViolations--%>
<%--                   ? 'is-invalid' : (not empty requestScope.values.district ? 'is-valid' : '')}"--%>
<%--                            id="selectDistrict"--%>
<%--                            name="district">--%>
<%--                        <c:if test="${empty requestScope.violations.districtViolations}">--%>
<%--                            <option value="${requestScope.values.district}" selected>${requestScope.values.district}</option>--%>
<%--                        </c:if>--%>

<%--                    </select>--%>
<%--                    <c:if test="${not empty requestScope.violations.districtViolations}">--%>
<%--                        <div class="invalid-feedback">--%>
<%--                            <ul class="list-unstyled">--%>
<%--                                <c:forEach var="violation" items="${requestScope.violations.districtViolations}">--%>
<%--                                    <li>${violation}</li>--%>
<%--                                </c:forEach>--%>
<%--                            </ul>--%>
<%--                        </div>--%>
<%--                    </c:if>--%>

<%--                    <select class="form-select mb-3 ${not empty requestScope.violations.wardViolations--%>
<%--                   ? 'is-invalid' : (not empty requestScope.values.ward ? 'is-valid' : '')}"--%>
<%--                            id="selectWard"--%>
<%--                            name="ward">--%>
<%--                        <c:if test="${empty requestScope.violations.wardViolations}">--%>
<%--                            <option value="${requestScope.values.ward}" selected>${requestScope.values.ward}</option>--%>
<%--                        </c:if>--%>

<%--                    </select>--%>
<%--                    <c:if test="${not empty requestScope.violations.wardViolations}">--%>
<%--                        <div class="invalid-feedback">--%>
<%--                            <ul class="list-unstyled">--%>
<%--                                <c:forEach var="violation" items="${requestScope.violations.wardViolations}">--%>
<%--                                    <li>${violation}</li>--%>
<%--                                </c:forEach>--%>
<%--                            </ul>--%>
<%--                        </div>--%>
<%--                    </c:if>--%>

<%--                </div>--%>
                <div class="mb-3 form-check">
                    <input class="form-check-input ${not empty requestScope.violations.policyViolations
                   ? 'is-invalid' : (not empty requestScope.values.policy ? 'is-valid' : '')}"
                           type="checkbox"
                           value="checked"
                           id="checkboxPolicy"
                           name="policy"
                           checked>
                    <label class="form-check-label" for="checkboxPolicy">
                        <fmt:message key="dong_y_voi"/> <a href="#"><fmt:message key="dieu_khoan_su_dung"/></a>
                    </label>
                    <c:if test="${not empty requestScope.violations.policyViolations}">
                        <div class="invalid-feedback">
                            <ul class="list-unstyled">
                                <c:forEach var="violation" items="${requestScope.violations.policyViolations}">
                                    <li>${violation}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary w-100"><fmt:message key="dang_ki"/></button>
            </form>
        </div> <!-- card-body.// -->
    </div> <!-- card.// -->
    <p class="text-center mt-4"><fmt:message key="da_co_tai_khoan"/>? <a
            href="${pageContext.request.contextPath}/signin"><fmt:message key="dang_nhap"/></a></p>
</section> <!-- section-content.// -->

<jsp:include page="_footer.jsp"/>
<%--<script src="${pageContext.request.contextPath}/js/address.js"></script>--%>
</body>

</html>
