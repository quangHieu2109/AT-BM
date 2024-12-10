<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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
  <title><fmt:message key="sua_danh_gia"/> #${requestScope.productReview.id}</title>
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-content">
  <div class="container">
    <header class="section-heading py-4">
      <h3 class="section-title"><fmt:message key="sua_danh_gia"/> #${requestScope.productReview.id}</h3>
    </header> <!-- section-heading.// -->

    <main class="row mb-5">
      <p>
        <fmt:message key="danh_gia_cua"/> <strong>${requestScope.productReview.user.fullname}</strong> <fmt:message key="tai"/>
        <strong>
          <a href="${pageContext.request.contextPath}/product?id=${requestScope.productReview.product.id}#review"
             target="_blank">
            ${requestScope.productReview.product.name}
          </a>
        </strong>
        <fmt:message key="luc"/> ${requestScope.productReview.createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))}
      </p>
      <form class="col-lg-6" method="POST" action="${pageContext.request.contextPath}/editProductReview">
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
        <div class="mb-3">
          <label for="productReview-ratingScore" class="form-label">Cho sao <span class="text-danger">*</span></label>
          <select class="form-select ${not empty requestScope.violations.ratingScoreViolations
                    ? 'is-invalid' : (not empty requestScope.productReview.ratingScore ? 'is-valid' : '')}"
                  id="productReview-ratingScore"
                  name="ratingScore"
                  required>
            <option disabled ${not empty requestScope.productReview.ratingScore ? '' : 'selected'}>
              <fmt:message key="cho_sao"/>
            </option>
            <c:forEach var="i" begin="1" end="5">
              <option value="${i}" ${requestScope.productReview.ratingScore == i ? 'selected' : ''}>${i}</option>
            </c:forEach>
          </select>
          <c:if test="${not empty requestScope.violations.ratingScoreViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled mb-0">
                <c:forEach var="violation" items="${requestScope.violations.ratingScoreViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>
        <div class="mb-3">
          <label for="productReview-content" class="form-label">Nội dung <span class="text-danger">*</span></label>
          <textarea class="form-control ${not empty requestScope.violations.contentViolations
                      ? 'is-invalid' : (not empty requestScope.productReview.content ? 'is-valid' : '')}"
                    id="productReview-content"
                    name="content"
                    placeholder="<fmt:message key="noi_dung_danh_gia"/>"
                    rows="5">${requestScope.productReview.content}</textarea>
          <c:if test="${not empty requestScope.violations.contentViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled mb-0">
                <c:forEach var="violation" items="${requestScope.violations.contentViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>
        <input type="hidden" name="id" value="${requestScope.productReview.id}">
        <button type="submit" class="btn btn-primary me-2 mb-3">
          <fmt:message key="sua"/>
        </button>
        <button type="reset"
                class="btn btn-warning me-2 mb-3"
                onclick="return confirm('Bạn có muốn để giá trị mặc định?')">
          <fmt:message key="mac_dinh"/>
        </button>
        <a class="btn btn-danger mb-3"
           href="${pageContext.request.contextPath}/product?id=${requestScope.productReview.product.id}#review"
           role="button"
           onclick="return confirm('Bạn có muốn hủy?')">
          <fmt:message key="huy"/>
        </a>
      </form>
    </main>
  </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="_footer.jsp"/>
</body>

</html>
