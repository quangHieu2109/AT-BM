<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<footer class="section-footer">
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />
  <section class="footer-bottom text-center bg-light border-top py-3">
    <div class="container-fluid">© 2022 — <fmt:message key="shop_ban_sach"/></div> <!-- container-fluid.// -->
  </section> <!-- footer-bottom.// -->
</footer> <!-- section-footer.// -->
