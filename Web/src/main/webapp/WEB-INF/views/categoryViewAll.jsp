<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
    <jsp:include page="_meta.jsp"/>
    <title>Danh mục sản phẩm</title>
</head>
<body>
<jsp:include page="_header.jsp"/>
<section class="section-content mb-2">
    <div class="container">
        <div class="row item-grid">
            <c:forEach var="category" items="${requestScope.categories}">
                <div class="col-lg-3 col-md-6">
                    <div class="card mb-4">
                        <div class="card-body">
                            <a href="${pageContext.request.contextPath}/category?id=${category.id}" class="stretched-link">
                                <div class="d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${empty category.imageName}">
                                            <img width="50" height="50" src="${pageContext.request.contextPath}/img/50px.png"
                                                 alt="50px.png">
                                        </c:when>
                                        <c:otherwise>
                                            <img width="50" height="50" src="${pageContext.request.contextPath}/image/${category.imageName}"
                                                 alt="${category.imageName}">
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="category-title ms-3">${category.name}</span>
                                </div>
                            </a>
                        </div>
                    </div>
                </div> <!-- col.// -->
            </c:forEach>
        </div> <!-- row.// -->
    </div> <!-- container.// -->
</section> <!-- section-content.// -->
<jsp:include page="_footer.jsp"/>
</body>
</html>
