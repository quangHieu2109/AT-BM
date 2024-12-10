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
            <c:if test="${not empty requestScope.successMessage}">
                <div class="alert alert-success mb-3" role="alert">
                        ${requestScope.successMessage}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="alert alert-danger mb-3" role="alert">
                        ${requestScope.errorMessage}
                </div>
            </c:if>

            <header class="section-heading py-4 d-flex justify-content-between">
                <h3 class="section-title"><fmt:message key="thong_ke"/></h3>
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

                <form class="col-lg-6" method="POST" action="${pageContext.request.contextPath}/admin/statiscalManager/create"
                      enctype="multipart/form-data">
                        <input type="file" name="file" accept=".xls,.xlsx" required>
                        <button type="submit" class="btn btn-primary me-2 mb-3">
                            <fmt:message key="nhap_hang"/>
                        </button>
                </form>
            </header>
            <aside class="mb-md-0 mb-3">
                <div class="card">
                    <form action="${pageContext.request.contextPath}/admin/statiscalManager/productImport" method="get" class="row g-3">
                        <div class="col-6">
                            <article class="filter-group">
                                <header class="card-header">
                                    <a data-bs-toggle="collapse" href="#collapse_1" aria-expanded="true" aria-controls="collapse_1">
                                        <i class="float-end bi bi-chevron-down"></i>
                                        <h6 class="title fw-bold"><fmt:message key="moc_thoi_gian"/></h6>
                                    </a>
                                </header>
                                <div class="filter-content collapse show" id="collapse_1">
                                    <div class="card-body pt-0">
                                        <div class="form-group">
                                            <label for="startYear"><fmt:message key="tu"/></label>
                                            <input type="date" class="form-control" id="startYear" name="startYear" value="${param.startYear}" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="endYear"><fmt:message key="den"/></label>
                                            <input type="date" class="form-control" id="endYear" name="endYear" value="${param.endYear}" required>
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
                                            <input class="form-check-input" type="radio" value="createAt-DESC" name="order" id="radio_order_1" ${requestScope.order == 'createAt-DESC' ? 'checked' : ''}>
                                            <label class="form-check-label" for="radio_order_1">
                                                <fmt:message key="moi_nhat"/>
                                            </label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" value="createAt-ASC" name="order" id="radio_order_2" ${requestScope.order == 'createAt-ASC' ? 'checked' : ''}>
                                            <label class="form-check-label" for="radio_order_2">
                                                <fmt:message key="cu_nhat"/>
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
                        <th scope="col"><fmt:message key="ma_san_pham"/></th>
                        <th scope="col"><fmt:message key="ma_nguoi_nhap"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="ngay_san_pham_duoc_them_lan_dau"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="so_luong"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="gia"/></th>
                        <th scope="col" style="width: 110px;"><fmt:message key="ngay_nhap_san_pham"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="product" varStatus="loop" items="${requestScope.productsImport}">
                        <tr>
                            <th scope="row">${loop.index + 1}</th>
                            <td>${product.id}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/productManager/detail?id=${product.productId}"
                                   target="_blank"> ${product.productId}</a>
                                   </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/userManager/detail?id=${product.userId}"
                                   target="_blank">${product.userId}</a>
                            </td>
                            <td>
                                <fmt:formatDate value="${product.importAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                    ${product.quantity}
                            </td>
                            <td>
                                <fmt:formatNumber value="${product.price}" type="number" minFractionDigits="0" maxFractionDigits="0"/> VNĐ
                            </td>
                            <td>
                                <fmt:formatDate value="${product.createAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
                               href="${pageContext.request.contextPath}/admin/statiscalManager/productImport?page=${requestScope.page - 1}&startYear=${requestScope.startYear}&endYear=${requestScope.endYear}&order=${requestScope.order}">
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
                                           href="${pageContext.request.contextPath}/admin/statiscalManager/productImport?page=${i}&startYear=${requestScope.startYear}&endYear=${requestScope.endYear}&order=${requestScope.order}">
                                                ${i}
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <li class="page-item ${requestScope.page == requestScope.totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/admin/statiscalManager/productImport?page=${requestScope.page + 1}&startYear=${requestScope.startYear}&endYear=${requestScope.endYear}&order=${requestScope.order}">
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
