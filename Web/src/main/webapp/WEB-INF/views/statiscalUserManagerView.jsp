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
    <title><fmt:message key="quan_ly_thong_ke"/></title>
</head>

<body>
<jsp:include page="_headerAdmin.jsp"/>

<section class="section-content padding-y">
    <div class="container">
        <div class="row">
            <header class="section-heading py-4 d-flex justify-content-center">
                <h3 class="section-title"><fmt:message key="thong_ke_khach_hang"/></h3>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <nav class="header">
                                <a class="btn btn-outline-primary mr-2 " href="${pageContext.request.contextPath}/admin/statiscalManager/product"
                                   role="button"><fmt:message key="thong_ke_san_pham"/></a>
                                <a class="btn btn-outline-primary mr-2" href="${pageContext.request.contextPath}/admin/statiscalManager/user"
                                   role="button"><fmt:message key="thong_ke_khach_hang"/></a>
                                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/statiscalManager/productImport"
                                   role="button"><fmt:message key="thong_ke"/></a>
                            </nav>
                        </div>
                    </div>
                </div>
            </header>
            <aside class="mb-md-0 mb-3">
                <div class="card">
                    <form action="${pageContext.request.contextPath}/admin/statiscalManager/user" method="get" class="row g-3">
                        <div class="col-6">
                            <article class="filter-group">
                                <header class="card-header">
                                    <a data-bs-toggle="collapse" href="#collapse_1" aria-expanded="true" aria-controls="collapse_1">
                                        <i class="float-end bi bi-chevron-down"></i>
                                        <h6 class="title fw-bold"><fmt:message key="khoang_thoi_gian_da_khong_mua_hang_gan_day"/></h6>
                                    </a>
                                </header>
                                <div class="filter-content collapse show" id="collapse_1">
                                    <div class="card-body pt-0">
                                        <div class="form-group">
                                            <label for="months"><fmt:message key="so_thang"/></label>
                                            <input type="number" class="form-control"  id="months" name="months" value=${param.months} min="0"
                                                   max="12" step="1" required>
                                        </div>
                                    </div> <!-- card-body.// -->
                                </div>
                            </article>
                        </div>

                        <!-- Phần sắp xếp -->
                        <div class="col-6">
                            <article class="filter-group">
                                <header class="card-header">
                                    <a data-bs-toggle="collapse" href="#collapse_2" aria-expanded="true" aria-controls="collapse_2">
                                        <i class="float-end bi bi-chevron-down"></i>
                                        <h6 class="title fw-bold"><fmt:message key="sap_xep_theo"/></h6>
                                    </a>
                                </header>
                                <div class="filter-content collapse show" id="collapse_2">
                                    <div class="card-body pt-0">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" value="lastOrderTime-ASC" name="order" id="radio_order_1" ${requestScope.order == 'lastOrderTime-ASC' ? 'checked' : ''}>
                                            <label class="form-check-label" for="radio_order_1">
                                                <fmt:message key="don_hang_cu_nhat"/>
                                            </label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" value="lastOrderTime-DESC" name="order" id="radio_order_2" ${requestScope.order == 'lastOrderTime-DESC' ? 'checked' : ''}>
                                            <label class="form-check-label" for="radio_order_2">
                                                <fmt:message key="don_hang_gan_nhat"/>
                                            </label>
                                        </div>
                                    </div> <!-- card-body.// -->
                                </div>
                            </article>
                        </div>

                        <!-- Nút lọc -->
                        <div class="col-12">
                            <div class="card-body">
                                <button type="submit" class="btn btn-primary w-100">Lọc</button>
                            </div>
                        </div>
                    </form>
                </div> <!-- card.// -->
            </aside> <!-- col.// -->


            <main class="table-responsive-xl mb-5">
                <table class="table table-bordered table-striped table-hover align-middle">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">ID</th>
                        <th scope="col"><fmt:message key="ho_va_ten"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="ma_don_hang"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="thoi_diem_mua_hang_lan_cuoi"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="thao_tac"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" varStatus="loop" items="${requestScope.users}">
                        <tr>
                            <th scope="row">${loop.index + 1}</th>
                            <td>${user.userId}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/userManager/detail?id=${user.userId}"
                                   target="_blank">${user.userName}</a>
                            </td>
                            <td>
                                    ${user.lasOrderId}
                            </td>
                            <td>
                                <fmt:formatDate value="${user.lastOrderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>

                            <td class="text-center text-nowrap">
                                <a class="btn btn-primary me-2" href="${pageContext.request.contextPath}/admin/orderManager/detail?id=${user.lasOrderId}" role="button" ><fmt:message key="xem"/>
                                </a>
                            </td>
                                <%--                <td class="text-center text-nowrap">--%>
                                <%--                    <a class="btn btn-primary me-2"--%>
                                <%--                       href="${pageContext.request.contextPath}/admin/categoryManager/detail?id=${product.id}"--%>
                                <%--                       role="button">--%>
                                <%--                        <fmt:message key="xem"/>--%>
                                <%--                    </a>--%>
                                <%--                    <a class="btn btn-success me-2"--%>
                                <%--                       href="${pageContext.request.contextPath}/admin/categoryManager/update?id=${product.id}"--%>
                                <%--                       role="button">--%>
                                <%--                        <fmt:message key="sua"/>--%>
                                <%--                    </a>--%>
                                <%--                    <a class="btn btn-danger"--%>
                                <%--                       href="${pageContext.request.contextPath}/admin/categoryManager/delete?id=${product.id}"--%>
                                <%--                       role="button"--%>
                                <%--                       onclick="return confirm('Bạn có muốn xóa?')">--%>
                                <%--                        <fmt:message key="xoa"/>--%>
                                <%--                    </a>--%>
                                <%--                </td>--%>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </main>

            <c:if test="${requestScope.totalPages != 0}">
                <nav class="mt-3 mb-5">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${requestScope.page == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/admin/statiscalManager/user?page=${requestScope.page - 1}&months=${requestScope.months}&order=${requestScope.order}">
                                <fmt:message key="trang_truoc"/>
                            </a>
                        </li>

                        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                            <c:choose>
                                <c:when test="${requestScope.page == i}">
                                    <li class="page-item active">
                                        <a class="page-link">${i}</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/admin/statiscalManager/user?page=${i}&months=${requestScope.months}&order=${requestScope.order}">
                                                ${i}
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <li class="page-item ${requestScope.page == requestScope.totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/admin/statiscalManager/user?page=${requestScope.page + 1}&months=${requestScope.months}&order=${requestScope.order}">
                                <fmt:message key="trang_sau"/>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div> <!-- row.// -->
    </div> <!-- container.//  -->
</section> <!-- section-content.// -->
<jsp:include page="_footerAdmin.jsp"/>

</body>
</html>
