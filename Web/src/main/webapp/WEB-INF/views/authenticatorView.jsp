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
    <title>Authenticator</title>
    <style>
        .truncate {
            display: flex;
            max-width: 200px; /* Đặt chiều rộng tối đa */
            white-space: nowrap; /* Ngăn ngừa xuống dòng */
            overflow: hidden; /* Ẩn phần văn bản vượt quá */
            text-overflow: ellipsis; /* Thêm dấu ba chấm */
        }
    </style>
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-pagetop bg-light">
    <div class="container">
        <h2 class="title-page">Authentiactor</h2>
    </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->

<section class="section-content padding-y">
    <div class="container">
        <div class="row">
            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">

                    <jsp:include page="_navPanel.jsp">
                        <jsp:param name="active" value="AUTHENTICATOR"/>
                    </jsp:include>

                    <main class="col-md-9">
                    <c:choose>
                        <c:when test="${1 > 0}">
<%--                            kiểm tra xem người dùng có key hay chưa. Nếu có hiển thị table nếu chưa hiển thị thông báo và button create key.--%>
                        <div class="table-responsive-xxl">
                            <table class="table table-bordered table-striped table-hover align-middle">
                                <thead>
                                <tr>
                                    <th scope="col" style="min-width: 125px; max-width: 200px">ID</th>
                                    <th scope="col" style="min-width: 100px; max-width: 200px">public_Key</th>
                                    <th scope="col" style="min-width: 175px; max-width: 200px"><fmt:message key="ngay_tao"/></th>
                                    <th scope="col" style="min-width: 175px; max-width: 200px"><fmt:message key="trang_thai"/></th>
                                    <th scope="col" style="min-width: 175px; max-width: 200px"><fmt:message key="so_don_hang_da_xac_thuc"/></th>
<%--                                    <th scope="col"><fmt:message key="thao_tac"/></th>--%>
                                </tr>
                                </thead>
                                <tbody id="table_body">

                                </tbody>
                            </table>
                            <div class="button-container d-flex justify-content-between">
<%--                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/createAuthenticator">--%>
<%--                                <fmt:message key="tao_key"/>--%>
<%--                            </a>--%>
                            <button class="btn btn-danger"  id="reportKey">
                                <fmt:message key="report_key"/>
                            </button>
                            </div>
                        </div>

                        </c:when>
                        <c:otherwise>
                            <div class="col-md-9">
                                <p class="alert alert-warning">
                                    <fmt:message key="ban_chua_co_key"/>! <fmt:message key="vui_long_tao_key"/>.
                                </p>
                                <button class="btn btn-primary" >
                                    <fmt:message key="tao_key"/>
                                </button>
                            </div>
                        </c:otherwise>
                    </c:choose>
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
<script src="${pageContext.request.contextPath}/js/authenticator.js" type="text/javascript"></script>

<jsp:include page="_footer.jsp"/>
</body>

</html>
