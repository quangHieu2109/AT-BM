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
                                    <th scope="col" style="min-width: 125px;">ID</th>
                                    <th scope="col" style="min-width: 100px;">public_Key</th>
                                    <th scope="col" style="min-width: 175px;"><fmt:message key="ngay_tao"/></th>
                                    <th scope="col" style="min-width: 175px;"><fmt:message key="trang_thai"/></th>
                                    <th scope="col" style="min-width: 175px;"><fmt:message key="so_don_hang_da_xac_thuc"/></th>
<%--                                    <th scope="col"><fmt:message key="thao_tac"/></th>--%>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th>1</th>
                                    <td>
                                    <span title="MFkwDQVJKOZIhvcNAQEBBOADSWAWSAJBAJyuXm55+dtASasGQJTWCOUZGsTwpC/M8MX2DWESACC4vLz4M/dhv/mGDg5mYgGXbGAtv0rsHvDTryanVIOVSOCAWEAAQ--">
                                     MFkwD...4A0==
                                    </span>
                                    </td>
                                    <td>12/12/2024</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="true">
                                                <span class="badge bg-info text-dark">Đang sử dụng</span>
                                            </c:when>
                                        </c:choose>

                                    </td>
                                    <td>
                                        11
                                    </td>
                                </tr>
                                <tr>
                                    <th>2</th>
                                    <td>
                                    <span title="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIQADQiruUBk/Z+R+6g0hM4fK84NFCwVRCTIUIMgooTmIYk0rPDN7CgpZ4u6OgDBIUfBWgSztEKTuvoZ4s+FDuUCAwEAAQ==">
                                    MFwwDQY...AQ==
                                    </span>
                                    </td>
                                    <td>12/12/2024</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="true">
                                                <span class="badge bg-danger">Đã dừng</span>
                                            </c:when>
                                        </c:choose>

                                    </td>
                                    <td>
                                        6
                                    </td>
                                </tr>
<%--                                <c:forEach var="order" items="${requestScope.orders}">--%>
<%--                                    <tr>--%>
<%--                                        <th scope="row">${order.id}</th>--%>
<%--                                        <td>${order.createdAt}</td>--%>
<%--                                        <td>${order.name}</td>--%>
<%--                                        <td><fmt:formatNumber pattern="#,##0" value="${order.total}"/>₫</td>--%>
<%--                                        <td>--%>
<%--                                            <c:choose>--%>
<%--                                                <c:when test="${order.status == 0}">--%>
<%--                                                    <span class="badge bg-info text-dark"><fmt:message key="dat_hang_thanh_cong"/></span>--%>
<%--                                                </c:when>--%>
<%--                                                <c:when test="${order.status == 1}">--%>
<%--                                                    <span class="badge bg-warning text-dark"><fmt:message key="dang_giao_hang"/></span>--%>
<%--                                                </c:when>--%>
<%--                                                <c:when test="${order.status == 2}">--%>
<%--                                                    <span class="badge bg-success"><fmt:message key="giao_hang_thanh_cong"/></span>--%>
<%--                                                </c:when>--%>
<%--                                                <c:when test="${order.status == 3}">--%>
<%--                                                    <span class="badge bg-danger"><fmt:message key="huy_don_hang"/></span>--%>
<%--                                                </c:when>--%>
<%--                                                <c:when test="${order.status == 4}">--%>
<%--                                                    <span class="badge bg-danger"><fmt:message key="tra_hang"/></span>--%>
<%--                                                </c:when>--%>
<%--                                            </c:choose>--%>
<%--                                        </td>--%>
<%--                                        <td class="text-center text-nowrap">--%>
<%--                                            <a class="btn btn-primary me-2"--%>
<%--                                               href="${pageContext.request.contextPath}/orderDetail?id=${order.id}"--%>
<%--                                               role="button">--%>
<%--                                                <fmt:message key="xem_don_hang"/>--%>
<%--                                            </a>--%>
<%--                                        </td>--%>
<%--                                    </tr>--%>
<%--                                </c:forEach>--%>
                                </tbody>
                            </table>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/authenticator">
                                <fmt:message key="tao_key"/>
                            </a>
                            <a class="btn btn-danger" href="${pageContext.request.contextPath}/authenticator">
                                <fmt:message key="report_key"/>
                            </a>
                        </div>

<%--                        <c:if test="${requestScope.totalPages != 0}">--%>
<%--                            <nav class="mt-4">--%>
<%--                                <ul class="pagination">--%>
<%--                                    <li class="page-item ${requestScope.page == 1 ? 'disabled' : ''}">--%>
<%--                                        <a class="page-link"--%>
<%--                                           href="${pageContext.request.contextPath}/order?page=${requestScope.page - 1}">--%>
<%--                                            <fmt:message key="trang_truoc"/>--%>
<%--                                        </a>--%>
<%--                                    </li>--%>

<%--                                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">--%>
<%--                                        <c:choose>--%>
<%--                                            <c:when test="${requestScope.page == i}">--%>
<%--                                                <li class="page-item active">--%>
<%--                                                    <a class="page-link">${i}</a>--%>
<%--                                                </li>--%>
<%--                                            </c:when>--%>
<%--                                            <c:otherwise>--%>
<%--                                                <li class="page-item">--%>
<%--                                                    <a class="page-link"--%>
<%--                                                       href="${pageContext.request.contextPath}/order?page=${i}">--%>
<%--                                                            ${i}--%>
<%--                                                    </a>--%>
<%--                                                </li>--%>
<%--                                            </c:otherwise>--%>
<%--                                        </c:choose>--%>
<%--                                    </c:forEach>--%>

<%--                                    <li class="page-item ${requestScope.page == requestScope.totalPages ? 'disabled' : ''}">--%>
<%--                                        <a class="page-link"--%>
<%--                                           href="${pageContext.request.contextPath}/order?page=${requestScope.page + 1}">--%>
<%--                                            <fmt:message key="trang_sau"/>--%>
<%--                                        </a>--%>
<%--                                    </li>--%>
<%--                                </ul>--%>
<%--                            </nav>--%>
<%--                        </c:if>--%>
                        </c:when>
                        <c:otherwise>
                            <div class="col-md-9">
                                <p class="alert alert-warning">
                                    <fmt:message key="ban_chua_co_key"/>! <fmt:message key="vui_long_tao_key"/>.
                                </p>
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/createKey">
                                    <fmt:message key="tao_key"/>
                                </a>
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

<jsp:include page="_footer.jsp"/>
</body>

</html>
